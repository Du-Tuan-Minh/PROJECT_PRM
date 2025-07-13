package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;
import com.example.project_prm.MainScreen.AppointmentRepository;
import com.example.project_prm.MainScreen.AppointmentModel;

public class WriteReviewActivity extends AppCompatActivity {

    private ImageView ivBack, ivDoctorAvatar;
    private TextView tvTitle, tvDoctorName, tvQuestion, tvRecommendQuestion;
    private StarRatingView starRating;
    private EditText etReviewText;
    private RadioGroup rgRecommend;
    private RadioButton rbYes, rbNo;
    private MaterialButton btnCancel, btnSubmit;

    private String doctorName = "BS. Drake Boeson";
    private int selectedRating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        initViews();
        setupListeners();
        loadDoctorData();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        ivDoctorAvatar = findViewById(R.id.ivDoctorAvatar);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvQuestion = findViewById(R.id.tvQuestion);
        starRating = findViewById(R.id.starRating);
        etReviewText = findViewById(R.id.etReviewText);
        tvRecommendQuestion = findViewById(R.id.tvRecommendQuestion);
        rgRecommend = findViewById(R.id.rgRecommend);
        rbYes = findViewById(R.id.rbYes);
        rbNo = findViewById(R.id.rbNo);
        btnCancel = findViewById(R.id.btnCancel);
        btnSubmit = findViewById(R.id.btnSubmit);

        tvTitle.setText("Viết đánh giá");
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        starRating.setOnRatingChangeListener(rating -> {
            selectedRating = rating;
            updateSubmitButton();
        });

        etReviewText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSubmitButton();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        rgRecommend.setOnCheckedChangeListener((group, checkedId) -> {
            updateSubmitButton();
        });

        btnCancel.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> submitReview());
    }

    private void loadDoctorData() {
        // Lấy thông tin bác sĩ từ Intent
        String appointmentId = getIntent().getStringExtra("appointment_id");

        // Load dữ liệu mẫu
        tvDoctorName.setText(doctorName);
        tvQuestion.setText("Trải nghiệm của bạn với " + doctorName + " như thế nào?");
        tvRecommendQuestion.setText("Bạn có muốn giới thiệu " + doctorName + " cho bạn bè không?");

        // Load avatar
        Glide.with(this)
                .load(R.drawable.ic_general) // Placeholder
                .circleCrop()
                .into(ivDoctorAvatar);

        // Set placeholder text
        etReviewText.setHint("BS. " + doctorName + " là một người rất thân thiện và chuyên nghiệp trong công việc. Tôi đã tư vấn với bác sĩ trong 30 phút và bác sĩ luôn phản hồi nhanh chóng và rõ ràng về các khiếu nại của tôi. Tôi rất thích và đặc biệt giới thiệu BS. " + doctorName + " cho bạn 👍");
    }

    private void updateSubmitButton() {
        boolean isValid = selectedRating > 0 &&
                !TextUtils.isEmpty(etReviewText.getText().toString().trim()) &&
                rgRecommend.getCheckedRadioButtonId() != -1;

        btnSubmit.setEnabled(isValid);
        btnSubmit.setAlpha(isValid ? 1.0f : 0.5f);
    }

    private void submitReview() {
        if (selectedRating == 0) {
            return;
        }

        String reviewText = etReviewText.getText().toString().trim();
        if (TextUtils.isEmpty(reviewText)) {
            return;
        }

        boolean recommend = rgRecommend.getCheckedRadioButtonId() == R.id.rbYes;

        // Xử lý gửi đánh giá
        processReviewSubmission(selectedRating, reviewText, recommend);
    }

    private void processReviewSubmission(int rating, String reviewText, boolean recommend) {
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Đang gửi...");
        String appointmentId = getIntent().getStringExtra("appointment_id");
        AppointmentRepository.getInstance().saveReview(appointmentId, rating, reviewText);
        new android.os.Handler().postDelayed(() -> {
            showSuccessModal();
        }, 1000);
    }

    private void showSuccessModal() {
        ReviewSuccessDialog dialog = new ReviewSuccessDialog();
        dialog.setOnActionListener(new ReviewSuccessDialog.OnActionListener() {
            @Override
            public void onViewReview() {
                Intent intent = new Intent(WriteReviewActivity.this, AppointmentHistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onClose() {
                Intent intent = new Intent(WriteReviewActivity.this, AppointmentHistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), "review_success");
    }
}