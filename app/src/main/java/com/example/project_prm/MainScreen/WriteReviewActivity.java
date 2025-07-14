package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_prm.R;
import com.example.project_prm.MainScreen.AppointmentRepository;
import com.example.project_prm.MainScreen.AppointmentModel;
import com.example.project_prm.widgets.EditTextFieldView;
import com.google.android.material.button.MaterialButton;

public class WriteReviewActivity extends AppCompatActivity {

    private static final String TAG = "WriteReviewActivity";
    
    private ImageView ivBack;
    private TextView tvTitle, tvDoctorName, tvDoctorSpecialty;
    private RatingBar ratingBar;
    private EditTextFieldView etReview;
    private MaterialButton btnSubmit;
    private String appointmentId;
    private AppointmentModel appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_write_review);
            
            getIntentData();
            initViews();
            setupListeners();
            displayAppointmentInfo();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getIntentData() {
        appointmentId = getIntent().getStringExtra("appointment_id");
        if (appointmentId == null) {
            finish();
            return;
        }

        appointment = AppointmentRepository.getInstance().getAppointmentById(appointmentId);
        if (appointment == null) {
            Toast.makeText(this, "Không tìm thấy lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!appointment.canBeReviewed()) {
            Toast.makeText(this, "Không thể đánh giá lịch hẹn này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDoctorSpecialty = findViewById(R.id.tvDoctorSpecialty);
        ratingBar = findViewById(R.id.ratingBar);
        etReview = findViewById(R.id.etReview);
        btnSubmit = findViewById(R.id.btnSubmit);
        
        tvTitle.setText("Viết đánh giá");
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> {
            try {
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Error finishing: " + e.getMessage());
            }
        });
        
        btnSubmit.setOnClickListener(v -> {
            try {
                submitReview();
            } catch (Exception e) {
                Log.e(TAG, "Error submitting review: " + e.getMessage());
                Toast.makeText(this, "Lỗi gửi đánh giá: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAppointmentInfo() {
        if (appointment != null) {
            tvDoctorName.setText(appointment.getDoctorName());
            tvDoctorSpecialty.setText(appointment.getSpecialty());
        }
    }

    private void submitReview() {
        float rating = ratingBar.getRating();
        String review = etReview.getFieldText().trim();
        
        if (rating == 0) {
            Toast.makeText(this, "Vui lòng chọn số sao", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (review.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Gửi review lên server
        AppointmentRepository.getInstance().saveReview(appointmentId, (int)rating, review);
        
        // Mô phỏng delay
        new android.os.Handler().postDelayed(() -> {
            showSuccessModal();
        }, 1000);
    }

    private void showSuccessModal() {
        try {
            ReviewSuccessDialog dialog = new ReviewSuccessDialog();
            dialog.setOnActionListener(new ReviewSuccessDialog.OnActionListener() {
                @Override
                public void onClose() {
                    try {
                        Intent intent = new Intent(WriteReviewActivity.this, AppointmentHistoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error closing: " + e.getMessage());
                        finish();
                    }
                }
            });
            dialog.show(getSupportFragmentManager(), "review_success");
        } catch (Exception e) {
            Log.e(TAG, "Error showing success modal: " + e.getMessage());
            finish();
        }
    }
}