package com.mitranetpars.sportmagazine.common.dto.invoice;

import com.mitranetpars.sportmagazine.common.dto.CommonDataTransferObject;

import java.util.Date;

/**
 * Created by Hamed on 12/6/2016.
 */

public class InvoiceSearchFilter extends CommonDataTransferObject {
    public Date from_invoice_date;
    public Date to_invoice_date;

    public int __offset__;
    public int __limit__;
}
