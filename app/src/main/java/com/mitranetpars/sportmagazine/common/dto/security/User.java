package com.mitranetpars.sportmagazine.common.dto.security;

/**
 * Created by Hamed on 9/16/2016.
 */
public class User {
    public static int CONSUMER = 0;
    public static int PRODUCER = 1;

    public String user_name;
    public String password;
    public String full_name;
    public String mobile;
    public String email;
    public String address;
    public String phone;
    public String work_address;
    public String national_code;
    public int production_type;
    public int producer_division;
    public String producer_division_name;
    public String ticket;

    public void setUserName(String name){
        this.user_name = name;
    }

    public String getUserName(){
        return this.user_name;
    }

    public void setPassword(String pass){
        this.password = pass;
    }

    public String getPassword(){
        return this.password;
    }

    public void setFullName(String fullName){
        this.full_name = fullName;
    }

    public String getFullName(){
        return this.full_name;
    }

    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    public String getMobile(){
        return this.mobile;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    public void setWorkAddress(String workAddress){
        this.work_address = workAddress;
    }

    public String getWorkAddress(){
        return this.work_address;
    }

    public void setNationalCode(String code) {
        this.national_code = code;
    }

    public String getNationalCode() {
        return this.national_code;
    }

    public void setProductionType(int type) {
        this.production_type = type;
    }

    public void setProducerDivision(int division){
        this.producer_division = division;
    }

    public int getProducerDivision() {
        return this.producer_division;
    }

    public void setProducerDivisionName(String divisionName) {
        this.producer_division_name = divisionName;
    }

    public String getProducerDivisionName() {
        return this.producer_division_name;
    }

    public int getProductionType() {
        return this.production_type;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicket() {
        return ticket;
    }
}
