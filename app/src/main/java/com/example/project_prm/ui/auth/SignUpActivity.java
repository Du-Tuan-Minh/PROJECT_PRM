package com.example.project_prm.ui.auth;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.DAO.UserDAO;
import com.example.project_prm.R;
import com.example.project_prm.ui.dialog.StatusPopup;
import com.example.project_prm.widgets.DropDownFieldView;
import com.example.project_prm.widgets.EditTextFieldView;
import com.example.project_prm.widgets.RegisterClinicView;

import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    private EditTextFieldView registerEmailInput,
            registerPasswordInput,
            registerFullNameInput,
            registerDateOfBirthInput;
    private DropDownFieldView registerGenderInput, registerRoleInput;
    private Button signUpButton;
    private TextView signInText;

    private RegisterClinicView registerClinicView;
    private StatusPopup popup;
    private UserDAO userDAO;


    private void bindingView() {
        registerEmailInput = findViewById(R.id.registerEmailInput);
        registerPasswordInput = findViewById(R.id.registerPasswordInput);
        signUpButton = findViewById(R.id.signUpButton);
        signInText = findViewById(R.id.signInText);
        registerFullNameInput = findViewById(R.id.registerFullNameInput);
        registerDateOfBirthInput = findViewById(R.id.registerDateOfBirthInput);
        registerGenderInput = findViewById(R.id.registerGenderInput);
        registerRoleInput = findViewById(R.id.registerRoleInput);
        registerClinicView = findViewById(R.id.registerClinicView);
        popup = new StatusPopup(this);
        userDAO = new UserDAO();
    }

    private void bindingAction() {
        registerPasswordInput.setOnEndIconClickListener(this::onEyeIconClick);
        signUpButton.setOnClickListener(this::onSignUpClick);
        signInText.setOnClickListener(this::onGoToLoginClick);
        registerDateOfBirthInput.setOnEndIconClickListener(this::onDateOfBirthIconClick);
        registerDateOfBirthInput.getEditText().setEnabled(false);
        registerGenderInput.setOnItemSelectedListener(this::onGenderIconClick);
        registerRoleInput.setOnItemSelectedListener(this::onRoleIconClick);
        registerClinicView.hiddenLinearLayout();
    }

    private void setClearFocus() {
        registerEmailInput.clearFocus();
        registerFullNameInput.clearFocus();
        registerDateOfBirthInput.clearFocus();
        registerGenderInput.clearFocus();
        registerRoleInput.clearFocus();
        registerPasswordInput.clearFocus();
    }

    private void onRoleIconClick(String s, int i) {
        //Toast.makeText(this, "Role selected: " + s, Toast.LENGTH_SHORT).show();
        // Handle role selection here
        if (s != null) {
            registerRoleInput.getDropdownItems().setHintEnabled(false);
            onRoleIconClick(s);
        }
    }

    // Display RegisterClinicView when registerRoleInput selected is Clinic value
    private void onRoleIconClick(String s) {
        if (s.equals("Clinic")) {
            registerClinicView.showLinearLayout();
        } else {
            registerClinicView.hiddenLinearLayout();
        }
    }


    private void onGenderIconClick(String s, int i) {
        //Toast.makeText(this, "Gender selected: " + s, Toast.LENGTH_SHORT).show();
        // Handle gender selection here
        if (s != null) {
            registerGenderInput.getDropdownItems().setHintEnabled(false);
        }
    }


    private void onDateOfBirthIconClick(EditTextFieldView editTextFieldView) {
        //Toast.makeText(this, "Date of birth icon clicked", Toast.LENGTH_SHORT).show();
        // Handle date of birth icon click event here
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (dpView, year, month, day) -> {
                    String date = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year);
                    editTextFieldView.getEditText().setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void onGoToLoginClick(View view) {
        setClearFocus();
        startActivity(new Intent(this, SignInActivity.class));
    }

    private void onSignUpClick(View view) {
        String email = registerEmailInput.getFieldText();
        String password = registerPasswordInput.getFieldText();
        String gender = registerGenderInput.getSelectedItem();
        String dateOfBirth = registerDateOfBirthInput.getFieldText();
        String name = registerFullNameInput.getFieldText();
        String role = registerRoleInput.getSelectedItem();

        if (email.isEmpty() || password.isEmpty() ||
                gender.isEmpty() || dateOfBirth.isEmpty() ||
                name.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (registerClinicView.getRegisterClinicView().getVisibility() == View.VISIBLE &&
                !registerClinicView.checkValidRegisterClinic(this)) {
            return;
        }
        userDAO.isEmailExist(email)
                .addOnSuccessListener(exists -> {
                    if (exists){
                        popup.setErrorPopup("Oops, Failed!",
                                "This email already exist","Oki");
                        popup.hiddenCancelButton();
                        popup.show();
                        return;
                    }
                    userDAO.register(email, password, gender, dateOfBirth, name, role)
                            .addOnSuccessListener(this::onRegisterSuccess)
                            .addOnFailureListener(this::onRegisterFailure);
                })
                .addOnFailureListener(e -> {
                    popup.setErrorPopup("Oops, Failed!",
                            "Register failed, please try again later","Oki");
                    popup.hiddenCancelButton();
                    popup.show();
                });
    }

    private void onRegisterFailure(Exception e) {
        popup.setErrorPopup("\"Oops, Failed!",
                "Register failed, please try again later",
                "Oki");
        popup.hiddenCancelButton();
        popup.show();
    }

    private void onRegisterSuccess(Void unused) {
        // Register clinic when success pass userId with userId is id of user register
        if (registerClinicView.getRegisterClinicView().getVisibility() == View.VISIBLE) {
            userDAO.getUserIdByEmail(registerEmailInput.getFieldText())
                    .addOnSuccessListener(this::onRegisterClinicSuccess);
        }

        startActivity(new Intent(this, SignInActivity.class));
        finish();

    }

    private void onRegisterClinicSuccess(String s) {
        registerClinicView.registerClinic(s);
    }

    private void onEyeIconClick(EditTextFieldView editTextFieldView) {
        EditText editText = editTextFieldView.getEditText();
        ImageView icon = editTextFieldView.getEndIcon();

        int inputType = editText.getInputType();
        boolean isVisible = (inputType & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

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
        setContentView(R.layout.activity_signup);

        bindingView();
        bindingAction();

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerRoleInput.clearFocus();
        setClearFocus();
    }
}
