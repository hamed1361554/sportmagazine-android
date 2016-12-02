package com.mitranetpars.sportmagazine.common.dto.invoice;

import com.mitranetpars.sportmagazine.common.dto.CommonDataTransferObject;
import com.mitranetpars.sportmagazine.common.dto.product.Product;

/**
 * Created by Hamed on 12/2/2016.
 */

public class InvoiceItem extends CommonDataTransferObject {
    public double price;
    public int quantity;
    public String product_id;
    public Product product;

    public void setPrice(double price){
        this.price = price;
    }

    public double getPrice(){
        return this.price;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setProductID(String id){
        this.product_id = id;
    }

    public String getProductID(){
        return this.product_id;
    }

    public void setProduct(Product p){
        this.product = p;
    }

    public Product getProduct(){
        return this.product;
    }
}
