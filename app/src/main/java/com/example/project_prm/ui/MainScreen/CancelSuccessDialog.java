package com.example.project_prm.ui.MainScreen;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;

public class CancelSuccessDialog extends DialogFragment {
    
    private OnActionListener listener;
    
    public interface OnActionListener {
        void onClose();
    }
    
    public void setOnActionListener(OnActionListener listener) {
        this.listener = listener;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cancel_success, container, false);
        
        MaterialButton btnClose = view.findViewById(R.id.btnClose);
        
        btnClose.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClose();
            }
            dismiss();
        });
        
        return view;
    }
} 