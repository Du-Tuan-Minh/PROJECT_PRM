package com.example.project_prm.ui.auth;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_prm.ui.dialog.StatusPopup;

import com.example.project_prm.R;
import com.example.project_prm.widgets.EditTextFieldView;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditTextFieldView oldPasswordInput;
    private EditTextFieldView newPasswordInput;
    private EditTextFieldView confirmNewPasswordInput;
    private Button changePasswordButton;

    private void bindingView(){
        oldPasswordInput = findViewById(R.id.OldPasswordInput);
        newPasswordInput = findViewById(R.id.NewPasswordInput);
        confirmNewPasswordInput = findViewById(R.id.confirmNewPasswordInput);
        changePasswordButton = findViewById(R.id.changePasswordButton);
    }

    private void bindingAction(){
        changePasswordButton.setOnClickListener(this::onChangePasswordButtonClick);
        oldPasswordInput.setOnEndIconClickListener(this::onEndIconClick);
        oldPasswordInput.requestFocus();
        newPasswordInput.setOnEndIconClickListener(this::onEndIconClick);
        confirmNewPasswordInput.setOnEndIconClickListener(this::onEndIconClick);
    }

    private void onEndIconClick(EditTextFieldView editTextFieldView) {
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

    private void onChangePasswordButtonClick(View view) {
        String oldPassword = oldPasswordInput.getFieldText();
        String newPassword = newPasswordInput.getFieldText();
        String confirmNewPassword = confirmNewPasswordInput.getFieldText();
        // handle change password logic here
        StatusPopup popup = new StatusPopup(this);
        popup.setPrimaryClick(v -> popup.dismiss());
        popup.setCancelClick(v -> popup.dismiss());

        // Success Example
        popup.setErrorPopup();
        popup.show();


        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            return;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        bindingView();
        bindingAction();

    }
}