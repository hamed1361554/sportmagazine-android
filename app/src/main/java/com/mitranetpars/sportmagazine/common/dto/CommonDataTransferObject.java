package com.mitranetpars.sportmagazine.common.dto;

/**
 * Created by Hamed on 11/30/2016.
 */

public class CommonDataTransferObject {
    public String ticket;
    public String user_name;

    public void setTicket(String ticket){
        this.ticket = ticket;
    }

    public void setUserName (String name) {
        this.user_name = name;
    }
}
