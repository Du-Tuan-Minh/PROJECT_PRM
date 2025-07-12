
package com.example.project_prm.DataManager;

import com.example.project_prm.DataManager.DAO.*;
import com.example.project_prm.DataManager.Entity.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.List;

public class FirestoreSeeder {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void seedAll() {
        WriteBatch batch = db.batch();

        long counter = 1;

        // Seed user
        String userId = db.collection("users").document().getId();
        User user = new User(counter++, "john_doe", "SHA256_HASHED_PASSWORD", "john@example.com", "0123456789");
        new UserDAO().addWithBatch(batch, userId, user);

        // Seed user_profile
        UserProfile profile = new UserProfile(counter++, userId, "John Doe", "1990-01-01", "Male", "O", 175.5, 70.2,
                "Peanuts", "Diabetes", "0987654321");
        new UserProfileDAO().addWithBatch(batch, profile);

        // Seed disease category
        String categoryId = db.collection("disease_categories").document().getId();
        DiseaseCategory category = new DiseaseCategory(counter++, "Tim mạch", "heart_icon", "Chuyên khoa tim mạch");
        new DiseaseCategoryDAO().addWithBatch(batch, categoryId, category);

        // Seed disease
        String diseaseId = db.collection("diseases").document().getId();
        Disease disease = new Disease(counter++, categoryId, "Hypertension", "Tăng huyết áp", "Bệnh tăng huyết áp",
                "Nhức đầu, chóng mặt", "Căng thẳng, di truyền", "Thuốc, nghỉ ngơi", "Ăn nhạt, tập thể dục",
                "Khi huyết áp cao kéo dài");
        new DiseaseDAO().addWithBatch(batch, diseaseId, disease);

        // Seed user bookmark
        UserBookmark bookmark = new UserBookmark(counter++, userId, diseaseId);
        new UserBookmarkDAO().addWithBatch(batch, bookmark);

        // Seed clinic
        String clinicId = db.collection("clinics").document().getId();
        Clinic clinic = new Clinic(counter++, "Phòng khám ABC", "123 HCM", 10.762622, 106.660172, "0123456789",
                "clinic@example.com", "https://clinic.vn", "Nội tổng quát",
                "8h-17h", 4.5, 10, "https://example.com/image.png");
        new ClinicDAO().addWithBatch(batch, clinicId, clinic);

        // Seed clinic service
        ClinicService service = new ClinicService(counter++, clinicId, "Khám tổng quát", "200-500k", "Khám sức khỏe tổng thể");
        new ClinicServiceDAO().addWithBatch(batch, service);

        // Seed clinic review
        ClinicReview review = new ClinicReview(counter++, clinicId, userId, 5, "Dịch vụ tốt!");
        new ClinicReviewDAO().addWithBatch(batch, review);

        // Seed appointment
        String appointmentId = db.collection("appointments").document().getId();
        Appointment appointment = new Appointment(counter++, userId, clinicId, "BS. Nguyễn Văn A", "Tim mạch",
                "2025-07-10", "08:00", "scheduled", "Đau ngực");
        new AppointmentDAO().addWithBatch(batch, appointmentId, appointment);

        // Seed appointment reminder
        AppointmentReminder reminder = new AppointmentReminder(counter++, appointmentId);
        new AppointmentReminderDAO().addWithBatch(batch, reminder);

        // Seed health log
        HealthLog log = new HealthLog(counter++, userId, "2025-07-05", "Mệt mỏi", "normal", 6,
                1500, 30, "120/80", 75, 36.5, "Không có gì bất thường");
        new HealthLogDAO().addWithBatch(batch, log);

        // Seed medication schedule
        List<String> times = Arrays.asList("08:00", "20:00");
        MedicationSchedule medication = new MedicationSchedule(counter++, userId, "Paracetamol", "500mg",
                "2 lần/ngày", times, "2025-07-05", "2025-07-10", "after", "Uống sau khi ăn", true);
        new MedicationScheduleDAO().addWithBatch(batch, medication);

        // Seed AI recommendation
        AiRecommendation recommendation = new AiRecommendation(counter++, userId, "2025-07-05", "diet",
                "Uống nhiều nước", "Nên uống đủ 2 lít nước mỗi ngày", "high", false);
        new AiRecommendationDAO().addWithBatch(batch, recommendation);

        // Seed diet plan
        DietPlan dietPlan = new DietPlan(counter++, userId, "2025-07-06", "Yến mạch + trứng", "Cơm + cá + rau",
                "Salad + soup", "Hạt điều", "Đồ chiên rán", 2000, 1800, "Tránh đồ ngọt");
        new DietPlanDAO().addWithBatch(batch, dietPlan);

        // Seed chat session
        String sessionId = db.collection("chat_sessions").document().getId();
        ChatSession session = new ChatSession(counter++, userId);
        new ChatSessionDAO().addWithBatch(batch, sessionId, session);

        // Seed chat message
        ChatMessage message = new ChatMessage(counter++, sessionId, true, "Xin chào bác sĩ!");
        new ChatMessageDAO().addWithBatch(batch, message);

        // Commit
        batch.commit()
                .addOnSuccessListener(aVoid -> System.out.println("Seeding completed"))
                .addOnFailureListener(e -> System.err.println("Seeding failed: " + e.getMessage()));
    }
}
