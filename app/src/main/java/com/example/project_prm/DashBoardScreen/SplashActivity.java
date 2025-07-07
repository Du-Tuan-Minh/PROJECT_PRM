package com.example.project_prm.DashBoardScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.project_prm.LoginActivity;
import com.example.project_prm.MainActivity;
import com.example.project_prm.R;





public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2000; // 2 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            boolean isFirstTime = prefs.getBoolean("first_time", true);

            if (isFirstTime) {
                startActivity(new Intent(this, Onboarding1Activity.class));
                prefs.edit().putBoolean("first_time", false).apply();
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }

            finish();
        }, SPLASH_TIME);
    }
}