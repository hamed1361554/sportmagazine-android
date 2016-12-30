package com.mitranetpars.sportmagazine.common.dto.product;

import com.mitranetpars.sportmagazine.common.dto.CommonDataTransferObject;

import java.util.Date;

/**
 * Created by Hamed on 12/1/2016.
 */

public class ProductSearchFilter extends CommonDataTransferObject {
    public Date from_creation_date;
    public Date to_creation_date;
    public double from_price;
    public double to_price;
    public String name;
    public int categories;
    public int age_categories;
    public int gender;
    public int wholesale_type;
    public String size;
    public String brand;
    public boolean just_current_user;
    public boolean include_out_of_stock;

    public int __offset__;
    public int __limit__;
}
