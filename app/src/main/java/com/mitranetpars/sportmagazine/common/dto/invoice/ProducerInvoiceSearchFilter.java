package com.mitranetpars.sportmagazine.common.dto.invoice;

import com.mitranetpars.sportmagazine.common.dto.CommonDataTransferObject;

import java.util.Date;

/**
 * Created by Hamed on 1/9/2017.
 */

public class ProducerInvoiceSearchFilter extends CommonDataTransferObject {
    public Date from_invoice_date;
    public Date to_invoice_date;
    public int wholesale_type;

    public int __offset__;
    public int __limit__;
}
