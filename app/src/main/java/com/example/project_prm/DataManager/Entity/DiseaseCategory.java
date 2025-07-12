package com.example.project_prm.DataManager.Entity;

import java.util.HashMap;
import java.util.Map;

public class DiseaseCategory {
    public long id;
    public String name;
    public String icon_name;
    public String description;

    public DiseaseCategory(long id, String name, String icon_name, String description) {
        this.id = id;
        this.name = name;
        this.icon_name = icon_name;
        this.description = description;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("icon_name", icon_name);
        map.put("description", description);
        return map;
    }
}