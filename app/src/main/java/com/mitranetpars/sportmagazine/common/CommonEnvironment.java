package com.mitranetpars.sportmagazine.common;

import java.util.HashMap;

/**
 * Created by Hamed on 9/16/2016.
 */
public class CommonEnvironment {
    protected static CommonEnvironment instance;
    private HashMap<String, Object> environmentDictionary;

    CommonEnvironment(){
        this.environmentDictionary = new HashMap<String, Object>();
    }

    static {
        instance = new CommonEnvironment();
    }

    public static <T extends CommonEnvironment> T getInstance() {
        return (T)instance;
    }

    public void setEnvironmentVariable(String key, Object value){
        this.environmentDictionary.put(key, value);
    }

    public Object getEnvironmentVariable(String key) {
        return this.environmentDictionary.get(key);
    }
}
