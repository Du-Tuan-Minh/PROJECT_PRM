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
}