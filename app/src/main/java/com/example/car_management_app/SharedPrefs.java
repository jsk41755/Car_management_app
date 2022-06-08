package com.example.car_management_app;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {      //CaptainCode가 true일 때, 이전 화면을 안보여주게 하는 기능 (로그인 부분)

    final static String FileName = "CaptainCode";

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

}
