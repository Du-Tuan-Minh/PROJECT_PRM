package com.example.project_prm.DataManager.Entity;

import java.util.HashMap;
import java.util.Map;

public class ClinicService {
    public long id;
    public String clinic_id;
    public String service_name, price_range, description;

    public ClinicService(long id, String clinic_id, String service_name, String price_range, String description) {
        this.id = id;
        this.clinic_id = clinic_id;
        this.service_name = service_name;
        this.price_range = price_range;
        this.description = description;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("clinic_id", clinic_id);
        map.put("service_name", service_name);
        map.put("price_range", price_range);
        map.put("description", description);
        return map;
    }
}
