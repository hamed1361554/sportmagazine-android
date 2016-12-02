package com.mitranetpars.sportmagazine.services;

import android.os.AsyncTask;
import android.text.Html;

import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.SportMagazineApplication;
import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.invoice.Invoice;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceItem;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hamed on 12/2/2016.
 */

public class InvoiceServicesI {
    private static final InvoiceServicesI instance;
    private final InvoiceServices invoiceServices;

    private final BlockingQueue<String> registerQueue = new ArrayBlockingQueue<String>(1, true);

    static {
        instance = new InvoiceServicesI();
    }

    public static InvoiceServicesI getInstance() {
        return instance;
    }

    private InvoiceServicesI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SportMagazineApplication.GetServerUrl())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.invoiceServices = retrofit.create(InvoiceServices.class);
    }

    public String register(ArrayList<InvoiceItem> items) throws Exception {
        Invoice invoice = new Invoice();
        invoice.setUserName(SecurityEnvironment.<SecurityEnvironment>getInstance().getUserName());
        invoice.setTicket(SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket());
        invoice.setInvoiceItems(items);

        RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask();
        registerAsyncTask.execute(invoice);

        String invoiceID = this.registerQueue.take();
        if (invoiceID == null || invoiceID.trim().isEmpty() || !invoiceID.startsWith("ticket"))
        {
            registerAsyncTask.cancel(true);
            if (invoiceID == null || invoiceID.trim().isEmpty()) {
                throw new Exception(SportMagazineApplication.getContext().getString(R.string.OutOfService));
            }
            else {
                if (invoiceID.startsWith("error")){
                    invoiceID = invoiceID.substring(5);
                    throw new Exception(invoiceID);
                }
                if (invoiceID.startsWith("htmlerrorbody")) {
                    invoiceID = invoiceID.substring(13);
                    throw new Exception(Html.fromHtml(invoiceID).toString());
                }
            }
        }
        invoiceID = invoiceID.replace("invoiceID", "");
        return invoiceID;
    }

    private class RegisterAsyncTask extends AsyncTask {

        @Override
        protected String doInBackground(Object[] callVariables) {
            Call<String> callRegister = invoiceServices.register((Invoice) callVariables[0]);

            String invoiceID = null;
            try {
                Response<String> response = callRegister.execute();

                String body = response.body();
                if (body == null) {
                    ResponseBody errorBody = response.errorBody();
                    String error = errorBody.string();
                    registerQueue.put(String.format("%s%s", "htmlerrorbody", error));
                }
                else {
                    invoiceID = response.body().toString();
                    if (invoiceID != null && !invoiceID.trim().isEmpty()) {
                        registerQueue.put(String.format("%s%s", "invoiceID", invoiceID));
                    } else {
                        invoiceID = response.message();

                        if (invoiceID == null || invoiceID.trim().isEmpty()) {
                            invoiceID = response.errorBody().string();
                        }

                        if (invoiceID == null || invoiceID.trim().isEmpty()) {
                            registerQueue.put(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.OutOfService)));
                        } else {
                            registerQueue.put(String.format("%s%s", "error", invoiceID));
                        }
                    }
                }
            } catch (Exception ticketEx) {
                try {
                    registerQueue.put(String.format("%s%s", "error", ticketEx.getMessage()));
                } catch (Exception queueEx){
                    registerQueue.add(String.format("%s%s", "error", queueEx.getMessage()));
                }
            }

            return invoiceID;
        }
    }
}
