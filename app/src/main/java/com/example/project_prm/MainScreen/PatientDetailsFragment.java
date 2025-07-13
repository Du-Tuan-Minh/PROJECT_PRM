package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.project_prm.R;
import android.widget.TextView;

public class PatientDetailsFragment extends Fragment {
    
    public static PatientDetailsFragment newInstance() {
        return new PatientDetailsFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_details, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize views and setup listeners
        TextInputEditText etFullName = view.findViewById(R.id.etFullName);
        AutoCompleteTextView actvGender = view.findViewById(R.id.actvGender);
        TextInputEditText etAge = view.findViewById(R.id.etAge);
        TextInputEditText etProblem = view.findViewById(R.id.etProblem);
        AutoCompleteTextView actvServiceType = view.findViewById(R.id.actvServiceType);
        MaterialButton btnNext = view.findViewById(R.id.btnNext);
        TextView tvServicePrice = view.findViewById(R.id.tvServicePrice);
        View layoutPrice = view.findViewById(R.id.layoutPrice);

        // Setup dropdown for gender
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, genders);
        actvGender.setAdapter(genderAdapter);
        actvGender.setInputType(0); // Disable keyboard
        actvGender.setKeyListener(null);
        actvGender.setOnClickListener(v -> actvGender.showDropDown());

        // Setup dropdown for service type
        String[] serviceTypes = {"Nhắn tin", "Gọi thoại", "Gọi video"};
        ArrayAdapter<String> serviceTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, serviceTypes);
        actvServiceType.setAdapter(serviceTypeAdapter);
        actvServiceType.setInputType(0);
        actvServiceType.setKeyListener(null);
        actvServiceType.setOnClickListener(v -> actvServiceType.showDropDown());

        actvServiceType.setOnItemClickListener((parent, view1, position, id) -> {
            BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
            if (activity != null) {
                String selected = serviceTypes[position];
                activity.bookingData.packageType = selected;
                // Set dynamic price
                int price = 0;
                switch (selected) {
                    case "Nhắn tin":
                        price = 200000;
                        activity.bookingData.amount = price;
                        activity.bookingData.duration = "15 phút";
                        break;
                    case "Gọi thoại":
                        price = 300000;
                        activity.bookingData.amount = price;
                        activity.bookingData.duration = "30 phút";
                        break;
                    case "Gọi video":
                        price = 500000;
                        activity.bookingData.amount = price;
                        activity.bookingData.duration = "45 phút";
                        break;
                }
                // Hiển thị giá
                if (price > 0) {
                    tvServicePrice.setText(String.format("%,dđ", price));
                    layoutPrice.setVisibility(View.VISIBLE);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
            String gender = actvGender.getText() != null ? actvGender.getText().toString().trim() : "";
            String age = etAge.getText() != null ? etAge.getText().toString().trim() : "";
            String problem = etProblem.getText() != null ? etProblem.getText().toString().trim() : "";
            String serviceType = actvServiceType.getText() != null ? actvServiceType.getText().toString().trim() : "";
            if (fullName.isEmpty() || gender.isEmpty() || age.isEmpty() || problem.isEmpty() || serviceType.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
            if (activity != null) {
                activity.bookingData.packageType = serviceType;
                // Đảm bảo giá đã được set khi chọn gói, nếu chưa thì set mặc định
                if (activity.bookingData.amount == 0) {
                    switch (serviceType) {
                        case "Nhắn tin":
                            activity.bookingData.amount = 200000;
                            activity.bookingData.duration = "15 phút";
                            break;
                        case "Gọi thoại":
                            activity.bookingData.amount = 300000;
                            activity.bookingData.duration = "30 phút";
                            break;
                        case "Gọi video":
                            activity.bookingData.amount = 500000;
                            activity.bookingData.duration = "45 phút";
                            break;
                    }
                }
                activity.onPatientDetailsSubmitted(fullName, gender, age, problem);
            }
        });
    }
} 