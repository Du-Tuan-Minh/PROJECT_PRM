package com.example.project_prm.ui.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.project_prm.R;
import java.util.ArrayList;
import java.util.List;

public class DoctorProfileActivity extends AppCompatActivity {

    private ImageView ivBack, ivSearch, ivMore, ivDoctorAvatar;
    private TextView tvTitle, tvDoctorName, tvDoctorSpecialty, tvDoctorLocation;
    private TextView tvPatients, tvExperience, tvRating, tvReviews;
    private TextView tvAboutDoctor, tvWorkingTime, tvSeeAllReviews;
    private ChipGroup cgRatingFilter;
    private RecyclerView rvReviews;
    private MaterialButton btnBookAppointment;

    private ReviewAdapter reviewAdapter;
    private List<ReviewModel> reviews;
    private int selectedRating = 0; // 0 = All

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        initViews();
        setupListeners();
        loadDoctorData();
        setupReviews();
    }

    private void initViews() {
        // Header
        ivBack = findViewById(R.id.ivBack);
        ivSearch = findViewById(R.id.ivSearch);
        ivMore = findViewById(R.id.ivMore);
        tvTitle = findViewById(R.id.tvTitle);

        // Doctor info
        ivDoctorAvatar = findViewById(R.id.ivDoctorAvatar);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDoctorSpecialty = findViewById(R.id.tvDoctorSpecialty);
        tvDoctorLocation = findViewById(R.id.tvDoctorLocation);

        // Stats
        tvPatients = findViewById(R.id.tvPatients);
        tvExperience = findViewById(R.id.tvExperience);
        tvRating = findViewById(R.id.tvRating);
        tvReviews = findViewById(R.id.tvReviews);

        // About section
        // tvAboutDoctor = findViewById(R.id.tvAboutDoctor);
        // tvWorkingTime = findViewById(R.id.tvWorkingTime);

        // Reviews section
        // tvSeeAllReviews = findViewById(R.id.tvSeeAllReviews);
        // cgRatingFilter = findViewById(R.id.cgRatingFilter);
        // rvReviews = findViewById(R.id.rvReviews);

        // Book appointment button
        btnBookAppointment = findViewById(R.id.btnBookAppointment);

        tvTitle.setText("BS. Jenny Watson");
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        ivSearch.setOnClickListener(v -> {
            // Mở tìm kiếm
        });

        ivMore.setOnClickListener(v -> {
            // Hiển thị menu thêm
        });

        tvSeeAllReviews.setOnClickListener(v -> {
            // Mở trang xem tất cả đánh giá
        });

        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookAppointmentActivity.class);
            intent.putExtra("doctor_id", "doctor_jenny");
            intent.putExtra("doctor_name", "BS. Jenny Watson");
            startActivity(intent);
        });

        setupRatingFilter();
    }

    private void setupRatingFilter() {
        String[] ratings = {"Tất cả", "5⭐", "4⭐", "3⭐", "2⭐", "1⭐"};

        for (int i = 0; i < ratings.length; i++) {
            Chip chip = new Chip(this);
            chip.setText(ratings[i]);
            chip.setCheckable(true);

            if (i == 0) {
                chip.setChecked(true);
                chip.setChipBackgroundColorResource(R.color.primary_blue);
                chip.setTextColor(getResources().getColor(R.color.white));
            } else {
                chip.setChipBackgroundColorResource(R.color.input_bg);
                chip.setTextColor(getResources().getColor(R.color.text_gray));
            }

            final int rating = i;
            chip.setOnClickListener(v -> {
                selectedRating = rating;
                updateRatingFilter();
                filterReviews();
            });

            cgRatingFilter.addView(chip);
        }
    }

    private void updateRatingFilter() {
        for (int i = 0; i < cgRatingFilter.getChildCount(); i++) {
            Chip chip = (Chip) cgRatingFilter.getChildAt(i);
            if (i == selectedRating) {
                chip.setChecked(true);
                chip.setChipBackgroundColorResource(R.color.primary_blue);
                chip.setTextColor(getResources().getColor(R.color.white));
            } else {
                chip.setChecked(false);
                chip.setChipBackgroundColorResource(R.color.input_bg);
                chip.setTextColor(getResources().getColor(R.color.text_gray));
            }
        }
    }

    private void loadDoctorData() {
        // Load doctor info
        tvDoctorName.setText("BS. Jenny Watson");
        tvDoctorSpecialty.setText("Chuyên khoa Miễn dịch");
        tvDoctorLocation.setText("Bệnh viện Christ, London, UK");

        // Load stats
        tvPatients.setText("5.000+");
        tvExperience.setText("10+");
        tvRating.setText("4.8");
        tvReviews.setText("498");

        // Load about
        tvAboutDoctor.setText("BS. Jenny Watson là chuyên gia hàng đầu về miễn dịch học tại Bệnh viện Christ ở London. Bà đã đạt được nhiều giải thưởng cho những đóng góp tuyệt vời trong lĩnh vực y tế. Bà có sẵn để tư vấn riêng. xem thêm");

        tvWorkingTime.setText("Thứ Hai - Thứ Sáu, 08:30 - 20:00");

        // Load avatar
        Glide.with(this)
                .load(R.drawable.ic_general)
                .circleCrop()
                .into(ivDoctorAvatar);
    }

    private void setupReviews() {
        reviews = new ArrayList<>();

        // Sample reviews
        reviews.add(new ReviewModel(
                "Charlotte Hanlin",
                5,
                "BS. Jenny rất chuyên nghiệp trong công việc và phản hồi nhanh. Tôi đã tư vấn và vấn đề của tôi đã được giải quyết. 🥰",
                198,
                "4 ngày trước"
        ));

        reviews.add(new ReviewModel(
                "Darron Kulikowski",
                4,
                "BS. Jenny rất chuyên nghiệp và dịch vụ xuất sắc! Tôi thích và muốn tư vấn lại 👍",
                863,
                "6 ngày trước"
        ));

        reviews.add(new ReviewModel(
                "Laurallee Quintero",
                5,
                "Bác sĩ rất khéo léo và nhanh chóng trong dịch vụ. Tôi đặc biệt giới thiệu BS. Jenny cho tất cả những ai muốn tư vấn 👍👍",
                629,
                "8 ngày trước"
        ));

        reviews.add(new ReviewModel(
                "Aileen Fullbright",
                4,
                "Bác sĩ rất khéo léo và nhanh chóng trong dịch vụ. Bệnh của tôi đã khỏi, cảm ơn rất nhiều! 🥰",
                353,
                "9 ngày trước"
        ));

        reviewAdapter = new ReviewAdapter(reviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);
    }

    private void filterReviews() {
        List<ReviewModel> filteredReviews;

        if (selectedRating == 0) {
            filteredReviews = new ArrayList<>(reviews);
        } else {
            filteredReviews = new ArrayList<>();
            int targetRating = 6 - selectedRating; // Convert index to rating (5,4,3,2,1)
            for (ReviewModel review : reviews) {
                if (review.rating == targetRating) {
                    filteredReviews.add(review);
                }
            }
        }

        reviewAdapter.updateReviews(filteredReviews);
    }
}