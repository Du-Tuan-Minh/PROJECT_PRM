package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class Appointment {
    public long id;
    public String user_id, clinic_id, doctor_name, specialty, appointment_date, appointment_time, status, notes;
    public Object created_at;

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

    public String getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(String clinic_id) {
        this.clinic_id = clinic_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Object getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Object created_at) {
        this.created_at = created_at;
    }

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

    public static Appointment fromMap(Map<String, Object> map) {
        long id = (long) map.get("id");
        String user_id = (String) map.get("user_id");
        String clinic_id = (String) map.get("clinic_id");
        String doctor_name = (String) map.get("doctor_name");
        String specialty = (String) map.get("specialty");
        String appointment_date = (String) map.get("appointment_date");
        String appointment_time = (String) map.get("appointment_time");
        String status = (String) map.get("status");
        String notes = (String) map.get("notes");
        Object created_at = map.get("created_at");
        return new Appointment(id, user_id, clinic_id, doctor_name, specialty,
                appointment_date, appointment_time, status, notes);
    }
}
