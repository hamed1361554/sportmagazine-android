package com.mitranetpars.sportmagazine.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.mitranetpars.sportmagazine.common.SecurityEnvironment;
import com.mitranetpars.sportmagazine.common.dto.security.User;
import com.mitranetpars.sportmagazine.services.SecurityServicesI;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

/**
 * Created by Hamed on 1/10/2017.
 */

public class SystemUtils {
    public static void saveProfile(Context context) {
        try {
            User user = SecurityEnvironment.<SecurityEnvironment>getInstance().getUser();
            String userName = SecurityEnvironment.<SecurityEnvironment>getInstance().getUserName();
            String loginTicket = SecurityEnvironment.<SecurityEnvironment>getInstance().getLoginTicket();

            if (loginTicket == null || loginTicket.isEmpty() ||
                    userName == null || userName.isEmpty()){
                return;
            }

            DB snappydb = DBFactory.open(context); //create or open an existing databse using the default name

            try {
                snappydb.put("userName", userName);
                snappydb.put("loginTicket", loginTicket);

                Gson gson = new Gson();
                snappydb.put("user", gson.toJson(user));
            } finally {
                snappydb.close();
            }

        } catch (Exception e) {
        }
    }

    public static void resetProfile(Context context){
        try {
            DB snappydb = DBFactory.open(context); //create or open an existing databse using the default name

            try {
                snappydb.put("userName", "");
                snappydb.put("loginTicket", "");
                snappydb.put("user", "");
            }
            finally {
                snappydb.close();
            }
        } catch (Exception e){
        }
    }

    public static void loadProfile(Context context){
        try {
            DB snappydb = DBFactory.open(context); //create or open an existing databse using the default name

            try {
                String userName = snappydb.get("userName");
                String loginTicket = snappydb.get("loginTicket");

                if (loginTicket == null || loginTicket.isEmpty() ||
                        userName == null || userName.isEmpty()) {
                    return;
                }

                SecurityEnvironment.<SecurityEnvironment>getInstance().setUserName(userName);
                SecurityEnvironment.<SecurityEnvironment>getInstance().setLoginTicket(loginTicket);

                try {
                    SecurityServicesI.getInstance().getUser(userName);
                } catch (Exception error){
                    resetProfile(context);
                    return;
                }

                Gson gson = new Gson();
                String user = snappydb.get("user");
                SecurityEnvironment.<SecurityEnvironment>getInstance().setUser(gson.fromJson(user, User.class));
            } finally {
                snappydb.close();
            }
        } catch (Exception e) {
        }
    }
}
