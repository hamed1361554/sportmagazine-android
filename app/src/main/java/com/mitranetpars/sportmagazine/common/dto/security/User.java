package com.mitranetpars.sportmagazine.common.dto.security;

/**
 * Created by Hamed on 9/16/2016.
 */
public class User {
    public String user_name;
    public String password;
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

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicket() {
        return ticket;
    }
}
