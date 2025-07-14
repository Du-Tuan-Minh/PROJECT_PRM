package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.project_prm.R;
import com.example.project_prm.widgets.EditTextFieldView;
import com.google.android.material.button.MaterialButton;

public class PatientDetailsFragment extends Fragment {
    
    private EditTextFieldView etFullName;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private SeekBar seekBarAge;
    private TextView tvAge;
    private EditTextFieldView etProblem;
    private MaterialButton btnNext;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_details, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupListeners();
    }
    
    private void initViews(View view) {
        etFullName = view.findViewById(R.id.etFullName);
        rgGender = view.findViewById(R.id.rgGender);
        rbMale = view.findViewById(R.id.rbMale);
        rbFemale = view.findViewById(R.id.rbFemale);
        seekBarAge = view.findViewById(R.id.seekBarAge);
        tvAge = view.findViewById(R.id.tvAge);
        etProblem = view.findViewById(R.id.etProblem);
        btnNext = view.findViewById(R.id.btnNext);
    }
    
    private void setupListeners() {
        seekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAge.setText(String.valueOf(progress));
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        btnNext.setOnClickListener(v -> {
            if (validateInputs()) {
                String fullName = etFullName.getFieldText();
                String gender = rbMale.isChecked() ? "Nam" : "Nữ";
                String age = tvAge.getText().toString();
                String problem = etProblem.getFieldText();
                
                BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
                if (activity != null) {
                    activity.onPatientDetailsEntered(fullName, gender, age, problem);
                }
            }
        });
    }
    
    private boolean validateInputs() {
        boolean isValid = true;
        
        if (etFullName.getFieldText().trim().isEmpty()) {
            // Show error message
            isValid = false;
        }
        
        if (etProblem.getFieldText().trim().isEmpty()) {
            // Show error message
            isValid = false;
        }
        
        return isValid;
    }
} 