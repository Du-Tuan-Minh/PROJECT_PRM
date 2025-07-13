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

public class ReviewSuccessDialog extends DialogFragment {
    
    private OnActionListener listener;
    
    public interface OnActionListener {
        void onViewReview();
        void onClose();
    }
    
    public static ReviewSuccessDialog newInstance() {
        return new ReviewSuccessDialog();
    }
    
    public void setOnActionListener(OnActionListener listener) {
        this.listener = listener;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_review_success, container, false);
        
        MaterialButton btnViewReview = view.findViewById(R.id.btnPrimary);
        MaterialButton btnClose = view.findViewById(R.id.btnSecondary);
        
        btnViewReview.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewReview();
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