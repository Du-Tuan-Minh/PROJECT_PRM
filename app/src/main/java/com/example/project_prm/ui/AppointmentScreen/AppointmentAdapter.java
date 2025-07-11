package com.example.project_prm.ui.AppointmentScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.AppointmentStatus;
import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    // Appointment types for different fragments
    public static final int TYPE_UPCOMING = 1;
    public static final int TYPE_COMPLETED = 2;
    public static final int TYPE_CANCELLED = 3;

    private List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList;
    private int appointmentType;
    private OnActionClickListener actionClickListener;
    private Context context;

    public AppointmentAdapter(List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList, int appointmentType) {
        this.appointmentList = appointmentList;
        this.appointmentType = appointmentType;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_history, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        AppointmentHistoryManager.AppointmentHistoryItem item = appointmentList.get(position);
        holder.bind(item, appointmentType, actionClickListener);
    }

    @Override
    public int getItemCount() {
        return appointmentList != null ? appointmentList.size() : 0;
    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.actionClickListener = listener;
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAppointmentStatus;
        private TextView tvClinicName;
        private TextView tvDoctorName;
        private TextView tvAppointmentType;
        private TextView tvAppointmentDate;
        private TextView tvAppointmentTime;
        private TextView tvAppointmentFee;
        private TextView tvPatientInfo;

        // Action buttons
        private MaterialButton btnCancel;
        private MaterialButton btnReschedule;
        private MaterialButton btnBookAgain;
        private MaterialButton btnReview;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppointmentStatus = itemView.findViewById(R.id.tv_appointment_status);
            tvClinicName = itemView.findViewById(R.id.tv_clinic_name);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvAppointmentType = itemView.findViewById(R.id.tv_appointment_type);
            tvAppointmentDate = itemView.findViewById(R.id.tv_appointment_date);
            tvAppointmentTime = itemView.findViewById(R.id.tv_appointment_time);
            tvAppointmentFee = itemView.findViewById(R.id.tv_appointment_fee);
            tvPatientInfo = itemView.findViewById(R.id.tv_patient_info);

            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnReschedule = itemView.findViewById(R.id.btn_reschedule);
            btnBookAgain = itemView.findViewById(R.id.btn_book_again);
            btnReview = itemView.findViewById(R.id.btn_review);
        }

        public void bind(AppointmentHistoryManager.AppointmentHistoryItem item, int type, OnActionClickListener listener) {
            // Basic appointment info
            tvClinicName.setText(item.appointment.getClinic());
            tvDoctorName.setText("BS. " + item.appointment.getDoctor());

            // Appointment type with fallback
            String appointmentType = item.appointment.getAppointmentType();
            if (appointmentType == null || appointmentType.isEmpty()) {
                appointmentType = "Khám tổng quát";
            }
            tvAppointmentType.setText(appointmentType);

            // Format date and time
            tvAppointmentDate.setText(formatDate(item.appointment.getDate()));
            tvAppointmentTime.setText(item.appointment.getTime());

            // Format fee
            tvAppointmentFee.setText(formatCurrency(item.appointment.getAppointmentFee()));

            // Patient info (if different from user)
            if (item.appointment.getPatientName() != null && !item.appointment.getPatientName().isEmpty()) {
                String patientInfo = String.format("Bệnh nhân: %s, %s tuổi",
                        item.appointment.getPatientName(),
                        item.appointment.getPatientAge() != null ? item.appointment.getPatientAge() : "N/A");
                tvPatientInfo.setText(patientInfo);
                tvPatientInfo.setVisibility(View.VISIBLE);
            } else {
                tvPatientInfo.setVisibility(View.GONE);
            }

            // Status and actions based on appointment type
            setupStatusAndActions(item, type, listener);

            // Item click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }

        private void setupStatusAndActions(AppointmentHistoryManager.AppointmentHistoryItem item, int type, OnActionClickListener listener) {
            // Hide all buttons first
            btnCancel.setVisibility(View.GONE);
            btnReschedule.setVisibility(View.GONE);
            btnBookAgain.setVisibility(View.GONE);
            btnReview.setVisibility(View.GONE);

            switch (type) {
                case TYPE_UPCOMING:
                    tvAppointmentStatus.setText("Sắp tới");
                    tvAppointmentStatus.setBackgroundTintList(
                            itemView.getContext().getColorStateList(R.color.primary_blue));

                    // Show cancel and reschedule buttons
                    btnCancel.setVisibility(View.VISIBLE);
                    btnReschedule.setVisibility(View.VISIBLE);

                    btnCancel.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onCancelClick(item);
                        }
                    });

                    btnReschedule.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onRescheduleClick(item);
                        }
                    });
                    break;

                case TYPE_COMPLETED:
                    tvAppointmentStatus.setText("Hoàn thành");
                    tvAppointmentStatus.setBackgroundTintList(
                            itemView.getContext().getColorStateList(R.color.success_green));

                    // Show book again and review buttons
                    btnBookAgain.setVisibility(View.VISIBLE);

                    // Show review button only if not reviewed yet
                    if (item.appointment.getRating() == 0) {
                        btnReview.setVisibility(View.VISIBLE);
                    }

                    btnBookAgain.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onBookAgainClick(item);
                        }
                    });

                    btnReview.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onReviewClick(item);
                        }
                    });
                    break;

                case TYPE_CANCELLED:
                    tvAppointmentStatus.setText("Đã hủy");
                    tvAppointmentStatus.setBackgroundTintList(
                            itemView.getContext().getColorStateList(R.color.error_red));

                    // Show book again button
                    btnBookAgain.setVisibility(View.VISIBLE);

                    btnBookAgain.setOnClickListener(v -> {
                        if (listener != null) {
                            listener.onBookAgainClick(item);
                        }
                    });
                    break;
            }
        }

        private String formatDate(String dateString) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                return outputFormat.format(inputFormat.parse(dateString));
            } catch (Exception e) {
                return dateString;
            }
        }

        private String formatCurrency(double amount) {
            if (amount <= 0) {
                return "Miễn phí";
            }
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            return formatter.format(amount) + "đ";
        }
    }

    // Interface for handling button clicks
    public interface OnActionClickListener {
        void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onBookAgainClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onItemClick(AppointmentHistoryManager.AppointmentHistoryItem item);
    }
}