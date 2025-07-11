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

import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.TungFeaturesService;
import com.example.project_prm.ui.dialog.StatusPopup;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private TungFeaturesService service;

    // Time slots
    private final String[] timeSlots = {
            "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
            "01:00 PM", "01:30 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM",
            "04:00 PM", "04:30 PM", "05:00 PM", "05:30 PM", "06:00 PM", "06:30 PM",
            "07:00 PM", "07:30 PM", "08:00 PM", "08:30 PM", "09:00 PM", "09:30 PM"
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

        service = TungFeaturesService.getInstance(this);
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
        tvTitle.setText("Reschedule Appointment");

        // Show reason selection
        findViewById(R.id.step_1_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.step_2_layout).setVisibility(View.GONE);
        findViewById(R.id.step_3_layout).setVisibility(View.GONE);

        btnNext.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.GONE);
        btnNext.setText("Next");
    }

    private void setupStep2() {
        currentStep = 2;
        tvTitle.setText("Reschedule Appointment");

        // Show date selection
        findViewById(R.id.step_1_layout).setVisibility(View.GONE);
        findViewById(R.id.step_2_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.step_3_layout).setVisibility(View.GONE);

        btnNext.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.GONE);
        btnNext.setText("Next");

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
        tvTitle.setText("Reschedule Appointment");

        // Show time selection
        findViewById(R.id.step_1_layout).setVisibility(View.GONE);
        findViewById(R.id.step_2_layout).setVisibility(View.GONE);
        findViewById(R.id.step_3_layout).setVisibility(View.VISIBLE);

        btnNext.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.VISIBLE);

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
            timeButton.setTextAllCaps(false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, 120, 1.0f
            );
            params.setMargins(0, 0, 8, 0);
            timeButton.setLayoutParams(params);

            // Style
            timeButton.setStrokeWidth(2);
            timeButton.setStrokeColor(getColorStateList(R.color.stroke_color));
            timeButton.setBackgroundTintList(getColorStateList(R.color.white));
            timeButton.setTextColor(getColor(R.color.text_black));
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
                button.setBackgroundTintList(getColorStateList(R.color.white));
                button.setTextColor(getColor(R.color.text_black));
            }
        }

        // Highlight selected button
        selectedButton.setBackgroundTintList(getColorStateList(R.color.primary_blue));
        selectedButton.setTextColor(getColor(R.color.white));
        selectedTime = time;
    }

    private void handleNext() {
        switch (currentStep) {
            case 1:
                // Validate reason selection
                int selectedReasonId = radioGroupReasons.getCheckedRadioButtonId();
                if (selectedReasonId == -1) {
                    Toast.makeText(this, "Please select a reason", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedRadio = findViewById(selectedReasonId);
                selectedReason = selectedRadio.getText().toString();
                setupStep2();
                break;

            case 2:
                // Validate date selection
                if (selectedDate == null) {
                    Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                    return;
                }
                setupStep3();
                break;
        }
    }

    private void handleSubmit() {
        if (selectedTime == null) {
            Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Rescheduling...");

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
                    btnSubmit.setText("Submit");
                    Toast.makeText(RescheduleActivity.this, "Failed to reschedule: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showSuccessDialog() {
        StatusPopup popup = new StatusPopup(this);
        popup.setSuccessPopup(
                "Rescheduling Success!",
                "Appointment successfully changed. You will receive a notification and the doctor will contact you.",
                "View Appointment"
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