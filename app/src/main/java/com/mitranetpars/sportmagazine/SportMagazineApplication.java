package com.mitranetpars.sportmagazine;

import android.app.Application;
import android.content.Context;

/**
 * Created by Hamed on 9/23/2016.
 */
public class SportMagazineApplication extends Application {
    private static Context appContext;

    public void onCreate(){
        super.onCreate();

        appContext = getApplicationContext();
    }

    public static Context getContext(){
        return appContext;
    }

    public static String GetServerUrl(){
        return String.format("http://%s:%s", appContext.getString(R.string.ServerIP), appContext.getString(R.string.ServerPort));
    }
}
