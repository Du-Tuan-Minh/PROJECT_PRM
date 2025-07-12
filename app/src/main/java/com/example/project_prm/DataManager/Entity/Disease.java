package com.example.project_prm.DataManager.Entity;

import java.util.HashMap;
import java.util.Map;

public class Disease {
    public long id;
    public String category_id;
    public String name;
    public String vietnamese_name;
    public String description;
    public String symptoms;
    public String causes;
    public String treatments;
    public String prevention;
    public String when_to_see_doctor;

    public Disease(long id, String category_id, String name, String vietnamese_name, String description, String symptoms,
                   String causes, String treatments, String prevention, String when_to_see_doctor) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.vietnamese_name = vietnamese_name;
        this.description = description;
        this.symptoms = symptoms;
        this.causes = causes;
        this.treatments = treatments;
        this.prevention = prevention;
        this.when_to_see_doctor = when_to_see_doctor;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("category_id", category_id);
        map.put("name", name);
        map.put("vietnamese_name", vietnamese_name);
        map.put("description", description);
        map.put("symptoms", symptoms);
        map.put("causes", causes);
        map.put("treatments", treatments);
        map.put("prevention", prevention);
        map.put("when_to_see_doctor", when_to_see_doctor);
        return map;
    }
}