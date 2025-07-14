package com.example.project_prm.ui.User_Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageHelper {
    private static final String PREF_NAME = "LanguagePrefs";
    private static final String KEY_LANGUAGE = "app_language";

    public static void setLanguage(Context context, String langCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, langCode).apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, "en"); // Mặc định là tiếng Anh
    }

    public static void applyLanguage(Context context) {
        String langCode = getLanguage(context);
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static Context wrap(Context context) {
        String langCode = getLanguage(context);
        Locale newLocale = new Locale(langCode);
        Locale.setDefault(newLocale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(newLocale);

        return context.createConfigurationContext(config);
    }

}

