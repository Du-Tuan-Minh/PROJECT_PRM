package com.example.project_prm.DataManager.Entity;

public enum AppLanguage {
    ENGLISH("en", "English (US)"),
    VIETNAMESE("vi", "Tiếng Việt"),
    FRENCH("fr", "Français");

    public final String code;
    public final String displayName;

    AppLanguage(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
}

