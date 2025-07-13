/*
 * 📁 ĐƯỜNG DẪN: app/src/main/java/com/example/project_prm/ui/AppointmentScreen/SimpleAppointmentFragment.java
 * 🎯 CHỨC NĂNG: Fragment đơn giản để thay thế các fragment bị thiếu
 * ⚠️ TẠO MỚI: File này để fix lỗi missing fragments
 */

package com.example.project_prm.ui.AppointmentScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SimpleAppointmentFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_TYPE = "type";

    private String title;
    private String type;

    public static SimpleAppointmentFragment newInstance(String title, String type) {
        SimpleAppointmentFragment fragment = new SimpleAppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Tạo TextView đơn giản
        TextView textView = new TextView(getContext());
        textView.setText(getDisplayText());
        textView.setTextSize(16);
        textView.setPadding(50, 50, 50, 50);
        textView.setGravity(android.view.Gravity.CENTER);

        return textView;
    }

    private String getDisplayText() {
        switch (type) {
            case "upcoming":
                return "📅 LỊCH HẸN SẮP TỚI\n\n" +
                        "Hiện tại chưa có lịch hẹn nào.\n\n" +
                        "Bạn có thể đặt lịch khám mới từ màn hình chính.";

            case "completed":
                return "✅ LỊCH HẸN ĐÃ HOÀN THÀNH\n\n" +
                        "Chưa có lịch hẹn đã hoàn thành.\n\n" +
                        "Các cuộc hẹn đã khám sẽ hiển thị ở đây.";

            case "cancelled":
                return "❌ LỊCH HẸN ĐÃ HỦY\n\n" +
                        "Chưa có lịch hẹn nào bị hủy.\n\n" +
                        "Các cuộc hẹn đã hủy sẽ hiển thị ở đây.";

            default:
                return "📋 " + title + "\n\nChức năng đang được phát triển...";
        }
    }
}