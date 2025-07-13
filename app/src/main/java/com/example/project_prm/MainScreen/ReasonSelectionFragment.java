package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;

public class ReasonSelectionFragment extends Fragment {
    
    private TextView tvTitle;
    private RadioGroup rgReasons;
    private EditText etOtherReason;
    private MaterialButton btnSubmit;
    
    private String title;
    private String[] reasons;
    private String submitButtonText;
    private OnReasonSelectedListener listener;
    
    public interface OnReasonSelectedListener {
        void onReasonSelected(String reason, String otherReason);
    }
    
    public static ReasonSelectionFragment newInstance(String title, String[] reasons, String submitButtonText) {
        ReasonSelectionFragment fragment = new ReasonSelectionFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArray("reasons", reasons);
        args.putString("submitButtonText", submitButtonText);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            reasons = getArguments().getStringArray("reasons");
            submitButtonText = getArguments().getString("submitButtonText");
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reason_selection, container, false);
        
        initViews(view);
        setupReasons();
        setupListeners();
        
        return view;
    }
    
    private void initViews(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        rgReasons = view.findViewById(R.id.rgReasons);
        etOtherReason = view.findViewById(R.id.etOtherReason);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        
        tvTitle.setText(title);
        btnSubmit.setText(submitButtonText);
        btnSubmit.setEnabled(false);
    }
    
    private void setupReasons() {
        if (reasons != null) {
            for (int i = 0; i < reasons.length; i++) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setId(i);
                radioButton.setText(reasons[i]);
                radioButton.setTextSize(16);
                radioButton.setTextColor(getResources().getColor(R.color.text_black));
                
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 24);
                radioButton.setLayoutParams(params);
                
                rgReasons.addView(radioButton);
            }
        }
    }
    
    private void setupListeners() {
        rgReasons.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId >= 0 && checkedId < reasons.length) {
                String selectedReason = reasons[checkedId];
                
                if (selectedReason.equals("Khác")) {
                    etOtherReason.setVisibility(View.VISIBLE);
                    etOtherReason.addTextChangedListener(new android.text.TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            updateSubmitButton();
                        }
                        
                        @Override
                        public void afterTextChanged(android.text.Editable s) {}
                    });
                } else {
                    etOtherReason.setVisibility(View.GONE);
                }
                
                updateSubmitButton();
            }
        });
        
        btnSubmit.setOnClickListener(v -> {
            int checkedId = rgReasons.getCheckedRadioButtonId();
            if (checkedId >= 0 && checkedId < reasons.length) {
                String selectedReason = reasons[checkedId];
                String otherReason = selectedReason.equals("Khác") ? etOtherReason.getText().toString().trim() : null;
                
                if (listener != null) {
                    listener.onReasonSelected(selectedReason, otherReason);
                }
            }
        });
    }
    
    private void updateSubmitButton() {
        int checkedId = rgReasons.getCheckedRadioButtonId();
        boolean isValid = false;
        
        if (checkedId >= 0 && checkedId < reasons.length) {
            String selectedReason = reasons[checkedId];
            if (selectedReason.equals("Khác")) {
                isValid = !TextUtils.isEmpty(etOtherReason.getText().toString().trim());
            } else {
                isValid = true;
            }
        }
        
        btnSubmit.setEnabled(isValid);
        btnSubmit.setAlpha(isValid ? 1.0f : 0.5f);
    }
    
    public void setOnReasonSelectedListener(OnReasonSelectedListener listener) {
        this.listener = listener;
    }
} 