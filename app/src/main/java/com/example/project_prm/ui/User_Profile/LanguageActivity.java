package com.example.project_prm.ui.User_Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.MainActivity;
import com.example.project_prm.R;

public class LanguageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_language);

        setupBackButton();           // Xử lý nút quay lại
        setupLanguageSelection();    // Bắt sự kiện chọn ngôn ngữ
    }


    private void setupBackButton() {
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }


    private void setupLanguageSelection() {
        findViewById(R.id.english_us_item).setOnClickListener(v -> updateLanguage("en"));
        findViewById(R.id.vietnamese_item).setOnClickListener(v -> updateLanguage("vi"));

    }

    private void updateLanguage(String langCode) {
        LanguageHelper.setLanguage(this, langCode);
        LanguageHelper.applyLanguage(this);

        Intent intent = new Intent(this, MainActivity.class); // hoặc activity chính
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent); // Restart app để áp dụng ngôn ngữ mới
    }

}
