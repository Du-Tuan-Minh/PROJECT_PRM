package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class ChatMessage {
    public long id;
    public String session_id;
    public boolean is_user_message;
    public String message;
    public Object timestamp;

    public ChatMessage(long id, String session_id, boolean is_user_message, String message) {
        this.id = id;
        this.session_id = session_id;
        this.is_user_message = is_user_message;
        this.message = message;
        this.timestamp = FieldValue.serverTimestamp();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("session_id", session_id);
        map.put("is_user_message", is_user_message);
        map.put("message", message);
        map.put("timestamp", timestamp);
        return map;
    }
}
