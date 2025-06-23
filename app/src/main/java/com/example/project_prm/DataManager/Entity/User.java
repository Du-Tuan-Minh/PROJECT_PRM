package com.example.project_prm.DataManager.Entity;

import org.json.JSONArray;

public class User {
    private int id;
    private String name;
    private String profile;
    private JSONArray symptomHistory;
    private String username; // Thêm username
    private String password; // Thêm password

    public User() {
        this.symptomHistory = new JSONArray();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public JSONArray getSymptomHistory() {
        return symptomHistory;
    }

    public void setSymptomHistory(JSONArray symptomHistory) {
        this.symptomHistory = symptomHistory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}