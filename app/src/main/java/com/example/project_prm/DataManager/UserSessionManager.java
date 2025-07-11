package com.example.project_prm.DataManager;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {

    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserSessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    // Create login session
    public void createLoginSession(int userId, String username, String email) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Get current user ID
    public int getCurrentUserId() {
        return preferences.getInt(KEY_USER_ID, 1); // Default to user ID 1 for demo
    }

    // Get current username
    public String getCurrentUsername() {
        return preferences.getString(KEY_USERNAME, "Demo User");
    }

    // Get current email
    public String getCurrentEmail() {
        return preferences.getString(KEY_EMAIL, "demo@example.com");
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Clear session (logout)
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }

    // Create demo session for testing
    public void createDemoSession() {
        createLoginSession(1, "Demo User", "demo@healthcare.com");
    }
}