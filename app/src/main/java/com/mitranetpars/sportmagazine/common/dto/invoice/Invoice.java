package com.mitranetpars.sportmagazine.common.dto.invoice;

import com.mitranetpars.sportmagazine.common.dto.CommonDataTransferObject;

import java.util.ArrayList;

/**
 * Created by Hamed on 12/2/2016.
 */

public class Invoice extends CommonDataTransferObject {
    public ArrayList<InvoiceItem> invoice_items;

    public void setInvoiceItems(ArrayList<InvoiceItem> items){
        this.invoice_items = items;
    }

    public ArrayList<InvoiceItem> getInvoiceItems() {
        return this.invoice_items;
    }
}
