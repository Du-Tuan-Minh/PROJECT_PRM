package com.example.project_prm.DataManager.Entity;

import java.util.HashMap;
import java.util.Map;

public class DietPlan {
    public long id;
    public String user_id, plan_date, breakfast, lunch, dinner, snacks, foods_to_avoid, notes;
    public int water_goal, calorie_goal;

    public DietPlan(long id, String user_id, String plan_date, String breakfast, String lunch, String dinner,
                    String snacks, String foods_to_avoid, int water_goal, int calorie_goal, String notes) {
        this.id = id;
        this.user_id = user_id;
        this.plan_date = plan_date;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snacks = snacks;
        this.foods_to_avoid = foods_to_avoid;
        this.water_goal = water_goal;
        this.calorie_goal = calorie_goal;
        this.notes = notes;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", user_id);
        map.put("plan_date", plan_date);
        map.put("breakfast", breakfast);
        map.put("lunch", lunch);
        map.put("dinner", dinner);
        map.put("snacks", snacks);
        map.put("foods_to_avoid", foods_to_avoid);
        map.put("water_goal", water_goal);
        map.put("calorie_goal", calorie_goal);
        map.put("notes", notes);
        return map;
    }
}
