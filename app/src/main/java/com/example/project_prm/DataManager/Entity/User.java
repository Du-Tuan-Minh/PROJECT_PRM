package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class User {
    public long id;
    public String username, password, email, phone;
    public Object created_at;

    public User(long id, String username, String password, String email, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.created_at = com.google.firebase.firestore.FieldValue.serverTimestamp();
    }

    public java.util.Map<String, Object> toMap() {
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("id", id);
        data.put("username", username);
        data.put("password", password);
        data.put("email", email);
        data.put("phone", phone);
        data.put("created_at", created_at);
        return data;
    }
}