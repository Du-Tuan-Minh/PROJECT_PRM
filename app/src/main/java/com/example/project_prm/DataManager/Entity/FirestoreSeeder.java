package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreSeeder {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void seedAll() {
        addUser();
        addUserProfile();
        addDiseaseCategory();
        addDisease();
        addUserBookmark();
        addClinic();
        addClinicService();
        addClinicReview();
        addAppointment();
        addAppointmentReminder();
        addHealthLog();
        addMedicationSchedule();
        addAIRecommendation();
        addDietPlan();
        addChatSession();
        addChatMessage();
    }

    public static void addUser() {
        Map<String, Object> data = new HashMap<>();
        data.put("username", "john_doe");
        data.put("password", "SHA256_HASHED_PASSWORD");
        data.put("email", "john@example.com");
        data.put("phone", "0123456789");
        data.put("created_at", FieldValue.serverTimestamp());
        db.collection("users").add(data);
    }

    public static void addUserProfile() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "USER_DOC_ID");
        data.put("full_name", "John Doe");
        data.put("date_of_birth", "1990-01-01");
        data.put("gender", "Male");
        data.put("blood_type", "O");
        data.put("height", 175.5);
        data.put("weight", 70.2);
        data.put("allergies", "Peanuts");
        data.put("chronic_diseases", "Diabetes");
        data.put("emergency_contact", "0987654321");
        db.collection("user_profiles").add(data);
    }

    public static void addDiseaseCategory() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Tim mạch");
        data.put("icon_name", "heart_icon");
        data.put("description", "Chuyên khoa tim mạch");
        db.collection("disease_categories").add(data);
    }

    public static void addDisease() {
        Map<String, Object> data = new HashMap<>();
        data.put("category_id", "CATEGORY_DOC_ID");
        data.put("name", "Hypertension");
        data.put("vietnamese_name", "Tăng huyết áp");
        data.put("description", "Bệnh tăng huyết áp");
        data.put("symptoms", "Nhức đầu, chóng mặt");
        data.put("causes", "Căng thẳng, di truyền");
        data.put("treatments", "Thuốc, nghỉ ngơi");
        data.put("prevention", "Ăn nhạt, tập thể dục");
        data.put("when_to_see_doctor", "Khi huyết áp cao kéo dài");
        db.collection("diseases").add(data);
    }

    public static void addUserBookmark() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "USER_DOC_ID");
        data.put("disease_id", "DISEASE_DOC_ID");
        data.put("bookmarked_at", FieldValue.serverTimestamp());
        db.collection("user_bookmarks").add(data);
    }

    public static void addClinic() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Phòng khám ABC");
        data.put("address", "123 HCM");
        data.put("latitude", 10.762622);
        data.put("longitude", 106.660172);
        data.put("phone", "0123456789");
        data.put("email", "clinic@example.com");
        data.put("website", "https://clinic.vn");
        data.put("specialties", "Nội tổng quát");
        data.put("working_hours", "Mon-Fri: 8h-17h");
        data.put("rating", 4.5);
        data.put("total_reviews", 10);
        data.put("image_url", "https://example.com/image.png");
        db.collection("clinics").add(data);
    }

    public static void addClinicService() {
        Map<String, Object> data = new HashMap<>();
        data.put("clinic_id", "CLINIC_DOC_ID");
        data.put("service_name", "Khám tổng quát");
        data.put("price_range", "200-500k");
        data.put("description", "Khám sức khỏe tổng thể");
        db.collection("clinic_services").add(data);
    }

    public static void addClinicReview() {
        Map<String, Object> data = new HashMap<>();
        data.put("clinic_id", "CLINIC_DOC_ID");
        data.put("user_id", "USER_DOC_ID");
        data.put("rating", 5);
        data.put("comment", "Dịch vụ tốt!");
        data.put("created_at", FieldValue.serverTimestamp());
        db.collection("clinic_reviews").add(data);
    }

    public static void addAppointment() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "USER_DOC_ID");
        data.put("clinic_id", "CLINIC_DOC_ID");
        data.put("doctor_name", "BS. Nguyễn Văn A");
        data.put("specialty", "Tim mạch");
        data.put("appointment_date", "2025-07-10");
        data.put("appointment_time", "08:00");
        data.put("status", "scheduled");
        data.put("notes", "Đau ngực");
        data.put("created_at", FieldValue.serverTimestamp());
        db.collection("appointments").add(data);
    }

    public static void addAppointmentReminder() {
        Map<String, Object> data = new HashMap<>();
        data.put("appointment_id", "APPOINTMENT_DOC_ID");
        data.put("reminder_time", FieldValue.serverTimestamp());
        data.put("is_sent", false);
        db.collection("appointment_reminders").add(data);
    }

    public static void addHealthLog() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "USER_DOC_ID");
        data.put("log_date", "2025-07-05");
        data.put("symptoms", "Mệt mỏi");
        data.put("mood", "normal");
        data.put("sleep_hours", 6);
        data.put("water_intake", 1500);
        data.put("exercise_minutes", 30);
        data.put("blood_pressure", "120/80");
        data.put("heart_rate", 75);
        data.put("temperature", 36.5);
        data.put("notes", "Không có gì bất thường");
        data.put("created_at", FieldValue.serverTimestamp());
        db.collection("health_logs").add(data);
    }

    public static void addMedicationSchedule() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "USER_DOC_ID");
        data.put("medicine_name", "Paracetamol");
        data.put("dosage", "500mg");
        data.put("frequency", "2 lần/ngày");
        List<String> times = Arrays.asList("08:00", "20:00");
        data.put("times", times);
        data.put("start_date", "2025-07-05");
        data.put("end_date", "2025-07-10");
        data.put("meal_relation", "after");
        data.put("notes", "Uống sau khi ăn");
        data.put("is_active", true);
        db.collection("medication_schedules").add(data);
    }

    public static void addAIRecommendation() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "USER_DOC_ID");
        data.put("recommendation_date", "2025-07-05");
        data.put("type", "diet");
        data.put("title", "Uống nhiều nước");
        data.put("content", "Nên uống đủ 2 lít nước mỗi ngày");
        data.put("priority", "high");
        data.put("is_read", false);
        data.put("created_at", FieldValue.serverTimestamp());
        db.collection("ai_recommendations").add(data);
    }

    public static void addDietPlan() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "USER_DOC_ID");
        data.put("plan_date", "2025-07-06");
        data.put("breakfast", "Yến mạch + trứng");
        data.put("lunch", "Cơm + cá + rau");
        data.put("dinner", "Salad + soup");
        data.put("snacks", "Hạt điều");
        data.put("foods_to_avoid", "Đồ chiên rán");
        data.put("water_goal", 2000);
        data.put("calorie_goal", 1800);
        data.put("notes", "Tránh đồ ngọt");
        db.collection("diet_plans").add(data);
    }

    public static void addChatSession() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", "USER_DOC_ID");
        data.put("started_at", FieldValue.serverTimestamp());
        data.put("ended_at", null);
        data.put("summary", "");
        db.collection("chat_sessions").add(data);
    }

    public static void addChatMessage() {
        Map<String, Object> data = new HashMap<>();
        data.put("session_id", "SESSION_DOC_ID");
        data.put("is_user_message", true);
        data.put("message", "Xin chào bác sĩ!");
        data.put("timestamp", FieldValue.serverTimestamp());
        db.collection("chat_messages").add(data);
    }
}
