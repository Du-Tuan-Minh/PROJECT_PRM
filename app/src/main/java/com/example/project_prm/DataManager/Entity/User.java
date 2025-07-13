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
    public static User fromMap(Map<String, Object> map) {
        if (map == null) return null;
        long id = (map.get("id") instanceof Long) ? (Long) map.get("id") : ((Number) map.get("id")).longValue();
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        String email = (String) map.get("email");
        String phone = (String) map.get("phone");
        Object createdAt = map.get("created_at");

        User user = new User(id, username, password, email, phone);
        user.created_at = createdAt;
        return user;
    }


}