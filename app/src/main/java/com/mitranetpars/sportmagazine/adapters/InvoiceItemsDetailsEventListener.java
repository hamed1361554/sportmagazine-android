package com.mitranetpars.sportmagazine.adapters;

import com.mitranetpars.sportmagazine.common.dto.invoice.InvoiceItem;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * Created by Hamed on 12/9/2016.
 */

public interface InvoiceItemsDetailsEventListener extends EventListener {
    void onItemsDetailsShow(ArrayList<InvoiceItem> items);
}
