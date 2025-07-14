package com.example.project_prm.ui.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

    public AppointmentAdapter(List<AppointmentModel> appointments) {
        this.appointments = appointments;
    }

    public void setOnAppointmentActionListener(OnAppointmentActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        holder.bind(appointments.get(position));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void updateAppointments(List<AppointmentModel> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDoctorName, tvSpecialty, tvDateTime, tvPackage, tvStatus, tvPrice;
        private ImageView ivPackageIcon, ivDoctorAvatar;
        private MaterialButton btnPrimary, btnSecondary;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvPackage = itemView.findViewById(R.id.tvPackage);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivPackageIcon = itemView.findViewById(R.id.ivPackageIcon);
            ivDoctorAvatar = itemView.findViewById(R.id.ivDoctorAvatar);
            btnPrimary = itemView.findViewById(R.id.btnPrimary);
            btnSecondary = itemView.findViewById(R.id.btnSecondary);
        }

        public void bind(AppointmentModel appointment) {
            // Basic info
            tvDoctorName.setText(appointment.getDoctorName());
            tvSpecialty.setText(appointment.getSpecialty());
            tvDateTime.setText(appointment.getDate() + " • " + appointment.getTime());
            tvPackage.setText(appointment.getPackageType());
            tvPrice.setText(String.format("%,d đ", appointment.getAmount()));

            // Status
            setStatusDisplay(appointment.getStatus());
            
            // Package icon
            setPackageIcon(appointment.getPackageType());
            
            // Setup buttons based on status and review status
            setupButtons(appointment);
            
            // Click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAppointmentClick(appointment);
                }
            });
        }
        
        private void setStatusDisplay(String status) {
            switch (status.toLowerCase()) {
                case "upcoming":
                    tvStatus.setText("Sắp tới");
                    tvStatus.setTextColor(itemView.getContext().getColor(R.color.orange));
                    break;
                case "completed":
                    tvStatus.setText("Đã hoàn thành");
                    tvStatus.setTextColor(itemView.getContext().getColor(R.color.green));
                    break;
                case "cancelled":
                    tvStatus.setText("Đã hủy");
                    tvStatus.setTextColor(itemView.getContext().getColor(R.color.red));
                    break;
                default:
                    tvStatus.setText("Chờ xác nhận");
                    tvStatus.setTextColor(itemView.getContext().getColor(R.color.text_gray));
                    break;
            }
        }
        
        private void setPackageIcon(String packageType) {
            int iconRes;
            switch (packageType) {
                case "Nhắn tin":
                    iconRes = R.drawable.ic_message;
                    break;
                case "Gọi thoại":
                case "Cuộc gọi thoại":
                    iconRes = R.drawable.ic_phone;
                    break;
                case "Gọi video":
                case "Cuộc gọi video":
                    iconRes = R.drawable.ic_video;
                    break;
                case "Khám trực tiếp":
                    iconRes = R.drawable.ic_hospital;
                    break;
                default:
                    iconRes = R.drawable.ic_message;
                    break;
            }
            ivPackageIcon.setImageResource(iconRes);
        }
        
        private void setupButtons(AppointmentModel appointment) {
            // Reset buttons
            btnPrimary.setVisibility(View.VISIBLE);
            btnSecondary.setVisibility(View.VISIBLE);
            
            switch (appointment.getStatus().toLowerCase()) {
                case "upcoming":
                    setupUpcomingButtons(appointment);
                    break;
                case "completed":
                    setupCompletedButtons(appointment);
                    break;
                case "cancelled":
                    setupCancelledButtons(appointment);
                    break;
                default:
                    // Hide buttons for pending/other statuses
                    btnPrimary.setVisibility(View.GONE);
                    btnSecondary.setVisibility(View.GONE);
                    break;
            }
        }

        private void setupUpcomingButtons(AppointmentModel appointment) {
            // Primary: Reschedule
            btnPrimary.setText("Đổi lịch");
            btnPrimary.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRescheduleAppointment(appointment);
                }
            });

            // Secondary: Cancel
            btnSecondary.setText("Hủy lịch");
            btnSecondary.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelAppointment(appointment);
                }
            });
        }

        private void setupCompletedButtons(AppointmentModel appointment) {
            // Primary: Book Again
            btnPrimary.setText("Đặt lịch lại");
            btnPrimary.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookAgain(appointment);
                }
            });

            // Secondary: Leave Review (ONLY if not reviewed yet)
            if (appointment.isReviewed()) {
                // Already reviewed - show "Đã đánh giá"
                btnSecondary.setText("Đã đánh giá");
                btnSecondary.setEnabled(false);
                btnSecondary.setAlpha(0.6f);
                btnSecondary.setOnClickListener(null);
            } else {
                // Not reviewed yet - show "Viết đánh giá"
                btnSecondary.setText("Viết đánh giá");
                btnSecondary.setEnabled(true);
                btnSecondary.setAlpha(1.0f);
                btnSecondary.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onLeaveReview(appointment);
                    }
                });
            }
        }

        private void setupCancelledButtons(AppointmentModel appointment) {
            // Primary: Book Again
            btnPrimary.setText("Đặt lịch lại");
            btnPrimary.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookAgain(appointment);
                }
            });

            // Hide secondary button for cancelled appointments
            btnSecondary.setVisibility(View.GONE);
        }
    }
} 