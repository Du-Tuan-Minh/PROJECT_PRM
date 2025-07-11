package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.TungFeaturesService;
import com.example.project_prm.ui.dialog.StatusPopup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;

public class ReviewActivity extends AppCompatActivity {

    // UI Components
    private ImageView ivDoctorAvatar;
    private TextView tvDoctorName;
    private RatingBar ratingBar;
    private EditText etReview;
    private MaterialRadioButton radioYes, radioNo;
    private MaterialButton btnSubmit;

    // Data
    private int appointmentId;
    private String doctorName;
    private TungFeaturesService service;
    private int selectedRating = 0;
    private boolean wouldRecommend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        doctorName = getIntent().getStringExtra("doctor_name");

        if (appointmentId == -1) {
            Toast.makeText(this, "Invalid appointment", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        service = TungFeaturesService.getInstance(this);
        initViews();
        setupData();
    }

    private void initViews() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());

        ivDoctorAvatar = findViewById(R.id.iv_doctor_avatar);
        tvDoctorName = findViewById(R.id.tv_doctor_name);
        ratingBar = findViewById(R.id.rating_bar);
        etReview = findViewById(R.id.et_review);
        radioYes = findViewById(R.id.radio_yes);
        radioNo = findViewById(R.id.radio_no);
        btnSubmit = findViewById(R.id.btn_submit);

        // Setup rating bar
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                selectedRating = (int) rating;
            }
        });

        // Setup recommendation radio buttons
        radioYes.setOnClickListener(v -> {
            wouldRecommend = true;
            radioNo.setChecked(false);
        });

        radioNo.setOnClickListener(v -> {
            wouldRecommend = false;
            radioYes.setChecked(false);
        });

        btnSubmit.setOnClickListener(v -> handleSubmitReview());
    }

    private void setupData() {
        if (doctorName != null) {
            tvDoctorName.setText(doctorName);
            TextView tvQuestion = findViewById(R.id.tv_question);
            tvQuestion.setText("Would you recommend " + doctorName + " to your friends?");
        }

        // Set default doctor avatar
        ivDoctorAvatar.setImageResource(R.drawable.default_doctor_avatar);
    }

    private void handleSubmitReview() {
        // Validate rating
        if (selectedRating == 0) {
            Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        String reviewText = etReview.getText().toString().trim();

        // Show loading
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Submitting...");

        // Create review text with recommendation
        String finalReview = reviewText;
        if (radioYes.isChecked() || radioNo.isChecked()) {
            finalReview += "\n\nWould recommend: " + (wouldRecommend ? "Yes" : "No");
        }

        service.addAppointmentFeedback(appointmentId, selectedRating, finalReview, new AppointmentHistoryManager.OnActionListener() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    showSuccessDialog();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("Submit");
                    Toast.makeText(ReviewActivity.this, "Failed to submit review: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showSuccessDialog() {
        StatusPopup popup = new StatusPopup(this);
        popup.setSuccessPopup(
                "Review Successful!",
                "Your review has been successfully submitted, thank you very much!",
                "OK"
        );
        popup.setPrimaryClick(v -> {
            popup.dismiss();
            // Navigate back to appointments
            Intent intent = new Intent(this, MyAppointmentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        popup.show();
    }
}