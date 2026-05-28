package com.example.ytvlc;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private static final String PREF_NAME = "settings";
    private static final String KEY_ENDPOINT = "endpoint";
    private static final String DEFAULT_ENDPOINT = "http://abc.com/endpoint";
    private static final String KEY_VIDEO = "video_codec";
    private static final String DEFAULT_VIDEO = "av1";
    private static final String KEY_AUDIO = "audio_codec";
    private static final String DEFAULT_AUDIO = "opus";

    private static SharedPreferences getPref(Context ctx) {
        return ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getEndpoint(Context ctx) {
        return getPref(ctx).getString(KEY_ENDPOINT, DEFAULT_ENDPOINT);
    }

    public static void setEndpoint(Context ctx, String url) {
        getPref(ctx).edit().putString(KEY_ENDPOINT, url).apply();
    }

    public static String getVideoCodec(Context ctx) {
        return getPref(ctx).getString(KEY_VIDEO, DEFAULT_VIDEO);
    }

    public static void setVideoCodec(Context ctx, String codec) {
        getPref(ctx).edit().putString(KEY_VIDEO, codec).apply();
    }

    public static String getAudioCodec(Context ctx) {
        return getPref(ctx).getString(KEY_AUDIO, DEFAULT_AUDIO);
    }

    public static void setAudioCodec(Context ctx, String codec) {
        getPref(ctx).edit().putString(KEY_AUDIO, codec).apply();
    }
}
