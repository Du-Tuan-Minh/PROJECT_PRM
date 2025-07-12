package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class UserBookmark {
    public long id;
    public String user_id;
    public String disease_id;
    public Object bookmarked_at;

    public UserBookmark(long id, String user_id, String disease_id) {
        this.id = id;
        this.user_id = user_id;
        this.disease_id = disease_id;
        this.bookmarked_at = FieldValue.serverTimestamp();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", user_id);
        map.put("disease_id", disease_id);
        map.put("bookmarked_at", bookmarked_at);
        return map;
    }
}
