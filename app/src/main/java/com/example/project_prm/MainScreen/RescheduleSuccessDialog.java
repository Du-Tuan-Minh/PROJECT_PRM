package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;

public class RescheduleSuccessDialog extends DialogFragment {
    
    private OnActionListener listener;
    
    public interface OnActionListener {
        void onViewAppointment();
        void onClose();
    }
    
    public static RescheduleSuccessDialog newInstance() {
        return new RescheduleSuccessDialog();
    }
    
    public void setOnActionListener(OnActionListener listener) {
        this.listener = listener;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_booking_success, container, false);
        
        MaterialButton btnViewAppointment = view.findViewById(R.id.btnViewAppointment);
        MaterialButton btnClose = view.findViewById(R.id.btnClose);
        
        btnViewAppointment.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewAppointment();
            }
            dismiss();
        });
        
        btnClose.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClose();
            }
            dismiss();
        });
        
        return view;
    }
} 