package com.mitranetpars.sportmagazine.services;

import android.os.AsyncTask;
import android.text.Html;

import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.SportMagazineApplication;
import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.security.User;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hamed on 9/16/2016.
 */
public class SecurityServicesI {
    private static final SecurityServicesI instance;
    private final SecurityServices securityServices;

    private final BlockingQueue<String> loginQueue = new ArrayBlockingQueue<String>(1, true);
    private final BlockingQueue<User> createQueue = new ArrayBlockingQueue<User>(1, true);
    private final BlockingQueue<User> getUserQueue = new ArrayBlockingQueue<User>(1, true);
    private final BlockingQueue<String> activateQueue = new ArrayBlockingQueue<String>(1, true);

    static {
        instance = new SecurityServicesI();
    }

    public static SecurityServicesI getInstance() {
        return instance;
    }

    private SecurityServicesI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SportMagazineApplication.GetServerUrl())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.securityServices = retrofit.create(SecurityServices.class);
    }

    public String login(String userName, String password) throws Exception {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);

        LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
        loginAsyncTask.execute(user);

        String ticket = this.loginQueue.take();
        if (ticket == null || ticket.trim().isEmpty() || !ticket.startsWith("ticket"))
        {
            loginAsyncTask.cancel(true);
            if (ticket == null || ticket.trim().isEmpty()) {
                throw new Exception(SportMagazineApplication.getContext().getString(R.string.OutOfService));
            }
            else {
                if (ticket.startsWith("error")){
                    ticket = ticket.substring(5);
                    throw new Exception(ticket);
                }
                if (ticket.startsWith("htmlerrorbody")) {
                    ticket = ticket.substring(13);
                    throw new Exception(Html.fromHtml(ticket).toString());
                }
            }
        }
        ticket = ticket.replace("ticket", "");

        SecurityEnvironment.<SecurityEnvironment>getInstance().setUserName(userName);
        SecurityEnvironment.<SecurityEnvironment>getInstance().setLoginTicket(ticket);
        user.setTicket(ticket);
        return ticket;
    }

    public User create(String userName, String password, String fullName, String email, String mobile, String address) throws Exception {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setAddress(address);

        return createUser(user);
    }

    public User createProducer(String userName, String password, String fullName, String email, String mobile, String address,
                               String phone, String nationalCode, int producerDivision, String producerDivisionName,
                               int productionPackage) throws Exception {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setAddress(address);
        user.setPhone(phone);
        user.setNationalCode(nationalCode);
        user.setProductionType(User.PRODUCER);
        user.setProducerDivision(producerDivision);
        user.setProducerDivisionName(producerDivisionName);
        user.setProductionPackage(productionPackage);

        return createUser(user);
    }

    private User createUser(User user) throws Exception {
        CreateAsyncTask createAsyncTask = new CreateAsyncTask();
        createAsyncTask.execute(user);

        User created_user = this.createQueue.take();
        if (created_user == null) {
            throw new Exception(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.OutOfService)));
        }

        if (created_user.getUserName() == null || created_user.getUserName() == "" ||
                created_user.getPassword() == null || created_user.getPassword() == ""){
            if (created_user.getTicket().startsWith("error")){
                String error = created_user.getTicket().substring(5);
                throw new Exception(error);
            }
            if (created_user.getTicket().startsWith("htmlerrorbody")) {
                String error = created_user.getTicket().substring(13);
                throw new Exception(Html.fromHtml(error).toString());
            }
        }

        SecurityEnvironment.<SecurityEnvironment>getInstance().setUserName(created_user.getUserName());
        return created_user;
    }

    public User getUser(String userName) throws Exception {
        User user = new User();
        user.setUserName(userName);
        user.setTicket(SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket());

        GetUserAsyncTask getUserAsyncTask = new GetUserAsyncTask();
        getUserAsyncTask.execute(user);

        User created_user = this.getUserQueue.take();
        if (created_user == null) {
            throw new Exception(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.OutOfService)));
        }

        if (created_user.getUserName() == null || created_user.getUserName() == "" ||
                created_user.getFullName() == null || created_user.getFullName() == ""){
            if (created_user.getTicket().startsWith("error")){
                String error = created_user.getTicket().substring(5);
                throw new Exception(error);
            }
            if (created_user.getTicket().startsWith("htmlerrorbody")) {
                String error = created_user.getTicket().substring(13);
                throw new Exception(Html.fromHtml(error).toString());
            }
        }

        SecurityEnvironment.<SecurityEnvironment>getInstance().setUser(created_user);
        return created_user;
    }

    public String activate(String data) throws Exception {
        ActivateAsyncTask activateAsyncTask = new ActivateAsyncTask();
        activateAsyncTask.execute(data);

        String url = this.activateQueue.take();
        if (url == null || url == "") {
            throw new Exception(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.OutOfService)));
        }

        if (url.startsWith("error")) {
            String error = url.substring(5);
            throw new Exception(error);
        }
        if (url.startsWith("htmlerrorbody")) {
            String error = url.substring(13);
            throw new Exception(Html.fromHtml(url).toString());
        }

        return url;
    }

    private class LoginAsyncTask extends AsyncTask{

        @Override
        protected String doInBackground(Object[] callVariables) {
            Call<User> callTicket = securityServices.login((User) callVariables[0]);

            String ticket = null;
            try {
                Response<User> response = callTicket.execute();

                User body = response.body();
                if (body == null) {
                    ResponseBody errorBody = response.errorBody();
                    String error = errorBody.string();
                    loginQueue.put(String.format("%s%s", "htmlerrorbody", error));
                }
                else {
                    ticket = response.body().getTicket();
                    if (ticket != null && !ticket.trim().isEmpty()) {
                        loginQueue.put(String.format("%s%s", "ticket", ticket));
                    } else {
                        ticket = response.message();

                        if (ticket == null || ticket.trim().isEmpty()) {
                            ticket = response.errorBody().string();
                        }

                        if (ticket == null || ticket.trim().isEmpty()) {
                            loginQueue.put(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.OutOfService)));
                        } else {
                            loginQueue.put(String.format("%s%s", "error", ticket));
                        }
                    }
                }
            } catch (Exception ticketEx) {
                try {
                    loginQueue.put(String.format("%s%s", "error", ticketEx.getMessage()));
                } catch (Exception queueEx){
                    loginQueue.add(String.format("%s%s", "error", queueEx.getMessage()));
                }
            }

            return ticket;
        }
    }

    private class CreateAsyncTask extends AsyncTask{

        @Override
        protected User doInBackground(Object[] callVariables) {
            Call<User> callUser = securityServices.create((User) callVariables[0]);

            User user = null;
            try {
                Response<User> response = callUser.execute();

                User body = response.body();
                if (body == null) {
                    ResponseBody errorBody = response.errorBody();
                    String error = errorBody.string();
                    user = new User();
                    user.setTicket(String.format("%s%s", "htmlerrorbody", error));
                    createQueue.put(user);
                }
                else {
                    user = response.body();
                    if (user != null) {
                        createQueue.put(user);
                    } else {
                        user = new User();
                        user.setTicket(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.could_not_create_user)));
                        createQueue.put(user);
                    }
                }
            } catch (Exception ticketEx) {
                user = new User();
                try {
                    user.setTicket(String.format("%s%s", "error", ticketEx.getMessage()));
                    createQueue.put(user);
                } catch (Exception queueEx){
                    user.setTicket(String.format("%s%s", "error", ticketEx.getMessage()));
                    createQueue.add(user);
                }
            }

            return user;
        }
    }

    private class GetUserAsyncTask extends AsyncTask{

        @Override
        protected User doInBackground(Object[] callVariables) {
            Call<User> callUser = securityServices.getUser((User) callVariables[0]);

            User user = null;
            try {
                Response<User> response = callUser.execute();

                User body = response.body();
                if (body == null) {
                    ResponseBody errorBody = response.errorBody();
                    String error = errorBody.string();
                    user = new User();
                    user.setTicket(String.format("%s%s", "htmlerrorbody", error));
                    getUserQueue.put(user);
                }
                else {
                    user = response.body();
                    if (user != null) {
                        getUserQueue.put(user);
                    } else {
                        user = new User();
                        user.setTicket(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.user_not_found)));
                        getUserQueue.put(user);
                    }
                }
            } catch (Exception ticketEx) {
                user = new User();
                try {
                    user.setTicket(String.format("%s%s", "error", ticketEx.getMessage()));
                    getUserQueue.put(user);
                } catch (Exception queueEx){
                    user.setTicket(String.format("%s%s", "error", ticketEx.getMessage()));
                    getUserQueue.add(user);
                }
            }

            return user;
        }
    }

    private class ActivateAsyncTask extends AsyncTask{

        @Override
        protected String doInBackground(Object[] callVariables) {
            Call<String> callActivate = securityServices.activate(callVariables[0].toString());

            String url = null;
            try {
                Response<String> response = callActivate.execute();

                String body = response.body();
                if (body == null) {
                    ResponseBody errorBody = response.errorBody();
                    String error = errorBody.string();
                    activateQueue.put(String.format("%s%s", "htmlerrorbody", error));
                }
                else {
                    url = response.body();
                    if (url != null && url != "") {
                        activateQueue.put(url);
                    } else {
                        activateQueue.put(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.could_not_create_user)));
                    }
                }
            } catch (Exception ticketEx) {
                try {
                    activateQueue.put(String.format("%s%s", "error", ticketEx.getMessage()));
                } catch (Exception queueEx){
                    activateQueue.add(String.format("%s%s", "error", ticketEx.getMessage()));
                }
            }

            return url;
        }
    }
}
