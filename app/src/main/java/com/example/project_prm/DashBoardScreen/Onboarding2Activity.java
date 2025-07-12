package com.example.project_prm.DashBoardScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.MainActivity;
import com.example.project_prm.R;

public class Onboarding2Activity extends AppCompatActivity {
    private ImageView image;
    private TextView text;
    private Button btnNext;
    private int currentIndex = 0;

    private int[] images = {
            R.drawable.doc1, R.drawable.doc2, R.drawable.doc3
    };

    private String[] descriptions = {
            "Thousands of doctors & experts to help your health!",
            "Health checks & consultations easily anywhere anytime",
            "Let’s start living healthy and well with us right now!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding2);

        image = findViewById(R.id.imgDoc);
        text = findViewById(R.id.txtDescription);
        btnNext = findViewById(R.id.btnNext);

        updateContent();

        btnNext.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex < images.length) {
                updateContent();
            } else {
                SharedPreferences.Editor editor = getSharedPreferences("my_prefs", MODE_PRIVATE).edit();
                editor.putBoolean("is_first_time", false);
                editor.apply();

                startActivity(new Intent(Onboarding2Activity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void updateContent() {
        image.setImageResource(images[currentIndex]);
        text.setText(descriptions[currentIndex]);
    }
}
