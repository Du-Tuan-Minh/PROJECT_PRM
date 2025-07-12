package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class ChatSession {
    public long id;
    public String user_id;
    public Object started_at;
    public Object ended_at;
    public String summary;

    public ChatSession(long id, String user_id) {
        this.id = id;
        this.user_id = user_id;
        this.started_at = FieldValue.serverTimestamp();
        this.ended_at = null;
        this.summary = "";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", user_id);
        map.put("started_at", started_at);
        map.put("ended_at", ended_at);
        map.put("summary", summary);
        return map;
    }
}
