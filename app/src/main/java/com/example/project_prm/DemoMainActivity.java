package com.example.project_prm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.UserSessionManager;
import com.example.project_prm.DataManager.Repository.DoctorRepository;
import com.example.project_prm.DataManager.Repository.ClinicRepository;
import com.example.project_prm.Services.TungFeaturesService;
import com.example.project_prm.ui.AppointmentScreen.MyAppointmentActivity;
import com.google.android.material.button.MaterialButton;

public class DemoMainActivity extends AppCompatActivity {

    private UserSessionManager sessionManager;
    private TungFeaturesService service;
    private DoctorRepository doctorRepository;
    private ClinicRepository clinicRepository;

    private MaterialButton btnViewAppointments, btnRefreshData, btnClearData, btnGenerateMore;
    private TextView tvUserInfo, tvDataStats, tvDoctorStats, tvClinicStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_main);

        sessionManager = new UserSessionManager(this);
        service = TungFeaturesService.getInstance(this);
        doctorRepository = DoctorRepository.getInstance();
        clinicRepository = ClinicRepository.getInstance();

        // Create demo session if not logged in
        if (!sessionManager.isLoggedIn()) {
            sessionManager.createDemoSession();
            Toast.makeText(this, "Demo user session created", Toast.LENGTH_SHORT).show();
        }

        initViews();
        setupClickListeners();
        loadStatistics();
    }

    private void initViews() {
        btnViewAppointments = findViewById(R.id.btn_view_appointments);
        btnRefreshData = findViewById(R.id.btn_refresh_data);
        btnClearData = findViewById(R.id.btn_clear_data);
        btnGenerateMore = findViewById(R.id.btn_generate_more);

        tvUserInfo = findViewById(R.id.tv_user_info);
        tvDataStats = findViewById(R.id.tv_data_stats);
        tvDoctorStats = findViewById(R.id.tv_doctor_stats);
        tvClinicStats = findViewById(R.id.tv_clinic_stats);
    }

    private void setupClickListeners() {
        btnViewAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyAppointmentActivity.class);
            startActivity(intent);
        });

        btnRefreshData.setOnClickListener(v -> {
            service.refreshSampleData();
            Toast.makeText(this, "Sample data refreshed!", Toast.LENGTH_SHORT).show();
            loadStatistics(); // Refresh stats
        });

        btnClearData.setOnClickListener(v -> {
            service.clearAllData();
            Toast.makeText(this, "All data cleared!", Toast.LENGTH_SHORT).show();
            loadStatistics(); // Refresh stats
        });

        btnGenerateMore.setOnClickListener(v -> {
            // Generate 5 additional random appointments for testing
            service.generateRandomAppointments(5);
            Toast.makeText(this, "5 additional appointments generated!", Toast.LENGTH_SHORT).show();
            loadStatistics(); // Refresh stats
        });
    }

    private void loadStatistics() {
        // Load user info
        updateUserInfo();

        // Load appointment statistics
        loadAppointmentStats();

        // Load doctor and clinic statistics
        updateDoctorAndClinicStats();
    }

    private void updateUserInfo() {
        String userInfo = "👤 Current User\n" +
                "Name: " + sessionManager.getCurrentUsername() + "\n" +
                "Email: " + sessionManager.getCurrentEmail() + "\n" +
                "User ID: " + sessionManager.getCurrentUserId();

        if (tvUserInfo != null) {
            tvUserInfo.setText(userInfo);
        }
    }

    private void loadAppointmentStats() {
        service.getDatabaseStats(new TungFeaturesService.OnStatsListener() {
            @Override
            public void onSuccess(String stats) {
                runOnUiThread(() -> {
                    if (tvDataStats != null) {
                        tvDataStats.setText("📊 Appointment Statistics\n" + stats);
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    if (tvDataStats != null) {
                        tvDataStats.setText("📊 Appointment Statistics\nError loading stats: " + error);
                    }
                });
            }
        });
    }

    private void updateDoctorAndClinicStats() {
        // Doctor statistics
        int doctorCount = doctorRepository.getAllDoctors().size();
        int topRatedDoctors = 0;
        for (com.example.project_prm.DataManager.Entity.Doctor doctor : doctorRepository.getAllDoctors()) {
            if (doctor.isTopRated()) {
                topRatedDoctors++;
            }
        }

        String doctorStats = "👨‍⚕️ Doctor Database\n" +
                "Total Doctors: " + doctorCount + "\n" +
                "Top Rated (4.7+): " + topRatedDoctors + "\n" +
                "Specialties: Dermatology, Neurology, Cardiology, etc.";

        if (tvDoctorStats != null) {
            tvDoctorStats.setText(doctorStats);
        }

        // Clinic statistics
        int clinicCount = clinicRepository.getAllClinics().size();
        int topRatedClinics = clinicRepository.getTopRatedClinics().size();

        String clinicStats = "🏥 Clinic Database\n" +
                "Total Clinics: " + clinicCount + "\n" +
                "Top Rated (4.5+): " + topRatedClinics + "\n" +
                "Services: General Medicine, Emergency, Specialist Care";

        if (tvClinicStats != null) {
            tvClinicStats.setText(clinicStats);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh statistics when returning to this activity
        loadStatistics();
    }
}