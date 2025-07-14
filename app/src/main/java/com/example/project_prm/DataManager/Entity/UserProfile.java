package com.example.project_prm.DataManager.Entity;

import java.util.HashMap;
import java.util.Map;

public class UserProfile {
    public long id;
    public String user_id;
    public String full_name;
    public String date_of_birth;
    public String gender;
    public String blood_type;
    public double height;
    public double weight;
    public String allergies;
    public String chronic_diseases;
    public String emergency_contact;

    public UserProfile(long id, String user_id, String full_name, String date_of_birth, String gender, String blood_type,
                       double height, double weight, String allergies, String chronic_diseases, String emergency_contact) {
        this.id = id;
        this.user_id = user_id;
        this.full_name = full_name;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.blood_type = blood_type;
        this.height = height;
        this.weight = weight;
        this.allergies = allergies;
        this.chronic_diseases = chronic_diseases;
        this.emergency_contact = emergency_contact;
    }

    public UserProfile() {

    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", user_id);
        map.put("full_name", full_name);
        map.put("date_of_birth", date_of_birth);
        map.put("gender", gender);
        map.put("blood_type", blood_type);
        map.put("height", height);
        map.put("weight", weight);
        map.put("allergies", allergies);
        map.put("chronic_diseases", chronic_diseases);
        map.put("emergency_contact", emergency_contact);
        return map;
    }
    public static UserProfile fromMap(Map<String, Object> map) {
        UserProfile userProfile = new UserProfile();
        userProfile.setId((long) map.get("id"));
        userProfile.setUser_id((String) map.get("user_id"));
        userProfile.setFull_name((String) map.get("full_name"));
        userProfile.setDate_of_birth((String) map.get("date_of_birth"));
        userProfile.setGender((String) map.get("gender"));
        userProfile.setBlood_type((String) map.get("blood_type"));
        userProfile.setHeight((double) map.get("height"));
        userProfile.setWeight((double) map.get("weight"));
        userProfile.setAllergies((String) map.get("allergies"));
        userProfile.setChronic_diseases((String) map.get("chronic_diseases"));
        userProfile.setEmergency_contact((String) map.get("emergency_contact"));
        return userProfile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getChronic_diseases() {
        return chronic_diseases;
    }

    public void setChronic_diseases(String chronic_diseases) {
        this.chronic_diseases = chronic_diseases;
    }

    public String getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(String emergency_contact) {
        this.emergency_contact = emergency_contact;
    }
}