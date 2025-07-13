package com.example.project_prm.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    
    private List<AppointmentModel> appointments;
    private OnAppointmentActionListener listener;
    
    public interface OnAppointmentActionListener {
        void onAppointmentClick(AppointmentModel appointment);
        void onCancelAppointment(AppointmentModel appointment);
        void onRescheduleAppointment(AppointmentModel appointment);
        void onBookAgain(AppointmentModel appointment);
        void onLeaveReview(AppointmentModel appointment);
    }
    
    public AppointmentAdapter(List<AppointmentModel> appointments, OnAppointmentActionListener listener) {
        this.appointments = appointments;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentModel appointment = appointments.get(position);
        holder.bind(appointment);
    }
    
    @Override
    public int getItemCount() {
        return appointments.size();
    }
    
    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        
        private ImageView ivDoctorAvatar, ivPackageIcon;
        private TextView tvDoctorName, tvDoctorSpecialty, tvDateTime, tvStatus, tvPackageType;
        private MaterialButton btnPrimary, btnSecondary;
        private LinearLayout layoutButtons;
        
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ivDoctorAvatar = itemView.findViewById(R.id.ivDoctorAvatar);
            ivPackageIcon = itemView.findViewById(R.id.ivPackageIcon);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDoctorSpecialty = itemView.findViewById(R.id.tvDoctorSpecialty);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPackageType = itemView.findViewById(R.id.tvPackageType);
            btnPrimary = itemView.findViewById(R.id.btnPrimary);
            btnSecondary = itemView.findViewById(R.id.btnSecondary);
            layoutButtons = itemView.findViewById(R.id.layoutButtons);
        }
        
        public void bind(AppointmentModel appointment) {
            // Set basic info
            tvDoctorName.setText(appointment.getDoctorName());
            tvDoctorSpecialty.setText(appointment.getDoctorSpecialty());
            tvDateTime.setText("Hôm nay, " + appointment.getDate() + " | " + appointment.getTime());
            tvStatus.setText(appointment.getStatusText());
            tvPackageType.setText(appointment.getPackageType());
            
            // Set status color
            tvStatus.setTextColor(itemView.getContext().getResources().getColor(appointment.getStatusColor()));
            
            // Load avatar
            Glide.with(itemView.getContext())
                .load(R.drawable.ic_general)
                .circleCrop()
                .into(ivDoctorAvatar);
            
            // Set package icon
            setPackageIcon(appointment.getPackageType());
            
            // Setup buttons based on status
            setupButtons(appointment);
            
            // Click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAppointmentClick(appointment);
                }
            });
        }
        
        private void setPackageIcon(String packageType) {
            int iconRes;
            switch (packageType) {
                case "Nhắn tin":
                    iconRes = R.drawable.ic_message;
                    break;
                case "Cuộc gọi thoại":
                    iconRes = R.drawable.ic_phone;
                    break;
                case "Cuộc gọi video":
                    iconRes = R.drawable.ic_video;
                    break;
                default:
                    iconRes = R.drawable.ic_message;
                    break;
            }
            ivPackageIcon.setImageResource(iconRes);
        }
        
        private void setupButtons(AppointmentModel appointment) {
            switch (appointment.getStatus()) {
                case "upcoming":
                    btnSecondary.setText("Hủy lịch hẹn");
                    btnPrimary.setText("Đổi lịch");
                    
                    btnSecondary.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onCancelAppointment(appointment);
                        }
                    });
                    
                    btnPrimary.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onRescheduleAppointment(appointment);
                        }
                    });
                    break;
                    
                case "completed":
                    btnSecondary.setText("Đặt lịch lại");
                    
                    // Kiểm tra xem đã có review chưa
                    if (appointment.hasReview()) {
                        btnPrimary.setText("Đã đánh giá");
                        btnPrimary.setEnabled(false);
                        btnPrimary.setAlpha(0.5f);
                    } else {
                        btnPrimary.setText("Viết đánh giá");
                        btnPrimary.setEnabled(true);
                        btnPrimary.setAlpha(1.0f);
                    }
                    
                    btnSecondary.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onBookAgain(appointment);
                        }
                    });
                    
                    btnPrimary.setOnClickListener(v -> {
                        if (listener != null && !appointment.hasReview()) {
                            listener.onLeaveReview(appointment);
                        }
                    });
                    break;
                    
                case "cancelled":
                    // Only show one button for cancelled
                    btnSecondary.setVisibility(View.GONE);
                    btnPrimary.setText("Đặt lịch lại");
                    
                    // Adjust layout for single button
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btnPrimary.getLayoutParams();
                    params.weight = 0;
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    btnPrimary.setLayoutParams(params);
                    
                    btnPrimary.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onBookAgain(appointment);
                        }
                    });
                    break;
            }
        }
    }
} 