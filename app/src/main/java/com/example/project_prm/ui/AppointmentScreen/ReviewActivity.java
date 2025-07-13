package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;

public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = "ReviewActivity";

    // UI Components
    private ImageView ivBack, ivDoctorAvatar;
    private TextView tvDoctorName, tvClinicName, tvAppointmentInfo;
    private RatingBar ratingBar;
    private EditText etReview;
    private MaterialRadioButton radioYes, radioNo;
    private MaterialButton btnSubmit;
    private View progressBar;

    // Data
    private int appointmentId;
    private String doctorName;
    private String clinicName;
    private String specialty;
    private String appointmentDate;
    private String appointmentTime;
    private double fee;

    private int selectedRating = 0;
    private boolean wouldRecommend = false;
    private boolean hasSelectedRecommendation = false;

    // Services
    private HealthcareService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Get intent data
        getIntentData();

        // Validate appointment ID
        if (appointmentId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin cuộc hẹn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize service
        service = HealthcareService.getInstance(this);

        // Initialize UI
        initViews();
        setupData();
        setupListeners();
    }

    private void getIntentData() {
        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        doctorName = getIntent().getStringExtra("doctor_name");
        clinicName = getIntent().getStringExtra("clinic_name");
        specialty = getIntent().getStringExtra("specialty");
        appointmentDate = getIntent().getStringExtra("appointment_date");
        appointmentTime = getIntent().getStringExtra("appointment_time");
        fee = getIntent().getDoubleExtra("fee", 0.0);

        Log.d(TAG, "Intent data - Appointment ID: " + appointmentId +
                ", Doctor: " + doctorName + ", Clinic: " + clinicName);
    }

    private void initViews() {
        // Header
        ivBack = findViewById(R.id.iv_back);

        // Doctor info
        ivDoctorAvatar = findViewById(R.id.iv_doctor_avatar);
        tvDoctorName = findViewById(R.id.tv_doctor_name);
        tvClinicName = findViewById(R.id.tv_clinic_name);
        tvAppointmentInfo = findViewById(R.id.tv_appointment_info);

        // Rating components
        ratingBar = findViewById(R.id.rating_bar);
        etReview = findViewById(R.id.et_review);

        // Recommendation
        radioYes = findViewById(R.id.radio_yes);
        radioNo = findViewById(R.id.radio_no);

        // Actions
        btnSubmit = findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progress_bar);

        // Set white background and black text
        setUIColors();
    }

    private void setUIColors() {
        // Set backgrounds and text colors
        etReview.setBackgroundColor(getColor(android.R.color.white));
        etReview.setTextColor(getColor(android.R.color.black));

        tvDoctorName.setTextColor(getColor(android.R.color.black));
        tvClinicName.setTextColor(getColor(android.R.color.black));
        tvAppointmentInfo.setTextColor(getColor(android.R.color.black));

        radioYes.setTextColor(getColor(android.R.color.black));
        radioNo.setTextColor(getColor(android.R.color.black));
    }

    private void setupData() {
        // Display doctor info
        if (doctorName != null && !doctorName.isEmpty()) {
            tvDoctorName.setText(doctorName);
        } else {
            tvDoctorName.setText("Bác sĩ");
        }

        // Display clinic info
        if (clinicName != null && !clinicName.isEmpty()) {
            tvClinicName.setText("🏥 " + clinicName);
            tvClinicName.setVisibility(View.VISIBLE);
        } else {
            tvClinicName.setVisibility(View.GONE);
        }

        // Display appointment info
        StringBuilder appointmentInfo = new StringBuilder();
        if (appointmentDate != null && appointmentTime != null) {
            appointmentInfo.append("📅 ").append(appointmentDate)
                    .append(" | 🕐 ").append(appointmentTime);
        }
        if (specialty != null && !specialty.isEmpty()) {
            if (appointmentInfo.length() > 0) appointmentInfo.append("\n");
            appointmentInfo.append("⚕️ ").append(specialty);
        }
        if (fee > 0) {
            if (appointmentInfo.length() > 0) appointmentInfo.append("\n");
            appointmentInfo.append("💰 ").append(String.format("%,.0f VNĐ", fee));
        }

        if (appointmentInfo.length() > 0) {
            tvAppointmentInfo.setText(appointmentInfo.toString());
            tvAppointmentInfo.setVisibility(View.VISIBLE);
        } else {
            tvAppointmentInfo.setVisibility(View.GONE);
        }

        // Set default doctor avatar
        ivDoctorAvatar.setImageResource(R.drawable.ic_launcher_foreground); // Use available resource

        // Set initial state
        updateSubmitButton();
    }

    private void setupListeners() {
        // Back button
        ivBack.setOnClickListener(v -> onBackPressed());

        // Rating bar
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                selectedRating = (int) rating;
                Log.d(TAG, "Rating selected: " + selectedRating);
                updateSubmitButton();

                // Show rating feedback
                showRatingFeedback(selectedRating);
            }
        });

        // Recommendation radio buttons
        radioYes.setOnClickListener(v -> {
            wouldRecommend = true;
            hasSelectedRecommendation = true;
            radioNo.setChecked(false);
            updateSubmitButton();
            Log.d(TAG, "Would recommend: Yes");
        });

        radioNo.setOnClickListener(v -> {
            wouldRecommend = false;
            hasSelectedRecommendation = true;
            radioYes.setChecked(false);
            updateSubmitButton();
            Log.d(TAG, "Would recommend: No");
        });

        // Submit button
        btnSubmit.setOnClickListener(v -> handleSubmitReview());
    }

    private void showRatingFeedback(int rating) {
        String feedback;
        switch (rating) {
            case 1:
                feedback = "😞 Rất không hài lòng";
                break;
            case 2:
                feedback = "😐 Không hài lòng";
                break;
            case 3:
                feedback = "😊 Bình thường";
                break;
            case 4:
                feedback = "😄 Hài lòng";
                break;
            case 5:
                feedback = "🤩 Rất hài lòng";
                break;
            default:
                return;
        }

        Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show();
    }

    private void updateSubmitButton() {
        boolean canSubmit = selectedRating > 0;

        btnSubmit.setEnabled(canSubmit);
        if (canSubmit) {
            btnSubmit.setText("Gửi đánh giá");
            btnSubmit.setBackgroundTintList(getColorStateList(android.R.color.holo_blue_dark));
        } else {
            btnSubmit.setText("Vui lòng chọn số sao");
            btnSubmit.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        }
    }

    private void handleSubmitReview() {
        // Validate rating
        if (selectedRating == 0) {
            Toast.makeText(this, "Vui lòng chọn số sao đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }

        String reviewText = etReview.getText().toString().trim();

        // Show loading
        showLoading(true);
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Đang gửi...");

        // Create complete review text
        String finalReview = createFinalReviewText(reviewText);

        // Submit review
        service.addAppointmentFeedback(appointmentId, selectedRating, finalReview,
                new AppointmentHistoryManager.OnActionListener() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            showLoading(false);
                            showSuccessDialog();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            showLoading(false);
                            btnSubmit.setEnabled(true);
                            updateSubmitButton();

                            String errorMsg = "Không thể gửi đánh giá: " + error;
                            Toast.makeText(ReviewActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Review submission error: " + error);
                        });
                    }
                });
    }

    private String createFinalReviewText(String userReview) {
        StringBuilder finalReview = new StringBuilder();

        // Add user review
        if (!userReview.isEmpty()) {
            finalReview.append(userReview);
        }

        // Add recommendation
        if (hasSelectedRecommendation) {
            if (finalReview.length() > 0) {
                finalReview.append("\n\n");
            }
            finalReview.append("Giới thiệu cho bạn bè: ")
                    .append(wouldRecommend ? "Có" : "Không");
        }

        return finalReview.toString();
    }

    private void showSuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(this);

        String message = String.format(
                "✅ Cảm ơn bạn đã đánh giá!\n\n" +
                        "⭐ Số sao: %d/5\n" +
                        "👨‍⚕️ Bác sĩ: %s\n" +
                        "🏥 Phòng khám: %s\n\n" +
                        "Đánh giá của bạn sẽ giúp cải thiện chất lượng dịch vụ.",
                selectedRating,
                doctorName != null ? doctorName : "Bác sĩ",
                clinicName != null ? clinicName : "Phòng khám"
        );

        builder.setTitle("Đánh giá thành công!")
                .setMessage(message)
                .setPositiveButton("Xem lịch hẹn", (dialog, which) -> {
                    navigateToAppointments();
                })
                .setNegativeButton("Đóng", (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void navigateToAppointments() {
        Intent intent = new Intent(this, MyAppointmentActivity.class);
        intent.putExtra("show_tab", 1); // Show completed appointments tab
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (selectedRating > 0 || !etReview.getText().toString().trim().isEmpty()) {
            // Show confirmation dialog if user has entered data
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc muốn thoát mà không gửi đánh giá?")
                    .setPositiveButton("Thoát", (dialog, which) -> super.onBackPressed())
                    .setNegativeButton("Tiếp tục đánh giá", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }
}