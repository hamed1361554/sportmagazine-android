package com.mitranetpars.sportmagazine.services;

import com.mitranetpars.sportmagazine.common.dto.invoice.Invoice;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceItem;
import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceSearchFilter;
import com.mitranetpars.sportmagazine.common.dto.invoice.ProducerInvoice;
import com.mitranetpars.sportmagazine.common.dto.invoice.ProducerInvoiceSearchFilter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Hamed on 12/2/2016.
 */

public interface InvoiceServices {
    @POST("/invoice/register")
    Call<String> register(@Body Invoice invoice);

    @POST("/invoice/find")
    Call<ArrayList<Invoice>> search(@Body InvoiceSearchFilter filter);

    @POST("/invoice/producer")
    Call<ArrayList<ProducerInvoice>> findProducerInvoices(@Body ProducerInvoiceSearchFilter filter);
}
