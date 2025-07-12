package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class HealthLog {
    public long id;
    public String user_id, log_date, symptoms, mood, blood_pressure, notes;
    public int sleep_hours, water_intake, exercise_minutes, heart_rate;
    public double temperature;
    public Object created_at;

    public HealthLog(long id, String user_id, String log_date, String symptoms, String mood, int sleep_hours,
                     int water_intake, int exercise_minutes, String blood_pressure, int heart_rate,
                     double temperature, String notes) {
        this.id = id;
        this.user_id = user_id;
        this.log_date = log_date;
        this.symptoms = symptoms;
        this.mood = mood;
        this.sleep_hours = sleep_hours;
        this.water_intake = water_intake;
        this.exercise_minutes = exercise_minutes;
        this.blood_pressure = blood_pressure;
        this.heart_rate = heart_rate;
        this.temperature = temperature;
        this.notes = notes;
        this.created_at = FieldValue.serverTimestamp();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", user_id);
        map.put("log_date", log_date);
        map.put("symptoms", symptoms);
        map.put("mood", mood);
        map.put("sleep_hours", sleep_hours);
        map.put("water_intake", water_intake);
        map.put("exercise_minutes", exercise_minutes);
        map.put("blood_pressure", blood_pressure);
        map.put("heart_rate", heart_rate);
        map.put("temperature", temperature);
        map.put("notes", notes);
        map.put("created_at", created_at);
        return map;
    }
}
