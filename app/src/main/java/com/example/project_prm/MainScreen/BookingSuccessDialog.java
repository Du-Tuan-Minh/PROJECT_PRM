package com.example.project_prm.MainScreen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;

public class BookingSuccessDialog extends DialogFragment {
    
    private static final String TAG = "BookingSuccessDialog";
    private OnActionListener listener;
    
    public interface OnActionListener {
        void onViewAppointment();
        void onClose();
    }
    
    public static BookingSuccessDialog newInstance() {
        return new BookingSuccessDialog();
    }
    
    public void setOnActionListener(OnActionListener listener) {
        this.listener = listener;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            Log.d(TAG, "onCreateView called");
            View view = inflater.inflate(R.layout.dialog_booking_success, container, false);
            
            MaterialButton btnViewAppointment = view.findViewById(R.id.btnViewAppointment);
            MaterialButton btnClose = view.findViewById(R.id.btnCancel);
            
            if (btnViewAppointment != null) {
                btnViewAppointment.setOnClickListener(v -> {
                    Log.d(TAG, "btnViewAppointment clicked");
                    try {
                        if (listener != null) {
                            listener.onViewAppointment();
                        }
                        dismiss();
                    } catch (Exception e) {
                        Log.e(TAG, "Error in btnViewAppointment click: " + e.getMessage(), e);
                        dismiss();
                    }
                });
            }
            
            if (btnClose != null) {
                btnClose.setOnClickListener(v -> {
                    Log.d(TAG, "btnClose clicked");
                    try {
                        if (listener != null) {
                            listener.onClose();
                        }
                        dismiss();
                    } catch (Exception e) {
                        Log.e(TAG, "Error in btnClose click: " + e.getMessage(), e);
                        dismiss();
                    }
                });
            }
            
            return view;
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
            return null;
        }
    }
} 