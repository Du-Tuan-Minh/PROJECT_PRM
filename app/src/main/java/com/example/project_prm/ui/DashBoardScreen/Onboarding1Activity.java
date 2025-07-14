package com.example.project_prm.ui.DashBoardScreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.R;

public class Onboarding1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding1);

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            startActivity(new Intent(Onboarding1Activity.this, Onboarding2Activity.class));
            finish();
        });
    }
}
