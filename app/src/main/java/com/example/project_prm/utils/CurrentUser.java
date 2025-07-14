package com.example.project_prm.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUser {

    public static void login(Context context, String userId) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", userId);
        editor.apply();
    }

    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("userId");
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return prefs.getString("userId", null);
    }

    public static boolean isLoggedIn(Context context) {
        return getUserId(context) != null;
    }

}
