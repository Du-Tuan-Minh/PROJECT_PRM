package com.example.project_prm.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.project_prm.DataManager.DAO.ClinicDAO;
import com.example.project_prm.DataManager.Entity.Clinic;
import com.example.project_prm.R;
import com.example.project_prm.utils.DimensionUtils;

public class RegisterClinicView extends LinearLayout {

    private LinearLayout registerClinicView;
    private EditTextFieldView clinicNameInput;
    private EditTextFieldView clinicPhoneInput;
    private EditTextFieldView clinicEmailInput;
    private EditTextFieldView clinicSpecialInput;
    private EditTextFieldView clinicAddressInput;
    private final ClinicDAO clinicDAO;

    public RegisterClinicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        clinicDAO = new ClinicDAO();
    }


    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.register_clinic_component, this, true);
        bindingView();
    }

    private void bindingView() {
        registerClinicView = findViewById(R.id.registerClinicView);
        clinicNameInput = findViewById(R.id.clinicNameInput);
        clinicPhoneInput = findViewById(R.id.clinicPhoneInput);
        clinicEmailInput = findViewById(R.id.clinicEmailInput);
        clinicSpecialInput = findViewById(R.id.clinicSpecialInput);
        clinicAddressInput = findViewById(R.id.clinicAddressInput);
    }

    public void hiddenLinearLayout() {
        registerClinicView.setVisibility(View.GONE);
    }
    public void showLinearLayout() {
        registerClinicView.setVisibility(View.VISIBLE);
    }

    public void registerClinic(String userId) {
        String name = clinicNameInput.getFieldText();
        String phone = clinicPhoneInput.getFieldText();
        String email = clinicEmailInput.getFieldText();
        String specialties = clinicSpecialInput.getFieldText();
        String address = clinicAddressInput.getFieldText();

        clinicDAO.registerClinic(name, phone, email, address, specialties, userId);
    }

    // check valid register clinic
    public boolean checkValidRegisterClinic(Context context){
        if(clinicNameInput.getFieldText().isEmpty()
                || clinicPhoneInput.getFieldText().isEmpty()
                || clinicEmailInput.getFieldText().isEmpty()
                || clinicSpecialInput.getFieldText().isEmpty()
                || clinicAddressInput.getFieldText().isEmpty()){
            Toast.makeText(context, "Please fill in all fields",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!clinicPhoneInput.getFieldText().matches("\\d{10,11}")){
            Toast.makeText(context, "Please enter a valid phone number in clinic",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!clinicEmailInput.getFieldText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            Toast.makeText(context, "Please enter a valid email in clinic",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public LinearLayout getRegisterClinicView() {
        return registerClinicView;
    }

    public void setRegisterClinicView(LinearLayout registerClinicView) {
        this.registerClinicView = registerClinicView;
    }

    public EditTextFieldView getClinicNameInput() {
        return clinicNameInput;
    }

    public void setClinicNameInput(EditTextFieldView clinicNameInput) {
        this.clinicNameInput = clinicNameInput;
    }

    public EditTextFieldView getClinicPhoneInput() {
        return clinicPhoneInput;
    }

    public void setClinicPhoneInput(EditTextFieldView clinicPhoneInput) {
        this.clinicPhoneInput = clinicPhoneInput;
    }

    public EditTextFieldView getClinicEmailInput() {
        return clinicEmailInput;
    }

    public void setClinicEmailInput(EditTextFieldView clinicEmailInput) {
        this.clinicEmailInput = clinicEmailInput;
    }

    public EditTextFieldView getClinicSpecialInput() {
        return clinicSpecialInput;
    }

    public void setClinicSpecialInput(EditTextFieldView clinicSpecialInput) {
        this.clinicSpecialInput = clinicSpecialInput;
    }
}
