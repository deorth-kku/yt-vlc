package com.example.ytvlc;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private static final String PREF_NAME = "settings";
    private static final String KEY_ENDPOINT = "endpoint";
    private static final String DEFAULT_ENDPOINT = "http://abc.com/endpoint";

    private static SharedPreferences getPref(Context ctx) {
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getEndpoint(Context ctx) {
        return getPref(ctx).getString(KEY_ENDPOINT, DEFAULT_ENDPOINT);
    }

    public static void setEndpoint(Context ctx, String url) {
        getPref(ctx).edit().putString(KEY_ENDPOINT, url).apply();
    }
}
