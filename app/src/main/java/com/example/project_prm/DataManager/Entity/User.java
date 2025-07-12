package com.example.project_prm.DataManager.Entity;

import com.example.project_prm.DataManager.DatabaseHelper;

import org.json.JSONArray;

import java.util.Date;

public class User {
    private int id;
    private String name;
    private int gender;
    private String dateOfBirth;
    private JSONArray symptomHistory;
    private String email; // Thêm username
    private String password; // Thêm password
    private int role; // Thêm role

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


    public JSONArray getSymptomHistory() {
        return symptomHistory;
    }

    public void setSymptomHistory(JSONArray symptomHistory) {
        this.symptomHistory = symptomHistory;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = DatabaseHelper.hashPassword(password);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public enum GenderEnum {
        Male,
        Female,
        Other;
    }

    public enum RoleEnum {
        Patient,
        Clinic;
    }
}