package com.example.project_prm.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.project_prm.R;

public class ReviewSuccessDialog extends DialogFragment {
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
        View view = inflater.inflate(R.layout.dialog_review_success, container, false);
        Button btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            if (listener != null) listener.onClose();
            dismiss();
        });
        return view;
    }

    public static void show(FragmentManager fragmentManager, OnActionListener listener) {
        ReviewSuccessDialog dialog = new ReviewSuccessDialog();
        dialog.setOnActionListener(listener);
        dialog.show(fragmentManager, "ReviewSuccessDialog");
    }
} 