package com.example.project_prm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_prm.R;

public class LoginOptionActivity extends AppCompatActivity {

    private LinearLayout gotoFacebookLogin, gotoGoogleLogin;
    private TextView signUpText;
    private Button goToSignInButton;

    private void bindingViews() {
        gotoFacebookLogin = findViewById(R.id.gotoFacebookLogin);
        gotoGoogleLogin = findViewById(R.id.gotoGoogleLogin);
        signUpText = findViewById(R.id.signUpText);
        goToSignInButton = findViewById(R.id.goToSignInButton);

    }
    private void bindingActions() {
        gotoFacebookLogin.setOnClickListener(this::onGotoFacebookLoginClicked);
        gotoGoogleLogin.setOnClickListener(this::onGotoGoogleLoginClicked);
        signUpText.setOnClickListener(this::onSignUpTextClicked);
        goToSignInButton.setOnClickListener(this::onGoToSignInButtonClicked);

    }

    private void onGoToSignInButtonClicked(View view) {
        startActivity(new Intent(this, SignInActivity.class));
    }

    private void onSignUpTextClicked(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void onGotoGoogleLoginClicked(View view) {
        // Handle Google login button click
    }

    private void onGotoFacebookLoginClicked(View view) {
        // Handle Facebook login button click
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_option);
        bindingViews();
        bindingActions();

    }
}