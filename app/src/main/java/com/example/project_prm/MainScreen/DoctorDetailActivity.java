package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class DoctorDetailActivity extends AppCompatActivity {

    private ImageView ivBack, ivDoctorAvatar;
    private TextView tvDoctorName, tvDoctorSpecialty, tvDoctorHospital, tvDoctorExperience;
    private TextView tvDoctorRating, tvDoctorReviewCount;
    private MaterialButton btnBookAppointment;
    private RecyclerView rvReviews;
    private ReviewAdapter reviewAdapter;

    private DoctorModel doctor;
    private List<ReviewModel> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);

        getIntentData();
        initViews();
        setupListeners();
        loadDoctorData();
        loadReviews();
    }

    private void getIntentData() {
        String doctorId = getIntent().getStringExtra("doctor_id");
        if (doctorId != null) {
            doctor = DoctorRepository.getInstance().getDoctorById(doctorId);
        }
        
        if (doctor == null) {
            finish();
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        ivDoctorAvatar = findViewById(R.id.ivDoctorAvatar);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDoctorSpecialty = findViewById(R.id.tvDoctorSpecialty);
        tvDoctorHospital = findViewById(R.id.tvDoctorHospital);
        tvDoctorExperience = findViewById(R.id.tvDoctorExperience);
        tvDoctorRating = findViewById(R.id.tvDoctorRating);
        tvDoctorReviewCount = findViewById(R.id.tvDoctorReviewCount);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
        rvReviews = findViewById(R.id.rvReviews);
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());
        
        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookAppointmentActivity.class);
            intent.putExtra("doctor_id", doctor.getId());
            intent.putExtra("doctor_name", doctor.getName());
            intent.putExtra("doctor_specialty", doctor.getSpecialty());
            intent.putExtra("doctor_hospital", doctor.getHospital());
            startActivity(intent);
        });
    }

    private void loadDoctorData() {
        if (doctor != null) {
            tvDoctorName.setText(doctor.getName());
            tvDoctorSpecialty.setText(doctor.getSpecialty());
            tvDoctorHospital.setText(doctor.getHospital());
            tvDoctorExperience.setText(doctor.getExperience());
            tvDoctorRating.setText(String.format("%.1f", doctor.getRating()));
            tvDoctorReviewCount.setText(String.format("(%d đánh giá)", doctor.getReviewCount()));
        }
    }

    private void loadReviews() {
        reviews = new ArrayList<>();
        // Load sample reviews
        reviews.add(new ReviewModel("Nguyễn Văn A", 5, "Bác sĩ rất tận tâm và chuyên nghiệp!", 0, "2 ngày trước"));
        reviews.add(new ReviewModel("Trần Thị B", 4, "Tư vấn rất chi tiết và dễ hiểu.", 0, "5 ngày trước"));
        reviews.add(new ReviewModel("Lê Văn C", 5, "Bác sĩ có kinh nghiệm cao, rất hài lòng.", 0, "1 tuần trước"));
        
        reviewAdapter = new ReviewAdapter(reviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);
    }
} 