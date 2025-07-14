package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;

public class AppointmentSummaryFragment extends Fragment {
    
    private TextView tvDoctorName, tvDoctorSpecialty;
    private TextView tvDate, tvTime, tvPackage, tvDuration, tvAmount;
    private TextView tvPatientName, tvPatientGender, tvPatientAge, tvPatientProblem;
    private MaterialButton btnConfirm;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_summary, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupListeners();
        displayBookingData();
    }
    
    private void initViews(View view) {
        tvDoctorName = view.findViewById(R.id.tvDoctorName);
        tvDoctorSpecialty = view.findViewById(R.id.tvDoctorSpecialty);
        tvDate = view.findViewById(R.id.tvDate);
        tvTime = view.findViewById(R.id.tvTime);
        tvPackage = view.findViewById(R.id.tvPackage);
        tvDuration = view.findViewById(R.id.tvDuration);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvPatientName = view.findViewById(R.id.tvPatientName);
        tvPatientGender = view.findViewById(R.id.tvPatientGender);
        tvPatientAge = view.findViewById(R.id.tvPatientAge);
        tvPatientProblem = view.findViewById(R.id.tvPatientProblem);
        btnConfirm = view.findViewById(R.id.btnConfirm);
    }
    
    private void setupListeners() {
        btnConfirm.setOnClickListener(v -> {
            BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
            if (activity != null) {
                activity.onAppointmentConfirmed();
            }
        });
    }
    
    private void displayBookingData() {
        BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
        if (activity != null && activity.bookingData != null) {
            BookAppointmentActivity.BookingData data = activity.bookingData;
            
            tvDoctorName.setText(data.doctorName);
            tvDoctorSpecialty.setText(data.doctorSpecialty);
            tvDate.setText(data.date);
            tvTime.setText(data.time);
            tvPackage.setText(data.packageType);
            tvDuration.setText(data.duration);
            tvAmount.setText(data.amount);
            tvPatientName.setText(data.patientName);
            tvPatientGender.setText(data.patientGender);
            tvPatientAge.setText(data.patientAge);
            tvPatientProblem.setText(data.patientProblem);
        }
    }
} 