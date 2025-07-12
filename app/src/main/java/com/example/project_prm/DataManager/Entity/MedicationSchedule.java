package com.example.project_prm.DataManager.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicationSchedule {
    public long id;
    public String user_id, medicine_name, dosage, frequency, start_date, end_date, meal_relation, notes;
    public List<String> times;
    public boolean is_active;

    public MedicationSchedule(long id, String user_id, String medicine_name, String dosage, String frequency,
                              List<String> times, String start_date, String end_date,
                              String meal_relation, String notes, boolean is_active) {
        this.id = id;
        this.user_id = user_id;
        this.medicine_name = medicine_name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.times = times;
        this.start_date = start_date;
        this.end_date = end_date;
        this.meal_relation = meal_relation;
        this.notes = notes;
        this.is_active = is_active;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", user_id);
        map.put("medicine_name", medicine_name);
        map.put("dosage", dosage);
        map.put("frequency", frequency);
        map.put("times", times);
        map.put("start_date", start_date);
        map.put("end_date", end_date);
        map.put("meal_relation", meal_relation);
        map.put("notes", notes);
        map.put("is_active", is_active);
        return map;
    }
}
