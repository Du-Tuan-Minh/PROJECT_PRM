package com.example.project_prm.MainScreen;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;

public class TermsDialog extends DialogFragment {

    private TextView tvTitle, tvContent;
    private MaterialButton btnAccept, btnCancel;
    private OnTermsActionListener listener;

    public interface OnTermsActionListener {
        void onAccept();
        void onCancel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_terms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupContent();
        setupListeners();
    }

    private void initViews(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvContent = view.findViewById(R.id.tvContent);
        btnAccept = view.findViewById(R.id.btnAccept);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    private void setupContent() {
        tvTitle.setText("Điều khoản sử dụng & Chính sách bảo mật");
        
        String termsContent = "**ĐIỀU KHOẢN SỬ DỤNG**\n\n" +
                "1. **Phạm vi áp dụng**\n" +
                "Điều khoản này áp dụng cho tất cả người dùng sử dụng dịch vụ đặt lịch khám bệnh trực tuyến.\n\n" +
                
                "2. **Quyền và nghĩa vụ của người dùng**\n" +
                "- Cung cấp thông tin chính xác, đầy đủ\n" +
                "- Tuân thủ lịch hẹn đã đặt\n" +
                "- Thanh toán đầy đủ theo quy định\n" +
                "- Không sử dụng dịch vụ cho mục đích bất hợp pháp\n\n" +
                
                "3. **Quyền và nghĩa vụ của nhà cung cấp**\n" +
                "- Cung cấp dịch vụ chất lượng\n" +
                "- Bảo mật thông tin người dùng\n" +
                "- Hỗ trợ kỹ thuật khi cần thiết\n" +
                "- Từ chối dịch vụ nếu phát hiện vi phạm\n\n" +
                
                "4. **Chính sách hủy và hoàn tiền**\n" +
                "- Hủy trước 24h: hoàn 100% phí\n" +
                "- Hủy trước 2h: hoàn 50% phí\n" +
                "- Hủy trong 2h: không hoàn phí\n\n" +
                
                "**CHÍNH SÁCH BẢO MẬT**\n\n" +
                
                "1. **Thu thập thông tin**\n" +
                "Chúng tôi chỉ thu thập thông tin cần thiết cho việc cung cấp dịch vụ y tế.\n\n" +
                
                "2. **Sử dụng thông tin**\n" +
                "- Cung cấp dịch vụ tư vấn y tế\n" +
                "- Liên hệ xác nhận lịch hẹn\n" +
                "- Cải thiện chất lượng dịch vụ\n" +
                "- Tuân thủ quy định pháp luật\n\n" +
                
                "3. **Bảo vệ thông tin**\n" +
                "- Mã hóa dữ liệu nhạy cảm\n" +
                "- Hạn chế quyền truy cập\n" +
                "- Kiểm tra bảo mật định kỳ\n" +
                "- Tuân thủ chuẩn HIPAA\n\n" +
                
                "4. **Quyền của người dùng**\n" +
                "- Xem và cập nhật thông tin cá nhân\n" +
                "- Yêu cầu xóa dữ liệu\n" +
                "- Từ chối nhận thông tin marketing\n" +
                "- Khiếu nại về việc xử lý dữ liệu\n\n" +
                
                "5. **Liên hệ**\n" +
                "Mọi thắc mắc về điều khoản và chính sách, vui lòng liên hệ:\n" +
                "📧 Email: privacy@healthapp.com\n" +
                "📞 Hotline: 1900-xxxx\n\n" +
                
                "Cập nhật lần cuối: " + getCurrentDate();
        
        tvContent.setText(termsContent);
    }

    private void setupListeners() {
        btnAccept.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAccept();
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

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }

    public void setOnTermsActionListener(OnTermsActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
            dialog.getWindow().setLayout(width, height);
        }
    }
} 