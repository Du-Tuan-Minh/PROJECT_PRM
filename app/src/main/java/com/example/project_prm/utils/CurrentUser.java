package com.example.project_prm.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUser {

    public static void clearOldUserIdInt(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        if (prefs.contains("userId")) {
            try {
                // Try to get as int, if it fails, ignore
                int oldId = prefs.getInt("userId", -1);
                // If it was an int, remove it
                prefs.edit().remove("userId").apply();
            } catch (ClassCastException ignored) {
                // Already a String, do nothing
            }
        }
    }

    public static void login(Context context, String userId) {
        clearOldUserIdInt(context);
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
