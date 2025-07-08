package com.example.project_prm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.DAO.UserDAO;
import com.example.project_prm.R;
import com.example.project_prm.widgets.DropDownFieldView;
import com.example.project_prm.widgets.EditTextFieldView;

public class SignInActivity extends AppCompatActivity {
    private EditTextFieldView loginEmailInput, loginPasswordInput;
    private Button signInButton;
    private TextView goToSignupText, forgetPasswordText;
    private DatabaseHelper dbHelper;
    private UserDAO userDAO;

    private void bindingView(){
        loginEmailInput = findViewById(R.id.loginEmailInput);
        loginPasswordInput = findViewById(R.id.loginPasswordInput);
        signInButton = findViewById(R.id.signInButton);
        goToSignupText = findViewById(R.id.signUpText);
        forgetPasswordText = findViewById(R.id.forgetPasswordText);
    }
    private void bindingAction(){
        loginPasswordInput.setOnEndIconClickListener(this::onEyeIconClick);
        signInButton.setOnClickListener(this::onSignInClick);
        goToSignupText.setOnClickListener(this::onGoToSignupClick);
        forgetPasswordText.setOnClickListener(this::onForgetPasswordClick);
    }

    private void setClearFocus(){
        loginEmailInput.clearFocus();
        loginPasswordInput.clearFocus();
    }

    private void onForgetPasswordClick(View view) {
        setClearFocus();
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    private void onGoToSignupClick(View view) {
        setClearFocus();
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void onSignInClick(View view) {
        String username = loginEmailInput.getFieldText();
        String password = loginPasswordInput.getFieldText();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
//        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
//        if (prefs.getInt("userId", -1) != -1) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//            return;
//        }
//
//            User user = userDAO.loginUser(username, password);
//            if (user != null) {
//                Toast.makeText(this, "Đăng nhập thành công: " + user.getName(), Toast.LENGTH_SHORT).show();
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putInt("userId", user.getId());
//                editor.putString("username", user.getUsername());
//                editor.apply();
//                startActivity(new Intent(this, MainActivity.class));
//                finish();
//            } else {
//                Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
//            }
    }

    private void onEyeIconClick(EditTextFieldView editTextFieldView) {
        EditText editText = editTextFieldView.getEditText();
        ImageView icon = editTextFieldView.getEndIcon();

        int inputType = editText.getInputType();
        boolean isVisible = (inputType & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

        if (isVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            icon.setImageResource(R.drawable.ic_eye_off);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            icon.setImageResource(R.drawable.ic_eye_off);
        }

        editText.setSelection(editText.getText().length());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        bindingView();
        bindingAction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setClearFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) { // Kiểm tra dbHelper không null trước khi đóng
            dbHelper.close();
        }
    }
}