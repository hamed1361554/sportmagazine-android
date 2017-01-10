package com.mitranetpars.sportmagazine.services;

import android.os.AsyncTask;
import android.text.Html;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mitranetpars.sportmagazine.R;
import com.mitranetpars.sportmagazine.SportMagazineApplication;
import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.invoice.Invoice;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceItem;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceSearchFilter;
import com.mitranetpars.sportmagazine.common.dto.invoice.ProducerInvoice;
import com.mitranetpars.sportmagazine.common.dto.invoice.ProducerInvoiceSearchFilter;

import java.util.ArrayList;
import java.util.Date;
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
    private final BlockingQueue<ArrayList<Invoice>> searchQueue = new ArrayBlockingQueue<ArrayList<Invoice>>(1, true);
    private final BlockingQueue<ArrayList<ProducerInvoice>> producerInvoiceSearchQueue =
            new ArrayBlockingQueue<ArrayList<ProducerInvoice>>(1, true);

    static {
        instance = new InvoiceServicesI();
    }

    public static InvoiceServicesI getInstance() {
        return instance;
    }

    private InvoiceServicesI(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SportMagazineApplication.GetServerUrl())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
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

    public ArrayList<Invoice> search(Date fromInvoiceDate, Date toInvoiceDate,
                                     int offset, int limit) throws Exception{
        InvoiceSearchFilter filter = new InvoiceSearchFilter();

        filter.setTicket(SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket());
        filter.setUserName(SecurityEnvironment.<SecurityEnvironment>getInstance().getUserName());

        filter.from_invoice_date = fromInvoiceDate;
        filter.to_invoice_date = toInvoiceDate;

        filter.__limit__ = limit;
        filter.__offset__ = offset;

        SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
        searchAsyncTask.execute(filter);

        ArrayList<Invoice> invoices = this.searchQueue.take();

        if (invoices.size() == 1) {
            Invoice p = invoices.get(0);
            if (p.getDate() == null || p.getInvoiceItems() == null || p.getInvoiceItems().size() == 0) {
                if (p.getComment() != null &&
                        p.getComment() != "") {
                    if (p.getComment().startsWith("error")) {
                        String error = p.getComment().substring(5);
                        throw new Exception(error);
                    }
                    if (p.getComment().startsWith("htmlerrorbody")) {
                        String error = p.getComment().substring(13);
                        throw new Exception(Html.fromHtml(error).toString());
                    }
                }
            }
        }

        return invoices;
    }

    public ArrayList<ProducerInvoice> searchProducerInvoices(Date fromInvoiceDate, Date toInvoiceDate,
                                     int wholesaleType, int offset, int limit) throws Exception{
        ProducerInvoiceSearchFilter filter = new ProducerInvoiceSearchFilter();

        filter.setTicket(SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket());
        filter.setUserName(SecurityEnvironment.<SecurityEnvironment>getInstance().getUserName());

        filter.from_invoice_date = fromInvoiceDate;
        filter.to_invoice_date = toInvoiceDate;
        filter.wholesale_type = wholesaleType;

        filter.__limit__ = limit;
        filter.__offset__ = offset;

        ProducerInvoiceSearchAsyncTask searchAsyncTask = new ProducerInvoiceSearchAsyncTask();
        searchAsyncTask.execute(filter);

        ArrayList<ProducerInvoice> invoices = this.producerInvoiceSearchQueue.take();

        if (invoices.size() == 1) {
            ProducerInvoice p = invoices.get(0);
            if (p.getInvoiceDate() == null || p.getItemProduct() == null) {
                if (p.getInvoiceComment() != null &&
                        p.getInvoiceComment() != "") {
                    if (p.getInvoiceComment().startsWith("error")) {
                        String error = p.getInvoiceComment().substring(5);
                        throw new Exception(error);
                    }
                    if (p.getInvoiceComment().startsWith("htmlerrorbody")) {
                        String error = p.getInvoiceComment().substring(13);
                        throw new Exception(Html.fromHtml(error).toString());
                    }
                }
            }
        }

        return invoices;
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

    private class SearchAsyncTask extends AsyncTask {

        @Override
        protected ArrayList<Invoice> doInBackground(Object[] callVariables) {
            Call<ArrayList<Invoice>> callInvoices = invoiceServices.search((InvoiceSearchFilter) callVariables[0]);

            ArrayList<Invoice> invoices = null;
            try {
                Response<ArrayList<Invoice>> response = callInvoices.execute();

                ArrayList<Invoice> body = response.body();
                if (body == null) {
                    ResponseBody errorBody = response.errorBody();
                    String error = errorBody.string();
                    Invoice inv = new Invoice();
                    inv.setComment(String.format("%s%s", "htmlerrorbody", error));
                    invoices = new ArrayList<Invoice>();
                    invoices.add(inv);
                    searchQueue.put(invoices);
                }
                else {
                    invoices = response.body();
                    if (invoices != null) {
                        searchQueue.put(invoices);
                    } else {
                        Invoice inv = new Invoice();
                        inv.setComment(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.could_not_search_product)));
                        invoices = new ArrayList<Invoice>();
                        invoices.add(inv);
                        searchQueue.put(invoices);
                    }
                }
            } catch (Exception productEx) {
                invoices = new ArrayList<Invoice>();
                try {
                    Invoice inv = new Invoice();
                    inv.setComment(String.format("%s%s", "error", productEx.getMessage()));
                    invoices.add(inv);
                    searchQueue.put(invoices);
                } catch (Exception queueEx){
                    Invoice inv = new Invoice();
                    inv.setComment(String.format("%s%s", "error", productEx.getMessage()));
                    invoices.add(inv);
                    searchQueue.add(invoices);
                }
            }

            return invoices;
        }
    }

    private class ProducerInvoiceSearchAsyncTask extends AsyncTask {

        @Override
        protected ArrayList<ProducerInvoice> doInBackground(Object[] callVariables) {
            Call<ArrayList<ProducerInvoice>> callInvoices =
                    invoiceServices.findProducerInvoices((ProducerInvoiceSearchFilter) callVariables[0]);

            ArrayList<ProducerInvoice> invoices = null;
            try {
                Response<ArrayList<ProducerInvoice>> response = callInvoices.execute();

                ArrayList<ProducerInvoice> body = response.body();
                if (body == null) {
                    ResponseBody errorBody = response.errorBody();
                    String error = errorBody.string();
                    ProducerInvoice inv = new ProducerInvoice();
                    inv.setInvoiceComment(String.format("%s%s", "htmlerrorbody", error));
                    invoices = new ArrayList<ProducerInvoice>();
                    invoices.add(inv);
                    producerInvoiceSearchQueue.put(invoices);
                }
                else {
                    invoices = response.body();
                    if (invoices != null) {
                        producerInvoiceSearchQueue.put(invoices);
                    } else {
                        ProducerInvoice inv = new ProducerInvoice();
                        inv.setInvoiceComment(String.format("%s%s", "error", SportMagazineApplication.getContext().getString(R.string.could_not_search_product)));
                        invoices = new ArrayList<ProducerInvoice>();
                        invoices.add(inv);
                        producerInvoiceSearchQueue.put(invoices);
                    }
                }
            } catch (Exception productEx) {
                invoices = new ArrayList<ProducerInvoice>();
                try {
                    ProducerInvoice inv = new ProducerInvoice();
                    inv.setInvoiceComment(String.format("%s%s", "error", productEx.getMessage()));
                    invoices.add(inv);
                    producerInvoiceSearchQueue.put(invoices);
                } catch (Exception queueEx){
                    ProducerInvoice inv = new ProducerInvoice();
                    inv.setInvoiceComment(String.format("%s%s", "error", productEx.getMessage()));
                    invoices.add(inv);
                    producerInvoiceSearchQueue.add(invoices);
                }
            }

            return invoices;
        }
    }
}
