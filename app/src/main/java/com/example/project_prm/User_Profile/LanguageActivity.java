package com.example.project_prm.User_Profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.R;

public class LanguageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_language);

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }
}
