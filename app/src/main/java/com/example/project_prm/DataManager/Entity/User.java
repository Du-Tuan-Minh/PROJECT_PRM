package com.example.project_prm.DataManager.Entity;

public class User {
    public String role, email, password, phone;
    public Object created_at;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(String role, String email, String password,  String phone) {
        this.role = role;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.created_at = com.google.firebase.firestore.FieldValue.serverTimestamp();
    }

    public java.util.Map<String, Object> toMap() {
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("role", role);
        data.put("username", email);
        data.put("password", password);
        data.put("email", email);
        data.put("phone", phone);
        data.put("created_at", created_at);
        return data;
    }
    public static User fromMap(java.util.Map<String, Object> data) {
        return new User(
                (String) data.get("role"),
                (String) data.get("username"),
                (String) data.get("email"),
                (String) data.get("phone")
        );
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Object created_at) {
        this.created_at = created_at;
    }

    public enum UserRole {
        Clinic, Personal
    }

}