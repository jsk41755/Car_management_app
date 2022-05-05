package com.example.car_management_app;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs2 {
    final static String FileName = "CaptainCode";

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref2 = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return sharedPref2.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref2 = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref2.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }
}
