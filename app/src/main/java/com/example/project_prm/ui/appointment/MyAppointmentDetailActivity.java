package com.example.project_prm.ui.appointment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.R;

public class MyAppointmentDetailActivity extends AppCompatActivity {

    private TextView tv_date;
    private TextView tv_time;
    private TextView tv_doctor_name;
    private TextView tv_doctor_specialization;
    private TextView tv_doctor_clinic;
    private TextView tv_patient_name;
    private TextView tv_patient_gender;
    private TextView tv_patient_age;
    private TextView tv_patient_problem;

    private void bindViews() {
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_doctor_name = findViewById(R.id.tv_doctor_name);
        tv_doctor_specialization = findViewById(R.id.tv_doctor_specialization);
        tv_doctor_clinic = findViewById(R.id.tv_doctor_clinic);
        tv_patient_name = findViewById(R.id.tv_patient_name);
        tv_patient_gender = findViewById(R.id.tv_patient_gender);
        tv_patient_age = findViewById(R.id.tv_patient_age);
        tv_patient_problem = findViewById(R.id.tv_patient_problem);
    }

    private void initData() {
        tv_date.setText("Today, December 22, 2022");
        tv_time.setText("16:00 - 16:30 PM (30 minutes)");
        tv_doctor_name.setText("Dr. Drake Boeson");
        tv_doctor_specialization.setText("Immunologists");
        tv_doctor_clinic.setText("The Valley Hospital in California, US");
        tv_patient_name.setText(": Andrew Ainsley");
        tv_patient_gender.setText(": Male");
        tv_patient_age.setText(": 27");
        tv_patient_problem.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment_detail);
        bindViews();
        initData();
    }
}

