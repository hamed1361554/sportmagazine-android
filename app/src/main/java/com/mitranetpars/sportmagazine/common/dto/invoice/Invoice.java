package com.mitranetpars.sportmagazine.common.dto.invoice;

import com.mitranetpars.sportmagazine.common.dto.CommonDataTransferObject;
import com.mitranetpars.sportmagazine.common.dto.security.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hamed on 12/2/2016.
 */

public class Invoice extends CommonDataTransferObject {
    public String id;
    public ArrayList<InvoiceItem> invoice_items;
    public Date date;
    public int status;
    public String consumer_user_id;
    public User consume_user;
    public String comment;
    public double total_price;

    public void setID(String id){
        this.id = id;
    }

    public String getID() {
        return this.id;
    }

    public void setInvoiceItems(ArrayList<InvoiceItem> items){
        this.invoice_items = items;
    }

    public ArrayList<InvoiceItem> getInvoiceItems() {
        return this.invoice_items;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return this.date;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }

    public void setConsumerUserID(String userID){
        this.consumer_user_id = userID;
    }

    public String getConsumerUserID(){
        return this.consumer_user_id;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return this.comment;
    }

    public void setTotalPrice(double totalPrice){
        this.total_price = totalPrice;
    }

    public double getTotalPrice(){
        if (this.total_price > 0) return this.total_price;

        double sum = 0;
        for (InvoiceItem item: this.getInvoiceItems()){
            sum += item.getPrice() * item.getQuantity();
        }
        return sum;
    }

    public User getConsumerUser(){
        return this.consume_user;
    }
}
