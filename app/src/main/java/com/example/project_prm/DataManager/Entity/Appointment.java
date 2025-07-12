package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class Appointment {
    public long id;
    public String user_id, clinic_id, doctor_name, specialty, appointment_date, appointment_time, status, notes;
    public Object created_at;

    public Appointment(long id, String user_id, String clinic_id, String doctor_name, String specialty,
                       String appointment_date, String appointment_time, String status, String notes) {
        this.id = id;
        this.user_id = user_id;
        this.clinic_id = clinic_id;
        this.doctor_name = doctor_name;
        this.specialty = specialty;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.status = status;
        this.notes = notes;
        this.created_at = FieldValue.serverTimestamp();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", user_id);
        map.put("clinic_id", clinic_id);
        map.put("doctor_name", doctor_name);
        map.put("specialty", specialty);
        map.put("appointment_date", appointment_date);
        map.put("appointment_time", appointment_time);
        map.put("status", status);
        map.put("notes", notes);
        map.put("created_at", created_at);
        return map;
    }
}
