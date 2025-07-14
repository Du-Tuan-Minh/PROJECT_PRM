package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.project_prm.R;
import com.example.project_prm.MainScreen.AppointmentRepository;
import com.example.project_prm.MainScreen.AppointmentModel;

public class RescheduleAppointmentActivity extends AppCompatActivity {

    private static final String TAG = "RescheduleActivity";
    
    private ImageView ivBack;
    private TextView tvTitle;
    private String appointmentId;
    private AppointmentModel appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_reschedule_appointment);
            
            getIntentData();
            initViews();
            setupListeners();
            showDateTimeFragment();
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

        if (!appointment.canBeRescheduled()) {
            Toast.makeText(this, "Không thể đổi lịch hẹn này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Đổi lịch hẹn");
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

    private void showDateTimeFragment() {
        try {
            RescheduleDateTimeFragment fragment = new RescheduleDateTimeFragment();
            fragment.setOnDateTimeSelectedListener((date, time) -> {
                try {
                    processReschedule(date, time);
                } catch (Exception e) {
                    Log.e(TAG, "Error processing reschedule: " + e.getMessage());
                }
            });

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Error showing datetime fragment: " + e.getMessage());
        }
    }

    private void processReschedule(String date, String time) {
        try {
            // Gửi request đổi lịch hẹn lên server
            AppointmentRepository.getInstance().rescheduleAppointment(appointmentId, date, time);
            
            // Mô phỏng delay
            new android.os.Handler().postDelayed(() -> {
                showSuccessModal();
            }, 1000);
        } catch (Exception e) {
            Log.e(TAG, "Error processing reschedule: " + e.getMessage());
            Toast.makeText(this, "Lỗi đổi lịch: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessModal() {
        try {
            RescheduleSuccessDialog dialog = new RescheduleSuccessDialog();
            dialog.setOnActionListener(new RescheduleSuccessDialog.OnActionListener() {
                @Override
                public void onClose() {
                    try {
                        Intent intent = new Intent(RescheduleAppointmentActivity.this, AppointmentHistoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error closing: " + e.getMessage());
                        finish();
                    }
                }
            });
            dialog.show(getSupportFragmentManager(), "reschedule_success");
        } catch (Exception e) {
            Log.e(TAG, "Error showing success modal: " + e.getMessage());
            finish();
        }
    }
}