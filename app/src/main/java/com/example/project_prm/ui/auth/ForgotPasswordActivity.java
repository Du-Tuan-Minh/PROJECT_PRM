package com.example.project_prm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.DAO.UserDAO;
import com.example.project_prm.R;
import com.example.project_prm.ui.dialog.StatusPopup;
import com.example.project_prm.utils.GMailSender;
import com.example.project_prm.utils.PasswordGenerator;
import com.example.project_prm.widgets.EditTextFieldView;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditTextFieldView forgetPasswordEmailInput;
    private Button forgetButton;
    private TextView signInText;
    StatusPopup popup;
    private UserDAO userDAO;

    private void bindingView(){
        forgetPasswordEmailInput = findViewById(R.id.forgetPasswordEmailInput);
        forgetButton = findViewById(R.id.forgetButton);
        signInText = findViewById(R.id.signInText);
        popup = new StatusPopup(this);
        userDAO = new UserDAO();
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
            popup.setErrorPopup("\"Oops, Failed!",
                    "Please enter your email!","Oki");
            popup.hiddenCancelButton();
            popup.show();
            return;
        }

        userDAO.isEmailExist(email)
                .addOnSuccessListener(exists -> {
                    if (!exists){
                        popup.setErrorPopup("Oops, Failed!",
                                "Email not exist!","Oki");
                        popup.hiddenCancelButton();
                        popup.show();
                        return;
                    }
                    popup.setSuccessPopup("Success!",
                            "Please check your mail to get password","Go to change password");
                    popup.setPrimaryClick(this::onPrimaryClick);
                    popup.setCancelClick(v -> popup.dismiss());
                    popup.show();
                })
                .addOnFailureListener(e -> {
                    popup.setErrorPopup("Oops, Failed!",
                            "Please try again or later!","Oki");
                    popup.hiddenCancelButton();
                    popup.show();
                });
    }

    private void onPrimaryClick(View view) {
        String password = PasswordGenerator.generatePassword(10);
        new Thread(() -> {
            try {
                GMailSender.sendMail(forgetPasswordEmailInput.getFieldText(),
                        "Reset Password","Your new password is: "+password);
                runOnUiThread(() -> Toast.makeText(this, "Email sent",
                        Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Failed: " +
                        e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
        setClearFocus();
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("resetPassword",password);
        popup.dismiss();
        startActivity(intent);
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
