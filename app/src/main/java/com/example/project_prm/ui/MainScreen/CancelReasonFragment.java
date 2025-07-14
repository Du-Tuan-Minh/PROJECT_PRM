package com.example.project_prm.ui.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.project_prm.R;
import com.example.project_prm.widgets.EditTextFieldView;
import com.google.android.material.button.MaterialButton;

public class CancelReasonFragment extends Fragment {
    
    private RadioGroup rgReasons;
    private RadioButton rbOther;
    private EditTextFieldView etOtherReason;
    private MaterialButton btnConfirm;
    private OnReasonSelectedListener listener;
    
    public interface OnReasonSelectedListener {
        void onReasonSelected(String reason, String otherReason);
    }
    
    public void setOnReasonSelectedListener(OnReasonSelectedListener listener) {
        this.listener = listener;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cancel_reason, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupListeners();
    }
    
    private void initViews(View view) {
        rgReasons = view.findViewById(R.id.rgReasons);
        rbOther = view.findViewById(R.id.rbOther);
        etOtherReason = view.findViewById(R.id.etOtherReason);
        btnConfirm = view.findViewById(R.id.btnConfirm);
    }
    
    private void setupListeners() {
        rgReasons.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbOther) {
                etOtherReason.setVisibility(View.VISIBLE);
            } else {
                etOtherReason.setVisibility(View.GONE);
            }
        });
        
        btnConfirm.setOnClickListener(v -> {
            int checkedId = rgReasons.getCheckedRadioButtonId();
            String reason = "";
            String otherReason = "";
            
            if (checkedId == R.id.rbSchedule) {
                reason = "Lịch trình thay đổi";
            } else if (checkedId == R.id.rbHealth) {
                reason = "Tình trạng sức khỏe tốt hơn";
            } else if (checkedId == R.id.rbCost) {
                reason = "Chi phí quá cao";
            } else if (checkedId == R.id.rbOther) {
                reason = "Khác";
                otherReason = etOtherReason.getFieldText();
            }
            
            if (!reason.isEmpty()) {
                if (listener != null) {
                    listener.onReasonSelected(reason, otherReason);
                }
            }
        });
    }
} 