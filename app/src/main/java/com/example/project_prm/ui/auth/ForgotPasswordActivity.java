package com.example.project_prm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.R;
import com.example.project_prm.ui.dialog.StatusPopup;
import com.example.project_prm.widgets.EditTextFieldView;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditTextFieldView forgetPasswordEmailInput;
    private Button forgetButton;
    private TextView signInText;

    private void bindingView(){
        forgetPasswordEmailInput = findViewById(R.id.forgetPasswordEmailInput);
        forgetButton = findViewById(R.id.forgetButton);
        signInText = findViewById(R.id.signInText);
    }
    private void bindingAction(){
        forgetButton.setOnClickListener(this::onForgetClick);
        signInText.setOnClickListener(this::onSignInClick);
    }

    private void setClearFocus(){
        forgetPasswordEmailInput.clearFocus();
    }

    private void onSignInClick(View view) {
        setClearFocus();
        startActivity(new Intent(this, SignInActivity.class));
    }

    private void onForgetClick(View view) {
        String email = forgetPasswordEmailInput.getFieldText();
        if (email.isEmpty()) {
            StatusPopup popup = new StatusPopup(this);
            popup.setErrorPopup("\"Oops, Failed!", "Desvv...","Oki");
            popup.show();
            return;
        }
        // handle forget password logic here
        setClearFocus();
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        bindingView();
        bindingAction();
    }
    @Override
    protected void onStart() {
        super.onStart();
        setClearFocus();
    }
}
