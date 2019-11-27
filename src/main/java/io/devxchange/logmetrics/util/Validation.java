package io.devxchange.logmetrics.util;

import com.google.gson.Gson;

public class Validation {

    public static boolean isJson(String payload){
            Gson gs = new Gson();
            Object ob = gs.toJson(payload);
            return true;
    }
}

