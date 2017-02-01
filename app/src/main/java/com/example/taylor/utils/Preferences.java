package com.example.taylor.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    public static final String DEVICE_ID = "com.example.taylor.DEVICE_ID";
    public static final String RSA_PUBLIC_KEY = "com.example.taylor.RSA_PUBLIC_KEY";
    public static final String RSA_PRIVATE_KEY = "com.example.taylor.RSA_PRIVATE_KEY";
    public static final String CURRENT_SERVER = "com.example.taylor.CURRENT_SERVER";
    public static String CURRENT_SERVER_MESSAGES;
    public static String CURRENT_SERVER_CONNECTIONS;
    public static String RSA_SERVER_PUBLIC_KEY;
    public static String SYMMETRIC_KEY;

    public static SharedPreferences mPreferences;

    public static void init(Context context) {
        mPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    public static void putBoolean(String key, boolean bool) {
        mPreferences.edit().putBoolean(key, bool).commit();
    }

    public static void putString(String key, String s) {
        mPreferences.edit().putString(key, s).commit();
    }

    public static void putInteger(String key, Integer integer) {
        mPreferences.edit().putInt(key, integer).commit();
    }

    public static void setCurrentServer() {
        CURRENT_SERVER_MESSAGES = "com.example.taylor." + Preferences.getString(CURRENT_SERVER)
                + ".MESSAGES";
        CURRENT_SERVER_CONNECTIONS = "com.example.taylor." + Preferences.getString(CURRENT_SERVER)
                + ".CONNECTIONS";
        RSA_SERVER_PUBLIC_KEY = "com.example.taylor." + Preferences.getString(CURRENT_SERVER)
                + ".RSA_SERVER_PUBLIC_KEY";
        SYMMETRIC_KEY = "com.example.taylor." + Preferences.getString(CURRENT_SERVER)
                + ".SYMMETRIC_KEY";
    }

    public static boolean contains(String key) {
        return mPreferences.contains(key);
    }

    public static void clear() {
        mPreferences.edit().clear().commit();
    }

    public static void remove(String key) {
        mPreferences.edit().remove(key).commit();
    }

    public static String getString(String key) {
        return mPreferences.getString(key, null);
    }

    public static int getInteger(String key) {
        return mPreferences.getInt(key, 0);
    }
}