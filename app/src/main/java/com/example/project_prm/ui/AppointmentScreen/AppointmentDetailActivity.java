package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.AppointmentStatus;
import com.example.project_prm.DataManager.Entity.Doctor;
import com.example.project_prm.DataManager.Repository.DoctorRepository;
import com.example.project_prm.R;
import com.example.project_prm.Services.TungFeaturesService;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppointmentDetailActivity extends AppCompatActivity {

    // UI Components
    private ImageView ivBack, ivDoctorAvatar, ivMore, ivPackageIcon;
    private TextView tvDoctorName, tvSpecialty, tvExperience, tvScheduledAppointment, tvDateTime;
    private TextView tvPatientName, tvPatientPhone, tvPatientAge, tvPatientGender;
    private TextView tvSymptoms, tvPackageName, tvPackagePrice;
    private MaterialButton btnPrimaryAction;

    // Data
    private Appointment appointment;
    private Doctor doctor;
    private TungFeaturesService service;
    private DoctorRepository doctorRepository;
    private int appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        // Get appointment ID from intent
        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        if (appointmentId == -1) {
            Toast.makeText(this, "Invalid appointment", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        service = TungFeaturesService.getInstance(this);
        doctorRepository = DoctorRepository.getInstance();
        initViews();
        loadAppointmentDetails();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivMore = findViewById(R.id.iv_more);
        ivDoctorAvatar = findViewById(R.id.iv_doctor_avatar);
        ivPackageIcon = findViewById(R.id.iv_package_icon);

        tvDoctorName = findViewById(R.id.tv_doctor_name);
        tvSpecialty = findViewById(R.id.tv_specialty);
        tvScheduledAppointment = findViewById(R.id.tv_scheduled_appointment);
        tvDateTime = findViewById(R.id.tv_date_time);

        tvPatientName = findViewById(R.id.tv_patient_name);
        tvPatientPhone = findViewById(R.id.tv_patient_phone);
        tvPatientAge = findViewById(R.id.tv_patient_age);
        tvPatientGender = findViewById(R.id.tv_patient_gender);
        tvSymptoms = findViewById(R.id.tv_symptoms);

        tvPackageName = findViewById(R.id.tv_package_name);
        tvPackagePrice = findViewById(R.id.tv_package_price);

        btnPrimaryAction = findViewById(R.id.btn_primary_action);

        // Set click listeners
        ivBack.setOnClickListener(v -> onBackPressed());
        ivMore.setOnClickListener(v -> showMoreOptions());
        btnPrimaryAction.setOnClickListener(v -> handlePrimaryAction());
    }

    private void loadAppointmentDetails() {
        // Get appointment from database
        service.getAppointmentDetails(appointmentId, new TungFeaturesService.OnAppointmentDetailListener() {
            @Override
            public void onSuccess(Appointment appointment) {
                runOnUiThread(() -> {
                    AppointmentDetailActivity.this.appointment = appointment;

                    // Get doctor details from repository
                    doctor = doctorRepository.getDoctorByName(appointment.getDoctor());

                    displayAppointmentDetails(appointment);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AppointmentDetailActivity.this, "Error loading appointment: " + error, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }

    private void displayAppointmentDetails(Appointment appointment) {
        // Doctor Information
        tvDoctorName.setText(appointment.getDoctor());

        if (doctor != null) {
            // Use real doctor data
            tvSpecialty.setText(doctor.getSpecialty());
            setDoctorAvatar(doctor);

            // Add experience info if available
            TextView tvExperienceView = findViewById(R.id.tv_experience);
            if (tvExperienceView != null) {
                tvExperienceView.setText(doctor.getExperience());
            }
        } else {
            // Fallback to appointment type based specialty
            tvSpecialty.setText(getSpecialtyFromType(appointment.getAppointmentType()));
            ivDoctorAvatar.setImageResource(R.drawable.default_doctor_avatar);
        }

        // Scheduled Appointment
        tvScheduledAppointment.setText("Scheduled Appointment");
        String dateTime = formatDateTime(appointment.getDate(), appointment.getTime());
        tvDateTime.setText(dateTime);

        // Patient Information
        tvPatientName.setText(appointment.getPatientName() != null ? appointment.getPatientName() : "N/A");
        tvPatientPhone.setText(appointment.getPatientPhone() != null ? appointment.getPatientPhone() : "N/A");
        tvPatientAge.setText(appointment.getPatientAge() != null ? appointment.getPatientAge() + " years old" : "N/A");
        tvPatientGender.setText(appointment.getPatientGender() != null ? appointment.getPatientGender() : "N/A");
        tvSymptoms.setText(appointment.getSymptoms() != null ? appointment.getSymptoms() : "No symptoms described");

        // Package Information with real data
        displayPackageInformation(appointment);

        // Configure primary action button based on appointment type and status
        configurePrimaryActionButton(appointment);
    }

    private void setDoctorAvatar(Doctor doctor) {
        String avatarResource = doctor.getAvatarResource();

        if (avatarResource != null) {
            // Try to get resource ID by name
            int resourceId = getResources().getIdentifier(
                    avatarResource, "drawable", getPackageName());

            if (resourceId != 0) {
                ivDoctorAvatar.setImageResource(resourceId);
            } else {
                // Use specialty-based avatar
                setAvatarBySpecialty(doctor.getSpecialtyCode());
            }
        } else {
            setAvatarBySpecialty(doctor.getSpecialtyCode());
        }
    }

    private void setAvatarBySpecialty(String specialtyCode) {
        int avatarResource;
        switch (specialtyCode.toLowerCase()) {
            case "dermatology":
                avatarResource = R.drawable.avatar_dermatologist;
                break;
            case "neurology":
                avatarResource = R.drawable.avatar_neurologist;
                break;
            case "cardiology":
                avatarResource = R.drawable.avatar_cardiologist;
                break;
            case "pediatrics":
                avatarResource = R.drawable.avatar_pediatrician;
                break;
            case "orthopedics":
                avatarResource = R.drawable.avatar_orthopedist;
                break;
            case "gastroenterology":
                avatarResource = R.drawable.avatar_gastroenterologist;
                break;
            default:
                avatarResource = R.drawable.default_doctor_avatar;
                break;
        }
        ivDoctorAvatar.setImageResource(avatarResource);
    }

    private void displayPackageInformation(Appointment appointment) {
        String appointmentType = appointment.getAppointmentType() != null ? appointment.getAppointmentType() : "Messaging";
        tvPackageName.setText(appointmentType);

        // Set package icon based on type
        int iconResource;
        switch (appointmentType.toLowerCase()) {
            case "messaging":
                iconResource = R.drawable.ic_message;
                break;
            case "video call":
                iconResource = R.drawable.ic_video_call;
                break;
            case "voice call":
                iconResource = R.drawable.ic_phone;
                break;
            default:
                iconResource = R.drawable.ic_message;
                break;
        }
        ivPackageIcon.setImageResource(iconResource);

        // Calculate real fee using doctor repository
        double fee;
        if (doctor != null) {
            fee = doctorRepository.getAppointmentFee(doctor.getName(), appointmentType);
        } else {
            fee = appointment.getAppointmentFee();
            if (fee <= 0) {
                fee = getDefaultFee(appointmentType);
            }
        }

        tvPackagePrice.setText("$" + (int)fee);
    }

    private double getDefaultFee(String appointmentType) {
        switch (appointmentType.toLowerCase()) {
            case "messaging":
                return 25.0;
            case "voice call":
                return 35.0;
            case "video call":
                return 45.0;
            default:
                return 30.0;
        }
    }

    private void configurePrimaryActionButton(Appointment appointment) {
        String status = appointment.getStatus();
        String appointmentType = appointment.getAppointmentType();

        if (status != null && status.equals(AppointmentStatus.UPCOMING.getValue())) {
            // For upcoming appointments, show appropriate action based on type
            if (appointmentType != null) {
                switch (appointmentType.toLowerCase()) {
                    case "messaging":
                        btnPrimaryAction.setText("Message (Start at " + appointment.getTime() + ")");
                        btnPrimaryAction.setVisibility(View.VISIBLE);
                        break;
                    case "video call":
                        btnPrimaryAction.setText("Join Video Call (Start at " + appointment.getTime() + ")");
                        btnPrimaryAction.setVisibility(View.VISIBLE);
                        break;
                    case "voice call":
                        btnPrimaryAction.setText("Voice Call (Start at " + appointment.getTime() + ")");
                        btnPrimaryAction.setVisibility(View.VISIBLE);
                        break;
                    default:
                        btnPrimaryAction.setVisibility(View.GONE);
                        break;
                }
            } else {
                btnPrimaryAction.setVisibility(View.GONE);
            }
        } else {
            btnPrimaryAction.setVisibility(View.GONE);
        }
    }

    private void handlePrimaryAction() {
        if (appointment != null) {
            String appointmentType = appointment.getAppointmentType();
            if (appointmentType != null) {
                switch (appointmentType.toLowerCase()) {
                    case "messaging":
                        startMessaging();
                        break;
                    case "video call":
                        startVideoCall();
                        break;
                    case "voice call":
                        startVoiceCall();
                        break;
                }
            }
        }
    }

    private void startMessaging() {
        String doctorName = doctor != null ? doctor.getName() : appointment.getDoctor();
        Toast.makeText(this, "Starting messaging session with " + doctorName + "...", Toast.LENGTH_SHORT).show();
        // TODO: Implement messaging functionality
    }

    private void startVideoCall() {
        String doctorName = doctor != null ? doctor.getName() : appointment.getDoctor();
        Toast.makeText(this, "Starting video call with " + doctorName + "...", Toast.LENGTH_SHORT).show();
        // TODO: Implement video call functionality
    }

    private void startVoiceCall() {
        String doctorName = doctor != null ? doctor.getName() : appointment.getDoctor();
        Toast.makeText(this, "Starting voice call with " + doctorName + "...", Toast.LENGTH_SHORT).show();
        // TODO: Implement voice call functionality
    }

    private void showMoreOptions() {
        // TODO: Implement more options menu
        String options = "Doctor Info:\n";
        if (doctor != null) {
            options += "• " + doctor.getRatingWithReviews() + "\n";
            options += "• " + doctor.getClinic() + "\n";
            options += "• " + doctor.getExperience();
        } else {
            options += "• Basic appointment information";
        }
        Toast.makeText(this, options, Toast.LENGTH_LONG).show();
    }

    private String getSpecialtyFromType(String appointmentType) {
        if (appointmentType == null) return "General Practice";

        switch (appointmentType.toLowerCase()) {
            case "messaging":
                return "Dermatologist";
            case "video call":
                return "Neurologist";
            case "voice call":
                return "Cardiologist";
            case "consultation":
                return "General Consultation";
            case "checkup":
                return "Health Checkup";
            case "followup":
                return "Follow-up";
            case "specialist":
                return "Specialist";
            case "emergency":
                return "Emergency";
            default:
                return "General Practice";
        }
    }

    private String formatDateTime(String date, String time) {
        try {
            // Parse the date
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateObj = inputDateFormat.parse(date);

            // Check if it's today
            SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String today = todayFormat.format(new Date());

            String displayDate;
            if (date.equals(today)) {
                displayDate = "Today";
            } else {
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                displayDate = outputDateFormat.format(dateObj);
            }

            return displayDate + " | " + time;
        } catch (ParseException e) {
            // If parsing fails, return raw date and time
            return date + " | " + time;
        }
    }
}