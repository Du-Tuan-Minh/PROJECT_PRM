package com.example.project_prm.MainScreen;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;

public class BookingFailedDialog extends DialogFragment {

    private OnActionListener listener;

    public interface OnActionListener {
        void onTryAgain();
        void onCancel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_booking_failed, container, false);

        setupViews(view);

        return view;
    }

    private void setupViews(View view) {
        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        MaterialButton btnTryAgain = view.findViewById(R.id.btnTryAgain);
        MaterialButton btnCancel = view.findViewById(R.id.btnCancel);

        ivIcon.setImageResource(R.drawable.ic_cancel);
        ivIcon.setColorFilter(getResources().getColor(R.color.white));

        tvTitle.setText("Oops. Thất bại!");
        tvMessage.setText("Đặt lịch hẹn thất bại. Vui lòng kiểm tra kết nối internet và thử lại.");

        btnTryAgain.setText("Thử lại");
        btnCancel.setText("Hủy");

        btnTryAgain.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTryAgain();
            }
            dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            }
            dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    public void setOnActionListener(OnActionListener listener) {
        this.listener = listener;
    }
}