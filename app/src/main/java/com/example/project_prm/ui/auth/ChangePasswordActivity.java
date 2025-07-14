package com.example.project_prm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.project_prm.DataManager.DAO.UserDAO;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_prm.ui.dialog.StatusPopup;

import com.example.project_prm.R;
import com.example.project_prm.utils.CurrentUser;
import com.example.project_prm.utils.HashUtil;
import com.example.project_prm.widgets.EditTextFieldView;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditTextFieldView oldPasswordInput;
    private EditTextFieldView newPasswordInput;
    private EditTextFieldView confirmNewPasswordInput;
    private Button changePasswordButton;
    private StatusPopup popup;
    private UserDAO userDAO;

    private void bindingView(){
        oldPasswordInput = findViewById(R.id.OldPasswordInput);
        newPasswordInput = findViewById(R.id.NewPasswordInput);
        confirmNewPasswordInput = findViewById(R.id.confirmNewPasswordInput);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        userDAO = new UserDAO();
        popup = new StatusPopup(this);
    }

    private void bindingAction(){
        changePasswordButton.setOnClickListener(this::onChangePasswordButtonClick);
        oldPasswordInput.setOnEndIconClickListener(this::onEndIconClick);
        oldPasswordInput.requestFocus();
        newPasswordInput.setOnEndIconClickListener(this::onEndIconClick);
        confirmNewPasswordInput.setOnEndIconClickListener(this::onEndIconClick);
    }

    private void setClearFocus(){
        oldPasswordInput.clearFocus();
        newPasswordInput.clearFocus();
        confirmNewPasswordInput.clearFocus();
    }

    private boolean isTruePassWord(String oldPassword){
        String userPassWord;
        if(getIntent().hasExtra("resetPassword")){
            userPassWord = getIntent().getStringExtra("resetPassword");

            return Objects.equals(userPassWord, oldPassword);
        }

        if (!CurrentUser.isLoggedIn(this)) {
            return false;
        }

        userPassWord = userDAO.getById(CurrentUser.getUserId(this))
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return task.getResult().get("password").toString();
                }).toString();

        return Objects.equals(userPassWord, HashUtil.sha256(oldPassword));
    }

    private void onPrimaryPopupClick(View view) {
        popup.dismiss();
        startActivity(new Intent(this, SignInActivity.class));
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
        StatusPopup popup = new StatusPopup(this);
        // handle change password logic here
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            popup.setErrorPopup("Oops, Failed!", "Cant empty...","Oki");
            popup.show();
            return;
        }

        if(!isTruePassWord(oldPassword)){
            popup.setErrorPopup("Oops, Failed!", "Old password is wrong...","Oki");
            popup.hiddenCancelButton();
            popup.show();
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {

            popup.setErrorPopup("Oops, Failed!", "New password not match...","Oki");
            popup.show();
            return;
        }
        // handle change password logic here
        userDAO.changePassword(CurrentUser.getUserId(this), newPassword)
                .addOnSuccessListener(this::onChangePasswordSuccess)
                .addOnFailureListener(this::onChangePasswordFailure);

    }

    private void onChangePasswordFailure(Exception e) {
        popup.setErrorPopup("Oops, Failed!", "Change password failed...","Oki");
        popup.setPrimaryClick(v -> popup.dismiss());
        popup.hiddenCancelButton();
        popup.show();
    }

    private void onChangePasswordSuccess(Void unused) {
        popup.setSuccessPopup("Success!", "Change password success","Oki");
        popup.setPrimaryClick(v -> popup.dismiss());
        popup.hiddenCancelButton();
        popup.show();
        setClearFocus();
        startActivity(new Intent(this, SignInActivity.class));
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        bindingView();
        bindingAction();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setClearFocus();
    }
}