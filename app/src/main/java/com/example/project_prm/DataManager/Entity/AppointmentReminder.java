package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class AppointmentReminder {
    public long id;
    public String appointment_id;
    public Object reminder_time;
    public boolean is_sent;

    public AppointmentReminder(long id, String appointment_id) {
        this.id = id;
        this.appointment_id = appointment_id;
        this.reminder_time = FieldValue.serverTimestamp();
        this.is_sent = false;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("appointment_id", appointment_id);
        map.put("reminder_time", reminder_time);
        map.put("is_sent", is_sent);
        return map;
    }
}
