package com.mitranetpars.sportmagazine.services;

import android.os.AsyncTask;

import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.SportMagazineApplication;
import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.security.User;

import java.net.ResponseCache;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
                }
                throw new Exception(ticket);
            }
        }
        ticket = ticket.replace("ticket", "");

        SecurityEnvironment.<SecurityEnvironment>getInstance().setUserName(userName);
        SecurityEnvironment.<SecurityEnvironment>getInstance().setLoginTicket(ticket);
        user.setTicket(ticket);
        return ticket;
    }

    private class LoginAsyncTask extends AsyncTask{

        @Override
        protected String doInBackground(Object[] callVariables) {
            Call<User> callTicket = securityServices.login((User) callVariables[0]);

            String ticket = null;
            try {
                Response<User> response = callTicket.execute();

                ticket = response.body().getTicket();
                if (ticket != null && !ticket.trim().isEmpty()) {
                    loginQueue.put(String.format("%s%s", "ticket", ticket));
                }
                else {
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
}
