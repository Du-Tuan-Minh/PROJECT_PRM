package com.example.project_prm.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        Button btnClose = view.findViewById(R.id.btnClose);
        tvTitle.setText(R.string.review_success_title);
        tvMessage.setText(R.string.review_success_message);
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