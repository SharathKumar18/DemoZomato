package com.sample.zomatodemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sample.zomatodemo.application.ZomatoDemoApplication;

public class PreferenceHelper {
    private static PreferenceHelper sPreferenceInstance;
    private static SharedPreferences sSharedPreferences;

    public static PreferenceHelper getInstance() {
        if (sPreferenceInstance == null){
            sSharedPreferences= ZomatoDemoApplication.getContext().getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
            sPreferenceInstance = new PreferenceHelper();
        }
        return sPreferenceInstance;
    }

    public void editPrefString(String name, String value) {
        sSharedPreferences.edit().putString(name, value).apply();
    }

    public void editPrefLong(String name, float value) {
        sSharedPreferences.edit().putFloat(name, value).apply();
    }
    public float getPrefFloat(String name) {
        return sSharedPreferences.getFloat(name, 0);
    }

    public String getPrefString(String name) {
        return sSharedPreferences.getString(name, null);
    }
}
