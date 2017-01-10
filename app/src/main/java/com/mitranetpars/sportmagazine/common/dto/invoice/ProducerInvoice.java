package com.mitranetpars.sportmagazine.common.dto.invoice;

import com.mitranetpars.sportmagazine.common.dto.CommonDataTransferObject;
import com.mitranetpars.sportmagazine.common.dto.product.Product;
import com.mitranetpars.sportmagazine.common.dto.security.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hamed on 1/9/2017.
 */

public class ProducerInvoice extends CommonDataTransferObject {
    public String invoice_id;
    public Date invoice_date;
    public int invoice_status;
    public String invoice_consumer_user_id;
    public User invoice_consume_user;
    public String invoice_comment;
    public String item_id;
    public int item_row;
    public String item_color;
    public String item_size;
    public String item_brand;
    public double item_price;
    public int item_quantity;
    public String item_product_id;
    public Product product;

    public void setInvoiceID(String id){
        this.invoice_id = id;
    }

    public String getInvoiceID() {
        return this.invoice_id;
    }

    public void setInvoiceDate(Date date) {
        this.invoice_date = date;
    }

    public Date getInvoiceDate() {
        return this.invoice_date;
    }

    public void setInvoiceStatus(int status){
        this.invoice_status = status;
    }

    public int getInvoiceStatus(){
        return this.invoice_status;
    }

    public void setInvoiceConsumerUserID(String userID){
        this.invoice_consumer_user_id = userID;
    }

    public String getInvoiceConsumerUserID(){
        return this.invoice_consumer_user_id;
    }

    public void setInvoiceComment(String comment){
        this.invoice_comment = comment;
    }

    public String getInvoiceComment(){
        return this.invoice_comment;
    }

    public User getConsumerUser(){
        return this.invoice_consume_user;
    }

    public void setItemPrice(double price){
        this.item_price = price;
    }

    public double getItemPrice(){
        return this.item_price;
    }

    public void setItemQuantity(int quantity){
        this.item_quantity = quantity;
    }

    public int getItemQuantity(){
        return this.item_quantity;
    }

    public void setItemProductID(String id){
        this.item_product_id = id;
    }

    public String getItemProductID(){
        return this.item_product_id;
    }

    public void setItemProduct(Product p){
        this.product = p;
    }

    public Product getItemProduct(){
        return this.product;
    }

    public void setItemID(String id) {
        this.item_id = id;
    }

    public String getItemID() {
        return this.item_id;
    }

    public void setItemRow(int row) {
        this.item_row = row;
    }

    public int getItemRow() {
        return this.item_row;
    }

    public void setItemColor(String color) {
        this.item_color = color;
    }

    public String getItemColor() {
        return this.item_color;
    }

    public void setItemSize(String size){
        this.item_size = size;
    }

    public String getItemSize(){
        return this.item_size;
    }

    public void setItemBrand(String brand){
        this.item_brand = brand;
    }

    public String getItemBrand() {
        return this.item_brand;
    }
}
