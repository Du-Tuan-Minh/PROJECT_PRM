package com.example.project_prm.ui.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.project_prm.R;

public class CancelAppointmentActivity extends AppCompatActivity {

    private static final String TAG = "CancelActivity";
    
    private ImageView ivBack;
    private TextView tvTitle;
    private String appointmentId;
    private AppointmentModel appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_cancel_appointment);
            
            getIntentData();
            initViews();
            setupListeners();
            showReasonFragment();
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

        if (!appointment.canBeCancelled()) {
            Toast.makeText(this, "Không thể hủy lịch hẹn này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Hủy lịch hẹn");
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> {
            try {
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Error finishing: " + e.getMessage());
            }
        });
    }

    private void showReasonFragment() {
        try {
            CancelReasonFragment fragment = new CancelReasonFragment();
            fragment.setOnReasonSelectedListener((reason, otherReason) -> {
                try {
                    String finalReason = "Khác".equals(reason) ? otherReason : reason;
                    processCancellation(finalReason, otherReason);
                } catch (Exception e) {
                    Log.e(TAG, "Error processing reason: " + e.getMessage());
                }
            });

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Error showing reason fragment: " + e.getMessage());
        }
    }

    private void processCancellation(String reason, String otherReason) {
        try {
            // Gửi request hủy lịch hẹn lên server
            AppointmentRepository.getInstance().cancelAppointment(appointmentId);
            
            // Mô phỏng delay
            new android.os.Handler().postDelayed(() -> {
                showSuccessModal();
            }, 1000);
        } catch (Exception e) {
            Log.e(TAG, "Error processing cancellation: " + e.getMessage());
            Toast.makeText(this, "Lỗi hủy lịch: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessModal() {
        try {
            CancelSuccessDialog dialog = new CancelSuccessDialog();
            dialog.setOnActionListener(new CancelSuccessDialog.OnActionListener() {
                @Override
                public void onClose() {
                    try {
                        Intent intent = new Intent(CancelAppointmentActivity.this, AppointmentHistoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error closing: " + e.getMessage());
                        finish();
                    }
                }
            });
            dialog.show(getSupportFragmentManager(), "cancel_success");
        } catch (Exception e) {
            Log.e(TAG, "Error showing success modal: " + e.getMessage());
            finish();
        }
    }
}