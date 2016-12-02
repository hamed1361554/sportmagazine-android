package com.mitranetpars.sportmagazine.common.dto.product;

import com.mitranetpars.sportmagazine.Cart.Saleable;
import com.mitranetpars.sportmagazine.common.dto.CommonDataTransferObject;

/**
 * Created by Hamed on 11/21/2016.
 */

public class Product extends CommonDataTransferObject implements Saleable{
    public String id;
    public String name;
    public double price;
    public int category;
    public String colors;
    public String sizes;
    public String brands;
    public int counter;
    public int age_category;
    public int gender;
    public int wholesale_type;
    public String comment;
    public String image;

    public void setID(String id){
        this.id = id;
    }

    public String getID(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public double getPrice(){
        return this.price;
    }

    public void setCategory(int category){
        this.category = category;
    }

    public void setColors(String colors){
        this.colors = colors;
    }

    public void setSizes(String sizes){
        this.sizes = sizes;
    }

    public void setBrands(String brands){
        this.brands = brands;
    }

    public void setCounter(int counter){
        this.counter = counter;
    }

    public void setAgeCategory(int ageCategory){
        this.age_category = ageCategory;
    }

    public void setGender(int gender){
        this.gender = gender;
    }

    public void setWholesaleType(int wholesaleType){
        this.wholesale_type = wholesaleType;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return this.comment;
    }

    public void setImage(String img){
        this.image = img;
    }

    public String getImage(){
        return this.image;
    }

    public String[] getColorsArray(){
        if (this.colors == null || this.colors == ""){
            return new String[0];
        }

        if (!this.colors.contains(",")){
            return new String[] {this.colors};
        }

        return this.colors.split(",");
    }

    public String[] getSizesArray(){
        if (this.sizes == null || this.sizes == ""){
            return new String[0];
        }

        if (!this.sizes.contains(",")){
            return new String[] {this.sizes};
        }

        return this.sizes.split(",");
    }

    public String[] getBrandsArray(){
        if (this.brands == null || this.brands == ""){
            return new String[0];
        }

        if (!this.brands.contains(",")){
            return new String[] {this.brands};
        }

        return this.brands.split(",");
    }
}
