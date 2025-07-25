package com.example.project_prm.ui.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;

public class CancelConfirmationDialog extends DialogFragment {
    
    private OnActionListener listener;
    
    public interface OnActionListener {
        void onConfirm();
        void onCancel();
    }
    
    public static CancelConfirmationDialog newInstance() {
        return new CancelConfirmationDialog();
    }
    
    public void setOnActionListener(OnActionListener listener) {
        this.listener = listener;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cancel_confirmation, container, false);
        
        MaterialButton btnConfirm = view.findViewById(R.id.btnConfirm);
        MaterialButton btnBack = view.findViewById(R.id.btnBack);
        
        btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm();
            }
            dismiss();
        });
        
        btnBack.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        });
        
        return view;
    }
} 