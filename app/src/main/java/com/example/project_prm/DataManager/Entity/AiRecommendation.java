package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class AiRecommendation {
    public long id;
    public String user_id, recommendation_date, type, title, content, priority;
    public boolean is_read;
    public Object created_at;

    public AiRecommendation(long id, String user_id, String recommendation_date, String type,
                            String title, String content, String priority, boolean is_read) {
        this.id = id;
        this.user_id = user_id;
        this.recommendation_date = recommendation_date;
        this.type = type;
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.is_read = is_read;
        this.created_at = FieldValue.serverTimestamp();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", user_id);
        map.put("recommendation_date", recommendation_date);
        map.put("type", type);
        map.put("title", title);
        map.put("content", content);
        map.put("priority", priority);
        map.put("is_read", is_read);
        map.put("created_at", created_at);
        return map;
    }
}
