package com.example.project_prm.Repository;

import com.example.project_prm.Model.DoctorModel;
import com.example.project_prm.Model.ReviewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorRepository {
    private FirebaseFirestore db;
    private static final String COLLECTION_DOCTORS = "doctors";

    public DoctorRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllDoctors(OnDoctorsLoadedListener listener) {
        db.collection(COLLECTION_DOCTORS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DoctorModel> doctors = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        DoctorModel doctor = document.toObject(DoctorModel.class);
                        if (doctor != null) {
                            doctor.setId(document.getId());
                            doctors.add(doctor);
                        }
                    }
                    listener.onDoctorsLoaded(doctors);
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void searchDoctors(String query, OnDoctorsLoadedListener listener) {
        db.collection(COLLECTION_DOCTORS)
                .whereGreaterThanOrEqualTo("name", query)
                .whereLessThanOrEqualTo("name", query + '\uf8ff')
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DoctorModel> doctors = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        DoctorModel doctor = document.toObject(DoctorModel.class);
                        if (doctor != null) {
                            doctor.setId(document.getId());
                            doctors.add(doctor);
                        }
                    }
                    listener.onDoctorsLoaded(doctors);
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void getDoctorById(String doctorId, OnDoctorLoadedListener listener) {
        db.collection(COLLECTION_DOCTORS)
                .document(doctorId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        DoctorModel doctor = documentSnapshot.toObject(DoctorModel.class);
                        if (doctor != null) {
                            doctor.setId(documentSnapshot.getId());
                            listener.onDoctorLoaded(doctor);
                        } else {
                            listener.onError("Failed to parse doctor data");
                        }
                    } else {
                        listener.onError("Doctor not found");
                    }
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public interface OnDoctorsLoadedListener {
        void onDoctorsLoaded(List<DoctorModel> doctors);
        void onError(String error);
    }

    public interface OnDoctorLoadedListener {
        void onDoctorLoaded(DoctorModel doctor);
        void onError(String error);
    }

    // Static methods for backward compatibility
    public static DoctorRepository getInstance() {
        return new DoctorRepository();
    }

    public List<DoctorModel> getAllDoctors() {
        // For now, return sample data
        List<DoctorModel> doctors = new ArrayList<>();
        doctors.add(new DoctorModel("1", "Dr. John Smith", "Cardiology", "City Hospital", 
                "https://example.com/doctor1.jpg", 4.8, 150, "15 years", "MD", "English, Spanish", 
                "150", "Mon-Fri 9AM-5PM", "Experienced cardiologist", "123-456-7890", 
                "john.smith@hospital.com", "123 Main St", true));
        doctors.add(new DoctorModel("2", "Dr. Sarah Johnson", "Dermatology", "Medical Center", 
                "https://example.com/doctor2.jpg", 4.9, 200, "12 years", "MD", "English", 
                "200", "Mon-Sat 10AM-6PM", "Specialized in skin conditions", "123-456-7891", 
                "sarah.johnson@hospital.com", "456 Oak Ave", true));
        return doctors;
    }

    public DoctorModel getDoctorById(String doctorId) {
        for (DoctorModel doctor : getAllDoctors()) {
            if (doctor.getId().equals(doctorId)) {
                return doctor;
            }
        }
        return null;
    }

    public List<ReviewModel> getDoctorReviews(String doctorId) {
        List<ReviewModel> reviews = new ArrayList<>();
        
        // Sample data - thay thế bằng database call thực tế
        reviews.add(new ReviewModel("1", doctorId, "Nguyễn Thị B", 4.5f, "Bác sĩ rất tận tâm và chuyên nghiệp. Tôi cảm thấy rất hài lòng với dịch vụ.", "15/12/2024"));
        reviews.add(new ReviewModel("2", doctorId, "Trần Văn C", 5.0f, "Khám bệnh rất kỹ càng, giải thích rõ ràng về tình trạng bệnh.", "10/12/2024"));
        reviews.add(new ReviewModel("3", doctorId, "Lê Thị D", 4.0f, "Bác sĩ nhiệt tình, thời gian chờ hợp lý.", "05/12/2024"));
        
        return reviews;
    }
} 