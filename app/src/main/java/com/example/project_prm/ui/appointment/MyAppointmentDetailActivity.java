package com.example.project_prm.ui.appointment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.DAO.AppointmentDAO;
import com.example.project_prm.DataManager.DAO.ClinicDAO;
import com.example.project_prm.DataManager.DAO.UserDAO;
import com.example.project_prm.DataManager.DAO.UserProfileDAO;
import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.Clinic;
import com.example.project_prm.DataManager.Entity.UserProfile;
import com.example.project_prm.R;

import java.util.Map;
import java.util.Objects;

public class MyAppointmentDetailActivity extends AppCompatActivity {

    private TextView tv_date;
    private TextView tv_time;
    private TextView tv_doctor_name;
    private TextView tv_doctor_specialization;
    private TextView tv_patient_name;
    private TextView tv_patient_gender;
    private TextView tv_patient_age;
    private TextView tv_patient_problem;
    private TextView tv_status;
    private TextView tv_doctor_phone;
    private TextView tv_patient_note;

    private AppointmentDAO appointmentDAO;
    private ClinicDAO clinicDAO;
    private UserProfileDAO userProfileDao;

    private void bindViews() {
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_doctor_name = findViewById(R.id.tv_doctor_name);
        tv_doctor_specialization = findViewById(R.id.tv_doctor_specialization);
        tv_doctor_phone = findViewById(R.id.tv_doctor_phone);
        tv_patient_name = findViewById(R.id.tv_patient_name);
        tv_patient_gender = findViewById(R.id.tv_patient_gender);
        tv_patient_age = findViewById(R.id.tv_patient_age);
        tv_patient_problem = findViewById(R.id.tv_patient_problem);
        tv_status = findViewById(R.id.tv_status);
        tv_patient_note = findViewById(R.id.tv_patient_note);

        appointmentDAO = new AppointmentDAO();
        clinicDAO = new ClinicDAO();
        userProfileDao = new UserProfileDAO();
    }

    @SuppressLint("SetTextI18n")
    private void getAppointmentDetail() {
        String appointmentId = getIntent().getStringExtra("appointmentId");
        if (appointmentId == null) {
            Toast.makeText(this, "Invalid appointment ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        appointmentDAO.getById(appointmentId)
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Appointment appointmentData = Appointment
                                .fromMap(Objects.requireNonNull(
                                        documentSnapshot.getData()));
                        tv_date.setText(appointmentData.getAppointment_date());
                        tv_time.setText(appointmentData.getAppointment_time());
                        tv_status.setText("Status: " + appointmentData.getStatus());
                        tv_doctor_specialization.setText(appointmentData.getSpecialty());
                        tv_patient_problem.setText(appointmentData.getSpecialty());
                        tv_patient_note.setText(appointmentData.getNotes());

                        clinicDAO.getById(appointmentData.getClinic_id())
                                        .addOnSuccessListener(v -> {
                                            Clinic clinicData = Clinic.fromMap(Objects
                                                    .requireNonNull(v.getData()));
                                            tv_doctor_name.setText(clinicData.getName());
                                            tv_doctor_phone.setText(clinicData.getPhone());
                                            tv_doctor_specialization.setText(
                                                    clinicData.getSpecialties());
                                        })
                                .addOnFailureListener(v -> {
                                    Toast.makeText(this, "Failed to fetch clinic details", Toast.LENGTH_SHORT).show();
                                    finish();
                                });

                        userProfileDao.getByUserId(appointmentData.getUser_id())
                                .addOnSuccessListener(v -> {
                                    UserProfile userProfileData = UserProfile
                                            .fromMap(Objects.requireNonNull(v.getData()));
                                    tv_patient_name.setText(userProfileData.getFull_name());
                                    tv_patient_gender.setText(userProfileData.getGender());
                                    tv_patient_age.setText(userProfileData.getDate_of_birth());
                                })
                                .addOnFailureListener(v -> {
                                    Toast.makeText(this, "Failed to fetch user profile details", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch appointment details", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void initData() {
        tv_date.setText("Today, December 22, 2022");
        tv_time.setText("16:00 - 16:30 PM (30 minutes)");
        tv_doctor_name.setText("Dr. Drake Boeson");
        tv_doctor_specialization.setText("Immunologists");
        tv_doctor_phone.setText("The Valley Hospital in California, US");
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
        getAppointmentDetail();
    }
}

