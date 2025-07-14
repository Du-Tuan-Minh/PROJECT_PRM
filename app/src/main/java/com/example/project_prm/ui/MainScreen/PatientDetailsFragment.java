package com.example.project_prm.ui.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.project_prm.R;

public class PatientDetailsFragment extends Fragment {
    private EditText etFullName, etAge, etPhone, etProblem;
    private Spinner spGender, spPackage;
    private Button btnNext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_details, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupSpinners();
        setupListeners();
    }
    private void initViews(View view) {
        etFullName = view.findViewById(R.id.etFullName);
        etAge = view.findViewById(R.id.etAge);
        etPhone = view.findViewById(R.id.etPhone);
        etProblem = view.findViewById(R.id.etProblem);
        spGender = view.findViewById(R.id.spGender);
        spPackage = view.findViewById(R.id.spPackage);
        btnNext = view.findViewById(R.id.btnNext);
    }
    private void setupSpinners() {
        String[] genders = {"Chọn giới tính", "Nam", "Nữ", "Khác"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
            getContext(),
            android.R.layout.simple_spinner_item, 
            genders
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);
        String[] packages = {
            "Chọn gói khám",
            "Nhắn tin - 200,000đ (30 phút)",
            "Gọi thoại - 300,000đ (30 phút)", 
            "Gọi video - 500,000đ (45 phút)",
            "Khám trực tiếp - 800,000đ (60 phút)",
            "Khám tổng quát - 1,200,000đ (90 phút)",
            "Tư vấn chuyên sâu - 1,500,000đ (120 phút)"
        };
        ArrayAdapter<String> packageAdapter = new ArrayAdapter<>(
            getContext(),
            android.R.layout.simple_spinner_item,
            packages
        );
        packageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPackage.setAdapter(packageAdapter);
    }
    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            if (validateForm()) {
                String fullName = etFullName.getText().toString().trim();
                String gender = spGender.getSelectedItem().toString();
                String age = etAge.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String problem = etProblem.getText().toString().trim();
                String selectedPackage = spPackage.getSelectedItem().toString();
                String[] packageInfo = parsePackageInfo(selectedPackage);
                String packageType = packageInfo[0];
                String packagePrice = packageInfo[1];
                String packageDuration = packageInfo[2];
                BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
                if (activity != null) {
                    activity.onPatientDetailsEntered(fullName, gender, age, phone, problem, packageType, packageDuration, packagePrice);
                }
            }
        });
    }
    private String[] parsePackageInfo(String selectedPackage) {
        String packageType, packagePrice, packageDuration;
        if (selectedPackage.contains("Nhắn tin")) {
            packageType = "Nhắn tin";
            packagePrice = "200,000đ";
            packageDuration = "30 phút";
        } else if (selectedPackage.contains("Gọi thoại")) {
            packageType = "Gọi thoại";
            packagePrice = "300,000đ";
            packageDuration = "30 phút";
        } else if (selectedPackage.contains("Gọi video")) {
            packageType = "Gọi video";
            packagePrice = "500,000đ";
            packageDuration = "45 phút";
        } else if (selectedPackage.contains("Khám trực tiếp")) {
            packageType = "Khám trực tiếp";
            packagePrice = "800,000đ";
            packageDuration = "60 phút";
        } else if (selectedPackage.contains("Khám tổng quát")) {
            packageType = "Khám tổng quát";
            packagePrice = "1,200,000đ";
            packageDuration = "90 phút";
        } else if (selectedPackage.contains("Tư vấn chuyên sâu")) {
            packageType = "Tư vấn chuyên sâu";
            packagePrice = "1,500,000đ";
            packageDuration = "120 phút";
        } else {
            packageType = "Chưa chọn";
            packagePrice = "0đ";
            packageDuration = "0 phút";
        }
        return new String[]{packageType, packagePrice, packageDuration};
    }
    private boolean validateForm() {
        if (etFullName.getText().toString().trim().isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return false;
        }
        if (spGender.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etAge.getText().toString().trim().isEmpty()) {
            etAge.setError("Vui lòng nhập tuổi");
            etAge.requestFocus();
            return false;
        }
        if (etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            etPhone.requestFocus();
            return false;
        }
        if (spPackage.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Vui lòng chọn gói khám", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etProblem.getText().toString().trim().isEmpty()) {
            etProblem.setError("Vui lòng mô tả vấn đề");
            etProblem.requestFocus();
            return false;
        }
        return true;
    }
} 