//package com.example.project_prm.DataManager.Entity;
//
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class FirestoreSeeder {
//    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    public static void seedAll() {
//        addUser();
//        addUserProfile();
//        addDiseaseCategory();
//        addDisease();
//        addUserBookmark();
//        addClinic();
//        addClinicService();
//        addClinicReview();
//        addAppointment();
//        addAppointmentReminder();
//        addHealthLog();
//        addMedicationSchedule();
//        addAIRecommendation();
//        addDietPlan();
//        addChatSession();
//        addChatMessage();
//    }
//
//    public static void addUser() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("username", "john_doe");
//        data.put("password", "SHA256_HASHED_PASSWORD");
//        data.put("email", "john@example.com");
//        data.put("phone", "0123456789");
//        data.put("created_at", FieldValue.serverTimestamp());
//        db.collection("users").add(data);
//    }
//
//    public static void addUserProfile() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("user_id", "USER_DOC_ID");
//        data.put("full_name", "John Doe");
//        data.put("date_of_birth", "1990-01-01");
//        data.put("gender", "Male");
//        data.put("blood_type", "O");
//        data.put("height", 175.5);
//        data.put("weight", 70.2);
//        data.put("allergies", "Peanuts");
//        data.put("chronic_diseases", "Diabetes");
//        data.put("emergency_contact", "0987654321");
//        db.collection("user_profiles").add(data);
//    }
//
//    public static void addDiseaseCategory() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("name", "Tim mạch");
//        data.put("icon_name", "heart_icon");
//        data.put("description", "Chuyên khoa tim mạch");
//        db.collection("disease_categories").add(data);
//    }
//
//    public static void addDisease() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("category_id", "CATEGORY_DOC_ID");
//        data.put("name", "Hypertension");
//        data.put("vietnamese_name", "Tăng huyết áp");
//        data.put("description", "Bệnh tăng huyết áp");
//        data.put("symptoms", "Nhức đầu, chóng mặt");
//        data.put("causes", "Căng thẳng, di truyền");
//        data.put("treatments", "Thuốc, nghỉ ngơi");
//        data.put("prevention", "Ăn nhạt, tập thể dục");
//        data.put("when_to_see_doctor", "Khi huyết áp cao kéo dài");
//        db.collection("diseases").add(data);
//    }
//
//    public static void addUserBookmark() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("user_id", "USER_DOC_ID");
//        data.put("disease_id", "DISEASE_DOC_ID");
//        data.put("bookmarked_at", FieldValue.serverTimestamp());
//        db.collection("user_bookmarks").add(data);
//    }
//
//    public static void addClinic() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("name", "Phòng khám ABC");
//        data.put("address", "123 HCM");
//        data.put("latitude", 10.762622);
//        data.put("longitude", 106.660172);
//        data.put("phone", "0123456789");
//        data.put("email", "clinic@example.com");
//        data.put("website", "https://clinic.vn");
//        data.put("specialties", "Nội tổng quát");
//        data.put("working_hours", "Mon-Fri: 8h-17h");
//        data.put("rating", 4.5);
//        data.put("total_reviews", 10);
//        data.put("image_url", "https://example.com/image.png");
//        db.collection("clinics").add(data);
//    }
//
//    public static void addClinicService() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("clinic_id", "CLINIC_DOC_ID");
//        data.put("service_name", "Khám tổng quát");
//        data.put("price_range", "200-500k");
//        data.put("description", "Khám sức khỏe tổng thể");
//        db.collection("clinic_services").add(data);
//    }
//
//    public static void addClinicReview() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("clinic_id", "CLINIC_DOC_ID");
//        data.put("user_id", "USER_DOC_ID");
//        data.put("rating", 5);
//        data.put("comment", "Dịch vụ tốt!");
//        data.put("created_at", FieldValue.serverTimestamp());
//        db.collection("clinic_reviews").add(data);
//    }
//
//    public static void addAppointment() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("user_id", "USER_DOC_ID");
//        data.put("clinic_id", "CLINIC_DOC_ID");
//        data.put("doctor_name", "BS. Nguyễn Văn A");
//        data.put("specialty", "Tim mạch");
//        data.put("appointment_date", "2025-07-10");
//        data.put("appointment_time", "08:00");
//        data.put("status", "scheduled");
//        data.put("notes", "Đau ngực");
//        data.put("created_at", FieldValue.serverTimestamp());
//        db.collection("appointments").add(data);
//    }
//
//    public static void addAppointmentReminder() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("appointment_id", "APPOINTMENT_DOC_ID");
//        data.put("reminder_time", FieldValue.serverTimestamp());
//        data.put("is_sent", false);
//        db.collection("appointment_reminders").add(data);
//    }
//
//    public static void addHealthLog() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("user_id", "USER_DOC_ID");
//        data.put("log_date", "2025-07-05");
//        data.put("symptoms", "Mệt mỏi");
//        data.put("mood", "normal");
//        data.put("sleep_hours", 6);
//        data.put("water_intake", 1500);
//        data.put("exercise_minutes", 30);
//        data.put("blood_pressure", "120/80");
//        data.put("heart_rate", 75);
//        data.put("temperature", 36.5);
//        data.put("notes", "Không có gì bất thường");
//        data.put("created_at", FieldValue.serverTimestamp());
//        db.collection("health_logs").add(data);
//    }
//
//    public static void addMedicationSchedule() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("user_id", "USER_DOC_ID");
//        data.put("medicine_name", "Paracetamol");
//        data.put("dosage", "500mg");
//        data.put("frequency", "2 lần/ngày");
//        List<String> times = Arrays.asList("08:00", "20:00");
//        data.put("times", times);
//        data.put("start_date", "2025-07-05");
//        data.put("end_date", "2025-07-10");
//        data.put("meal_relation", "after");
//        data.put("notes", "Uống sau khi ăn");
//        data.put("is_active", true);
//        db.collection("medication_schedules").add(data);
//    }
//
//    public static void addAIRecommendation() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("user_id", "USER_DOC_ID");
//        data.put("recommendation_date", "2025-07-05");
//        data.put("type", "diet");
//        data.put("title", "Uống nhiều nước");
//        data.put("content", "Nên uống đủ 2 lít nước mỗi ngày");
//        data.put("priority", "high");
//        data.put("is_read", false);
//        data.put("created_at", FieldValue.serverTimestamp());
//        db.collection("ai_recommendations").add(data);
//    }
//
//    public static void addDietPlan() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("user_id", "USER_DOC_ID");
//        data.put("plan_date", "2025-07-06");
//        data.put("breakfast", "Yến mạch + trứng");
//        data.put("lunch", "Cơm + cá + rau");
//        data.put("dinner", "Salad + soup");
//        data.put("snacks", "Hạt điều");
//        data.put("foods_to_avoid", "Đồ chiên rán");
//        data.put("water_goal", 2000);
//        data.put("calorie_goal", 1800);
//        data.put("notes", "Tránh đồ ngọt");
//        db.collection("diet_plans").add(data);
//    }
//
//    public static void addChatSession() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("user_id", "USER_DOC_ID");
//        data.put("started_at", FieldValue.serverTimestamp());
//        data.put("ended_at", null);
//        data.put("summary", "");
//        db.collection("chat_sessions").add(data);
//    }
//
//    public static void addChatMessage() {
//        Map<String, Object> data = new HashMap<>();
//        data.put("session_id", "SESSION_DOC_ID");
//        data.put("is_user_message", true);
//        data.put("message", "Xin chào bác sĩ!");
//        data.put("timestamp", FieldValue.serverTimestamp());
//        db.collection("chat_messages").add(data);
//    }
//}


package com.example.project_prm.DataManager.Entity;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FirestoreSeeder {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final AtomicLong counter = new AtomicLong(0);

    public static void seedAll() {
        WriteBatch batch = db.batch();

        // Seed users
        String userId = db.collection("users").document().getId();
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", counter.incrementAndGet());
        userData.put("username", "john_doe");
        userData.put("password", "SHA256_HASHED_PASSWORD");
        userData.put("email", "john@example.com");
        userData.put("phone", "0123456789");
        userData.put("created_at", FieldValue.serverTimestamp());
        batch.set(db.collection("users").document(userId), userData);

        // Seed user_profiles
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("id", counter.incrementAndGet());
        profileData.put("user_id", userId);
        profileData.put("full_name", "John Doe");
        profileData.put("date_of_birth", "1990-01-01");
        profileData.put("gender", "Male");
        profileData.put("blood_type", "O");
        profileData.put("height", 175.5);
        profileData.put("weight", 70.2);
        profileData.put("allergies", "Peanuts");
        profileData.put("chronic_diseases", "Diabetes");
        profileData.put("emergency_contact", "0987654321");
        batch.set(db.collection("user_profiles").document(), profileData);

        // Seed disease_categories
        String categoryId = db.collection("disease_categories").document().getId();
        Map<String, Object> categoryData = new HashMap<>();
        categoryData.put("id", counter.incrementAndGet());
        categoryData.put("name", "Tim mạch");
        categoryData.put("icon_name", "heart_icon");
        categoryData.put("description", "Chuyên khoa tim mạch");
        batch.set(db.collection("disease_categories").document(categoryId), categoryData);

        // Seed diseases
        String diseaseId = db.collection("diseases").document().getId();
        Map<String, Object> diseaseData = new HashMap<>();
        diseaseData.put("id", counter.incrementAndGet());
        diseaseData.put("category_id", categoryId);
        diseaseData.put("name", "Hypertension");
        diseaseData.put("vietnamese_name", "Tăng huyết áp");
        diseaseData.put("description", "Bệnh tăng huyết áp");
        diseaseData.put("symptoms", "Nhức đầu, chóng mặt");
        diseaseData.put("causes", "Căng thẳng, di truyền");
        diseaseData.put("treatments", "Thuốc, nghỉ ngơi");
        diseaseData.put("prevention", "Ăn nhạt, tập thể dục");
        diseaseData.put("when_to_see_doctor", "Khi huyết áp cao kéo dài");
        batch.set(db.collection("diseases").document(diseaseId), diseaseData);

        // Seed user_bookmarks
        Map<String, Object> bookmarkData = new HashMap<>();
        bookmarkData.put("id", counter.incrementAndGet());
        bookmarkData.put("user_id", userId);
        bookmarkData.put("disease_id", diseaseId);
        bookmarkData.put("bookmarked_at", FieldValue.serverTimestamp());
        batch.set(db.collection("user_bookmarks").document(), bookmarkData);

        // Seed clinics
        String clinicId = db.collection("clinics").document().getId();
        Map<String, Object> clinicData = new HashMap<>();
        clinicData.put("id", counter.incrementAndGet());
        clinicData.put("name", "Phòng khám ABC");
        clinicData.put("address", "123 HCM");
        clinicData.put("latitude", 10.762622);
        clinicData.put("longitude", 106.660172);
        clinicData.put("phone", "0123456789");
        clinicData.put("email", "clinic@example.com");
        clinicData.put("website", "https://clinic.vn");
        clinicData.put("specialties", "Nội tổng quát");
        Map<String, String> workingHours = new HashMap<>();
        workingHours.put("mon_fri", "8h-17h");
        clinicData.put("working_hours", workingHours);
        clinicData.put("rating", 4.5);
        clinicData.put("total_reviews", 10);
        clinicData.put("image_url", "https://example.com/image.png");
        batch.set(db.collection("clinics").document(clinicId), clinicData);

        // Seed clinic_services
        Map<String, Object> serviceData = new HashMap<>();
        serviceData.put("id", counter.incrementAndGet());
        serviceData.put("clinic_id", clinicId);
        serviceData.put("service_name", "Khám tổng quát");
        serviceData.put("price_range", "200-500k");
        serviceData.put("description", "Khám sức khỏe tổng thể");
        batch.set(db.collection("clinic_services").document(), serviceData);

        // Seed clinic_reviews
        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("id", counter.incrementAndGet());
        reviewData.put("clinic_id", clinicId);
        reviewData.put("user_id", userId);
        reviewData.put("rating", 5);
        reviewData.put("comment", "Dịch vụ tốt!");
        reviewData.put("created_at", FieldValue.serverTimestamp());
        batch.set(db.collection("clinic_reviews").document(), reviewData);

        // Seed appointments
        String appointmentId = db.collection("appointments").document().getId();
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("id", counter.incrementAndGet());
        appointmentData.put("user_id", userId);
        appointmentData.put("clinic_id", clinicId);
        appointmentData.put("doctor_name", "BS. Nguyễn Văn A");
        appointmentData.put("specialty", "Tim mạch");
        appointmentData.put("appointment_date", "2025-07-10");
        appointmentData.put("appointment_time", "08:00");
        appointmentData.put("status", "scheduled");
        appointmentData.put("notes", "Đau ngực");
        appointmentData.put("created_at", FieldValue.serverTimestamp());
        batch.set(db.collection("appointments").document(appointmentId), appointmentData);

        // Seed appointment_reminders
        Map<String, Object> reminderData = new HashMap<>();
        reminderData.put("id", counter.incrementAndGet());
        reminderData.put("appointment_id", appointmentId);
        reminderData.put("reminder_time", FieldValue.serverTimestamp());
        reminderData.put("is_sent", false);
        batch.set(db.collection("appointment_reminders").document(), reminderData);

        // Seed health_logs
        Map<String, Object> healthLogData = new HashMap<>();
        healthLogData.put("id", counter.incrementAndGet());
        healthLogData.put("user_id", userId);
        healthLogData.put("log_date", "2025-07-05");
        healthLogData.put("symptoms", "Mệt mỏi");
        healthLogData.put("mood", "normal");
        healthLogData.put("sleep_hours", 6);
        healthLogData.put("water_intake", 1500);
        healthLogData.put("exercise_minutes", 30);
        healthLogData.put("blood_pressure", "120/80");
        healthLogData.put("heart_rate", 75);
        healthLogData.put("temperature", 36.5);
        healthLogData.put("notes", "Không có gì bất thường");
        healthLogData.put("created_at", FieldValue.serverTimestamp());
        batch.set(db.collection("health_logs").document(), healthLogData);

        // Seed medication_schedules
        Map<String, Object> medicationData = new HashMap<>();
        medicationData.put("id", counter.incrementAndGet());
        medicationData.put("user_id", userId);
        medicationData.put("medicine_name", "Paracetamol");
        medicationData.put("dosage", "500mg");
        medicationData.put("frequency", "2 lần/ngày");
        List<String> times = Arrays.asList("08:00", "20:00");
        medicationData.put("times", times);
        medicationData.put("start_date", "2025-07-05");
        medicationData.put("end_date", "2025-07-10");
        medicationData.put("meal_relation", "after");
        medicationData.put("notes", "Uống sau khi ăn");
        medicationData.put("is_active", true);
        batch.set(db.collection("medication_schedules").document(), medicationData);

        // Seed ai_recommendations
        Map<String, Object> aiRecommendationData = new HashMap<>();
        aiRecommendationData.put("id", counter.incrementAndGet());
        aiRecommendationData.put("user_id", userId);
        aiRecommendationData.put("recommendation_date", "2025-07-05");
        aiRecommendationData.put("type", "diet");
        aiRecommendationData.put("title", "Uống nhiều nước");
        aiRecommendationData.put("content", "Nên uống đủ 2 lít nước mỗi ngày");
        aiRecommendationData.put("priority", "high");
        aiRecommendationData.put("is_read", false);
        aiRecommendationData.put("created_at", FieldValue.serverTimestamp());
        batch.set(db.collection("ai_recommendations").document(), aiRecommendationData);

        // Seed diet_plans
        Map<String, Object> dietPlanData = new HashMap<>();
        dietPlanData.put("id", counter.incrementAndGet());
        dietPlanData.put("user_id", userId);
        dietPlanData.put("plan_date", "2025-07-06");
        dietPlanData.put("breakfast", "Yến mạch + trứng");
        dietPlanData.put("lunch", "Cơm + cá + rau");
        dietPlanData.put("dinner", "Salad + soup");
        dietPlanData.put("snacks", "Hạt điều");
        dietPlanData.put("foods_to_avoid", "Đồ chiên rán");
        dietPlanData.put("water_goal", 2000);
        dietPlanData.put("calorie_goal", 1800);
        dietPlanData.put("notes", "Tránh đồ ngọt");
        batch.set(db.collection("diet_plans").document(), dietPlanData);

        // Seed chat_sessions
        String sessionId = db.collection("chat_sessions").document().getId();
        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("id", counter.incrementAndGet());
        sessionData.put("user_id", userId);
        sessionData.put("started_at", FieldValue.serverTimestamp());
        sessionData.put("ended_at", null);
        sessionData.put("summary", "");
        batch.set(db.collection("chat_sessions").document(sessionId), sessionData);

        // Seed chat_messages
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("id", counter.incrementAndGet());
        messageData.put("session_id", sessionId);
        messageData.put("is_user_message", true);
        messageData.put("message", "Xin chào bác sĩ!");
        messageData.put("timestamp", FieldValue.serverTimestamp());
        batch.set(db.collection("chat_messages").document(), messageData);

        // Commit batch
        batch.commit().addOnSuccessListener(aVoid -> System.out.println("Seeding completed"))
                .addOnFailureListener(e -> System.err.println("Seeding failed: " + e.getMessage()));
    }
}