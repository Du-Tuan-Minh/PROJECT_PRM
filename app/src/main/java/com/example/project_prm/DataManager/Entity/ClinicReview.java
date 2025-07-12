package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class ClinicReview {
    public long id;
    public String clinic_id;
    public String user_id;
    public int rating;
    public String comment;
    public Object created_at;

    public ClinicReview(long id, String clinic_id, String user_id, int rating, String comment) {
        this.id = id;
        this.clinic_id = clinic_id;
        this.user_id = user_id;
        this.rating = rating;
        this.comment = comment;
        this.created_at = FieldValue.serverTimestamp();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("clinic_id", clinic_id);
        map.put("user_id", user_id);
        map.put("rating", rating);
        map.put("comment", comment);
        map.put("created_at", created_at);
        return map;
    }
}
