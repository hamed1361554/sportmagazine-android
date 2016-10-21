package com.mitranetpars.sportmagazine.common;

import com.mitranetpars.sportmagazine.common.dto.security.User;

/**
 * Created by Hamed on 9/16/2016.
 */
public class SecurityEnvironment extends CommonEnvironment {
    private SecurityEnvironment() {
        super();
    }

    static {
        instance = new SecurityEnvironment();
    }

    public static <T extends CommonEnvironment> T getInstance() {
        return (T)instance;
    }

    public void setUserName(String userName){
        this.setEnvironmentVariable("user_name", userName);
    }

    public String getUserName() {
        return (String) this.getEnvironmentVariable("user_name");
    }

    public void setLoginTicket(String ticket) {
        this.setEnvironmentVariable("ticket", ticket);
    }

    public String getLoginTicket(){
        return (String) this.getEnvironmentVariable("ticket");
    }

    public void setUser(User user){
        this.setEnvironmentVariable("user", user);
    }

    public User getUser() {
        return (User) this.getEnvironmentVariable("user");
    }
}
