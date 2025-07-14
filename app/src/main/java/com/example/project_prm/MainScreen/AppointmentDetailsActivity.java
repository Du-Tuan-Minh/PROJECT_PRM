package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;

public class AppointmentDetailsActivity extends AppCompatActivity {
    
    private ImageView ivDoctorAvatar, ivPackageIcon;
    private TextView tvDoctorName, tvDoctorSpecialty, tvDoctorLocation;
    private TextView tvAppointmentTime, tvPatientName, tvPatientGender, tvPatientAge, tvPatientProblem;
    private TextView tvPackageType, tvPackagePrice;
    private MaterialButton btnAction, btnReschedule, btnReview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        
        initViews();
        setupToolbar();
        loadAppointmentData();
        setupListeners();
    }
    
    private void initViews() {
        ivDoctorAvatar = findViewById(R.id.ivDoctorAvatar);
        ivPackageIcon = findViewById(R.id.ivPackageIcon);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDoctorSpecialty = findViewById(R.id.tvDoctorSpecialty);
        tvDoctorLocation = findViewById(R.id.tvDoctorLocation);
        tvAppointmentTime = findViewById(R.id.tvAppointmentTime);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvPatientGender = findViewById(R.id.tvPatientGender);
        tvPatientAge = findViewById(R.id.tvPatientAge);
        tvPatientProblem = findViewById(R.id.tvPatientProblem);
        tvPackageType = findViewById(R.id.tvPackageType);
        tvPackagePrice = findViewById(R.id.tvPackagePrice);
        btnAction = findViewById(R.id.btnAction);
        btnReschedule = findViewById(R.id.btnReschedule);
        btnReview = findViewById(R.id.btnReview);
    }
    
    private void setupToolbar() {
        // Toolbar setup removed as it's not in the layout
        setTitle("My Appointment");
    }
    
    private void loadAppointmentData() {
        // Get appointment data from intent
        String doctorName = getIntent().getStringExtra("doctor_name");
        String doctorSpecialty = getIntent().getStringExtra("doctor_specialty");
        String doctorLocation = getIntent().getStringExtra("doctor_location");
        String appointmentDate = getIntent().getStringExtra("appointment_date");
        String appointmentTime = getIntent().getStringExtra("appointment_time");
        String packageType = getIntent().getStringExtra("package_type");
        String packagePrice = getIntent().getStringExtra("package_price");
        String patientName = getIntent().getStringExtra("patient_name");
        String patientGender = getIntent().getStringExtra("patient_gender");
        String patientAge = getIntent().getStringExtra("patient_age");
        String patientProblem = getIntent().getStringExtra("patient_problem");
        
        // Set default values if not provided
        if (doctorName == null) doctorName = "Dr. Drake Boeson";
        if (doctorSpecialty == null) doctorSpecialty = "Immunologists";
        if (doctorLocation == null) doctorLocation = "The Yorkey Hospital in California, US";
        if (appointmentDate == null) appointmentDate = "December 22, 2022";
        if (appointmentTime == null) appointmentTime = "16:00 - 16:30 PM (30 minutes)";
        if (packageType == null) packageType = "Messaging";
        if (packagePrice == null) packagePrice = "$20";
        if (patientName == null) patientName = "Andrew Ainsley";
        if (patientGender == null) patientGender = "Male";
        if (patientAge == null) patientAge = "27";
        if (patientProblem == null) {
            patientProblem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident.";
        }
        
        // Set data to views
        tvDoctorName.setText(doctorName);
        tvDoctorSpecialty.setText(doctorSpecialty);
        tvDoctorLocation.setText(doctorLocation);
        tvAppointmentTime.setText(appointmentTime);
        tvPatientName.setText(patientName);
        tvPatientGender.setText(patientGender);
        tvPatientAge.setText(patientAge);
        tvPatientProblem.setText(patientProblem);
        tvPackageType.setText(packageType);
        tvPackagePrice.setText(packagePrice);
        
        // Load doctor avatar
        Glide.with(this)
            .load(R.drawable.ic_general)
            .circleCrop()
            .into(ivDoctorAvatar);
        
        // Set package icon
        setPackageIcon(packageType);
        
        // Set button text based on appointment time
        btnAction.setText("Message (Start at " + appointmentTime.split(" ")[0] + " PM)");
    }
    
    private void setPackageIcon(String packageType) {
        int iconRes;
        switch (packageType) {
            case "Messaging":
            case "Nhắn tin":
                iconRes = R.drawable.ic_message;
                break;
            case "Voice Call":
            case "Cuộc gọi thoại":
                iconRes = R.drawable.ic_phone;
                break;
            case "Video Call":
            case "Cuộc gọi video":
                iconRes = R.drawable.ic_video;
                break;
            default:
                iconRes = R.drawable.ic_message;
                break;
        }
        ivPackageIcon.setImageResource(iconRes);
    }
    
    private void setupListeners() {
        btnAction.setOnClickListener(v -> {
            // Handle message action
            // This could open chat screen or video call
        });
        
        btnReschedule.setOnClickListener(v -> {
            // Open reschedule activity
            Intent intent = new Intent(this, RescheduleAppointmentActivity.class);
            intent.putExtra("appointment_id", getIntent().getStringExtra("appointment_id"));
            intent.putExtra("doctor_name", tvDoctorName.getText().toString());
            intent.putExtra("current_date", getIntent().getStringExtra("appointment_date"));
            intent.putExtra("current_time", getIntent().getStringExtra("appointment_time"));
            startActivity(intent);
        });
        
        btnReview.setOnClickListener(v -> {
            // Open review activity
            Intent intent = new Intent(this, WriteReviewActivity.class);
            intent.putExtra("appointment_id", getIntent().getStringExtra("appointment_id"));
            intent.putExtra("doctor_name", tvDoctorName.getText().toString());
            startActivity(intent);
        });
        
        // Handle "view more" for patient problem
        findViewById(R.id.tvPatientProblem).setOnClickListener(v -> {
            // Show full problem description in dialog
            showProblemDialog();
        });
    }
    
    private void showProblemDialog() {
        // Show dialog with full problem description
        // This would use the existing dialog_problem_description.xml
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
