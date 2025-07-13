package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.project_prm.R;
import android.widget.Button;
import android.widget.TextView;

public class AppointmentSummaryFragment extends Fragment {
    
    public static AppointmentSummaryFragment newInstance() {
        return new AppointmentSummaryFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_summary, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize views and setup listeners
        Button btnNext = view.findViewById(R.id.btnNext);

        // Lấy bookingData từ activity
        BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
        if (activity != null) {
            BookAppointmentActivity.BookingData data = activity.getBookingData();
            // Doctor info (tĩnh)
            TextView tvDoctorName = view.findViewById(R.id.tvDoctorName);
            TextView tvDoctorSpecialty = view.findViewById(R.id.tvDoctorSpecialty);
            // Schedule info
            TextView tvDate = view.findViewById(R.id.tvDate);
            TextView tvTime = view.findViewById(R.id.tvTime);
            TextView tvPackage = view.findViewById(R.id.tvPackage);
            TextView tvDuration = view.findViewById(R.id.tvDuration);
            TextView tvAmount = view.findViewById(R.id.tvAmount);
            TextView tvDurationPrice = view.findViewById(R.id.tvDurationPrice);
            TextView tvTotal = view.findViewById(R.id.tvTotal);
            // Patient info
            TextView tvPatientName = view.findViewById(R.id.tvPatientName);
            TextView tvPatientGender = view.findViewById(R.id.tvPatientGender);
            TextView tvPatientAge = view.findViewById(R.id.tvPatientAge);
            TextView tvPatientProblem = view.findViewById(R.id.tvPatientProblem);

            // Set doctor info (tĩnh)
            tvDoctorName.setText(data.doctorName);
            tvDoctorSpecialty.setText(data.doctorSpecialty);
            // Set schedule info
            tvDate.setText(data.selectedDate != null ? data.selectedDate : "");
            tvTime.setText(data.selectedTime != null ? data.selectedTime : "");
            tvPackage.setText(data.packageType != null ? data.packageType : "");
            tvDuration.setText(data.duration != null ? data.duration : "");
            String priceStr = String.format("%,dđ", data.amount);
            tvAmount.setText(priceStr);
            tvDurationPrice.setText("1 x " + priceStr);
            tvTotal.setText(priceStr);
            // Set patient info
            tvPatientName.setText(data.fullName != null ? data.fullName : "");
            tvPatientGender.setText(data.gender != null ? data.gender : "");
            tvPatientAge.setText(data.age != null ? data.age : "");
            tvPatientProblem.setText(data.problem != null ? data.problem : "");
        }

        btnNext.setOnClickListener(v -> {
            BookAppointmentActivity activity1 = (BookAppointmentActivity) getActivity();
            if (activity1 != null) {
                activity1.onSummaryConfirmed();
            }
        });
    }
} 