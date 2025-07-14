package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RescheduleReasonFragment extends Fragment {
    
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
        // Return a simple view for now
        View view = new View(getContext());
        view.setBackgroundColor(0xFFFFFFFF); // White background
        
        // Simulate reason selection after a delay
        view.postDelayed(() -> {
            if (listener != null) {
                listener.onReasonSelected("Tôi có lịch trình xung đột", "");
            }
        }, 1000);
        
        return view;
    }
} 