package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Activity cho chức năng đổi lịch hẹn
 * 3 bước: Chọn lý do -> Chọn ngày -> Chọn giờ
 */
public class RescheduleActivity extends AppCompatActivity {

    // UI Components
    private RadioGroup radioGroupReasons;
    private CalendarView calendarView;
    private LinearLayout timeSlotContainer;
    private MaterialButton btnNext, btnSubmit;
    private TextView tvTitle;

    // Data
    private int appointmentId;
    private String selectedReason;
    private String selectedDate;
    private String selectedTime;
    private int currentStep = 1; // 1: Reason, 2: Date, 3: Time
    private HealthcareService service;

    // Time slots
    private final String[] timeSlots = {
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30",
            "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule);

        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        if (appointmentId == -1) {
            Toast.makeText(this, "Invalid appointment", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        service = HealthcareService.getInstance(this);
        initViews();
        setupStep1(); // Start with reason selection
    }

    private void initViews() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());

        tvTitle = findViewById(R.id.tv_title);
        radioGroupReasons = findViewById(R.id.radio_group_reasons);
        calendarView = findViewById(R.id.calendar_view);
        timeSlotContainer = findViewById(R.id.time_slot_container);
        btnNext = findViewById(R.id.btn_next);
        btnSubmit = findViewById(R.id.btn_submit);

        btnNext.setOnClickListener(v -> handleNext());
        btnSubmit.setOnClickListener(v -> handleSubmit());
    }

    private void setupStep1() {
        currentStep = 1;
        tvTitle.setText("Đổi lịch hẹn - Chọn lý do");

        // Show reason selection
        findViewById(R.id.step_1_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.step_2_layout).setVisibility(View.GONE);
        findViewById(R.id.step_3_layout).setVisibility(View.GONE);

        btnNext.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.GONE);
        btnNext.setText("Tiếp theo");
    }

    private void setupStep2() {
        currentStep = 2;
        tvTitle.setText("Đổi lịch hẹn - Chọn ngày");

        // Show date selection
        findViewById(R.id.step_1_layout).setVisibility(View.GONE);
        findViewById(R.id.step_2_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.step_3_layout).setVisibility(View.GONE);

        btnNext.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.GONE);
        btnNext.setText("Tiếp theo");

        // Setup calendar
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1); // Start from tomorrow
        calendarView.setMinDate(calendar.getTimeInMillis());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDate = sdf.format(selectedCalendar.getTime());
        });
    }

    private void setupStep3() {
        currentStep = 3;
        tvTitle.setText("Đổi lịch hẹn - Chọn giờ");

        // Show time selection
        findViewById(R.id.step_1_layout).setVisibility(View.GONE);
        findViewById(R.id.step_2_layout).setVisibility(View.GONE);
        findViewById(R.id.step_3_layout).setVisibility(View.VISIBLE);

        btnNext.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.VISIBLE);
        btnSubmit.setText("Xác nhận đổi lịch");

        setupTimeSlots();
    }

    private void setupTimeSlots() {
        timeSlotContainer.removeAllViews();

        // Create time slot buttons
        LinearLayout currentRow = null;
        for (int i = 0; i < timeSlots.length; i++) {
            if (i % 3 == 0) {
                // Create new row every 3 items
                currentRow = new LinearLayout(this);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                rowParams.setMargins(0, 0, 0, 16);
                currentRow.setLayoutParams(rowParams);
                timeSlotContainer.addView(currentRow);
            }

            MaterialButton timeButton = new MaterialButton(this);
            timeButton.setText(timeSlots[i]);
            // FIXED: Remove setTextAllCaps method

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, 120, 1.0f
            );
            params.setMargins(0, 0, 8, 0);
            timeButton.setLayoutParams(params);

            // Style with safe colors
            timeButton.setStrokeWidth(2);
            try {
                timeButton.setStrokeColor(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
                timeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.white));
                timeButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            } catch (Exception e) {
                // Ignore color setting if resources not available
            }
            timeButton.setCornerRadius(12);

            final String time = timeSlots[i];
            timeButton.setOnClickListener(v -> selectTimeSlot(timeButton, time));

            if (currentRow != null) {
                currentRow.addView(timeButton);
            }
        }
    }

    private void selectTimeSlot(MaterialButton selectedButton, String time) {
        // Reset all buttons
        for (int i = 0; i < timeSlotContainer.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) timeSlotContainer.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                MaterialButton button = (MaterialButton) row.getChildAt(j);
                try {
                    button.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.white));
                    button.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                } catch (Exception e) {
                    // Ignore color setting
                }
            }
        }

        // Highlight selected button
        try {
            selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_blue_bright));
            selectedButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        } catch (Exception e) {
            // Ignore color setting
        }
        selectedTime = time;
    }

    private void handleNext() {
        switch (currentStep) {
            case 1:
                // Validate reason selection
                int selectedReasonId = radioGroupReasons.getCheckedRadioButtonId();
                if (selectedReasonId == -1) {
                    Toast.makeText(this, "Vui lòng chọn lý do", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedRadio = findViewById(selectedReasonId);
                selectedReason = selectedRadio.getText().toString();
                setupStep2();
                break;

            case 2:
                // Validate date selection
                if (selectedDate == null) {
                    Toast.makeText(this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                    return;
                }
                setupStep3();
                break;
        }
    }

    private void handleSubmit() {
        if (selectedTime == null) {
            Toast.makeText(this, "Vui lòng chọn khung giờ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Đang xử lý...");

        service.rescheduleAppointment(appointmentId, selectedDate, selectedTime, new AppointmentHistoryManager.OnActionListener() {
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
                    btnSubmit.setText("Xác nhận đổi lịch");
                    Toast.makeText(RescheduleActivity.this, "Lỗi khi đổi lịch: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showSuccessDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Đổi lịch thành công!")
                .setMessage("Cuộc hẹn đã được đổi thành công. Bạn sẽ nhận được thông báo và bác sĩ sẽ liên hệ với bạn.")
                .setPositiveButton("Xem lịch hẹn", (dialog, which) -> {
                    // Navigate back to appointments
                    Intent intent = new Intent(this, MyAppointmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Đóng", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (currentStep > 1) {
            // Go back to previous step
            switch (currentStep) {
                case 2:
                    setupStep1();
                    break;
                case 3:
                    setupStep2();
                    break;
            }
        } else {
            super.onBackPressed();
        }
    }
}