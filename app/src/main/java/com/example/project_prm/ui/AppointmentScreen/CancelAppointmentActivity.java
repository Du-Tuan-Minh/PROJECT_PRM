package com.example.project_prm.ui.AppointmentScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.TungFeaturesService;
import com.example.project_prm.ui.dialog.StatusPopup;
import com.google.android.material.button.MaterialButton;

public class CancelAppointmentActivity extends AppCompatActivity {

    // UI Components
    private RadioGroup radioGroupReasons;
    private MaterialButton btnCancel;

    // Data
    private int appointmentId;
    private String selectedReason;
    private TungFeaturesService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointment);

        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        if (appointmentId == -1) {
            Toast.makeText(this, "Invalid appointment", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        service = TungFeaturesService.getInstance(this);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());

        radioGroupReasons = findViewById(R.id.radio_group_reasons);
        btnCancel = findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(v -> handleCancel());
    }

    private void handleCancel() {
        // Validate reason selection
        int selectedReasonId = radioGroupReasons.getCheckedRadioButtonId();
        if (selectedReasonId == -1) {
            Toast.makeText(this, "Please select a reason for cancellation", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadio = findViewById(selectedReasonId);
        selectedReason = selectedRadio.getText().toString();

        // Show loading
        btnCancel.setEnabled(false);
        btnCancel.setText("Cancelling...");

        service.cancelAppointment(appointmentId, selectedReason, new AppointmentHistoryManager.OnActionListener() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    showSuccessDialog();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    btnCancel.setEnabled(true);
                    btnCancel.setText("Yes, Cancel");
                    Toast.makeText(CancelAppointmentActivity.this, "Failed to cancel: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showSuccessDialog() {
        StatusPopup popup = new StatusPopup(this);
        popup.setSuccessPopup(
                "Cancel Appointment Success!",
                "We are very sorry that you have cancelled your doctor's appointment. Only 50% of the funds will be returned to your account.",
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