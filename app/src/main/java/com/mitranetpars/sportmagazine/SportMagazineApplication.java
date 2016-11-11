package com.mitranetpars.sportmagazine;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.Locale;

/**
 * Created by Hamed on 9/23/2016.
 */
@ReportsCrashes(formUri = "http://185.94.99.134:5984/acra-sportmagazine/_design/acra-storage/_update/report",
                reportType = org.acra.sender.HttpSender.Type.JSON,
                httpMethod = org.acra.sender.HttpSender.Method.PUT,
                formUriBasicAuthLogin = "simayesalem",
                formUriBasicAuthPassword = "simayesalem",
                mode = ReportingInteractionMode.TOAST,
                resToastText = R.string.crash_toast_text)
public class SportMagazineApplication extends Application {
    private static Context appContext;
    private Locale locale = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        if (locale != null)
        {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public void onCreate(){
        super.onCreate();

        appContext = getApplicationContext();
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = getString(R.string.pref_locale);
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
        {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public static Context getContext(){
        return appContext;
    }

    public static String GetServerUrl(){
        return String.format("http://%s:%s", appContext.getString(R.string.ServerIP), appContext.getString(R.string.ServerPort));
    }
}
