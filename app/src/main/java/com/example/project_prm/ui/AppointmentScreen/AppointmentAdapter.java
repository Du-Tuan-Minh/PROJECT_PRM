package com.example.project_prm.ui.AppointmentScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying appointment history items in RecyclerView
 * Supports different types: Upcoming, Completed, Cancelled
 * Each type has different action buttons and status display
 */
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

    /**
     * Update adapter data and refresh
     */
    public void updateData(List<AppointmentHistoryManager.AppointmentHistoryItem> newData) {
        this.appointmentList = newData;
        notifyDataSetChanged();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        // Text Views
        private TextView tvAppointmentStatus;
        private TextView tvClinicName;
        private TextView tvDoctorName;
        private TextView tvSpecialty;
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

            // Initialize text views
            tvAppointmentStatus = itemView.findViewById(R.id.tv_appointment_status);
            tvClinicName = itemView.findViewById(R.id.tv_clinic_name);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvSpecialty = itemView.findViewById(R.id.tv_specialty);
            tvAppointmentType = itemView.findViewById(R.id.tv_appointment_type);
            tvAppointmentDate = itemView.findViewById(R.id.tv_appointment_date);
            tvAppointmentTime = itemView.findViewById(R.id.tv_appointment_time);
            tvAppointmentFee = itemView.findViewById(R.id.tv_appointment_fee);
            tvPatientInfo = itemView.findViewById(R.id.tv_patient_info);

            // Initialize action buttons
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnReschedule = itemView.findViewById(R.id.btn_reschedule);
            btnBookAgain = itemView.findViewById(R.id.btn_book_again);
            btnReview = itemView.findViewById(R.id.btn_review);
        }

        public void bind(AppointmentHistoryManager.AppointmentHistoryItem item, int type, OnActionClickListener listener) {
            // Basic appointment info - using direct properties from AppointmentHistoryItem
            if (tvClinicName != null) {
                tvClinicName.setText(item.clinicName != null ? item.clinicName : "Phòng khám");
            }

            if (tvDoctorName != null) {
                tvDoctorName.setText(item.doctorName != null ? "BS. " + item.doctorName : "Bác sĩ");
            }

            if (tvSpecialty != null) {
                tvSpecialty.setText(item.specialty != null ? item.specialty : "Tổng quát");
            }

            // Appointment type with fallback
            if (tvAppointmentType != null) {
                String appointmentType = item.packageType;
                if (appointmentType == null || appointmentType.isEmpty()) {
                    appointmentType = "Khám tổng quát";
                }
                tvAppointmentType.setText(appointmentType);
            }

            // Format date and time
            if (tvAppointmentDate != null) {
                tvAppointmentDate.setText(formatDate(item.appointmentDate));
            }

            if (tvAppointmentTime != null) {
                tvAppointmentTime.setText(item.appointmentTime != null ? item.appointmentTime : "N/A");
            }

            // Format fee
            if (tvAppointmentFee != null) {
                tvAppointmentFee.setText(formatCurrency(item.fee));
            }

            // Patient info - for now hide since we don't have patient data in history item
            if (tvPatientInfo != null) {
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
            hideAllButtons();

            switch (type) {
                case TYPE_UPCOMING:
                    setupUpcomingAppointment(item, listener);
                    break;

                case TYPE_COMPLETED:
                    setupCompletedAppointment(item, listener);
                    break;

                case TYPE_CANCELLED:
                    setupCancelledAppointment(item, listener);
                    break;
            }
        }

        private void hideAllButtons() {
            if (btnCancel != null) btnCancel.setVisibility(View.GONE);
            if (btnReschedule != null) btnReschedule.setVisibility(View.GONE);
            if (btnBookAgain != null) btnBookAgain.setVisibility(View.GONE);
            if (btnReview != null) btnReview.setVisibility(View.GONE);
        }

        private void setupUpcomingAppointment(AppointmentHistoryManager.AppointmentHistoryItem item, OnActionClickListener listener) {
            if (tvAppointmentStatus != null) {
                tvAppointmentStatus.setText("Sắp tới");
                try {
                    tvAppointmentStatus.setBackgroundTintList(
                            itemView.getContext().getColorStateList(R.color.primary_blue));
                } catch (Exception e) {
                    // Fallback if color resource not found
                }
            }

            // Show cancel and reschedule buttons
            if (btnCancel != null) {
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onCancelClick(item);
                    }
                });
            }

            if (btnReschedule != null) {
                btnReschedule.setVisibility(View.VISIBLE);
                btnReschedule.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onRescheduleClick(item);
                    }
                });
            }
        }

        private void setupCompletedAppointment(AppointmentHistoryManager.AppointmentHistoryItem item, OnActionClickListener listener) {
            if (tvAppointmentStatus != null) {
                tvAppointmentStatus.setText("Hoàn thành");
                try {
                    tvAppointmentStatus.setBackgroundTintList(
                            itemView.getContext().getColorStateList(R.color.success_green));
                } catch (Exception e) {
                    // Fallback if color resource not found
                }
            }

            // Show book again button
            if (btnBookAgain != null) {
                btnBookAgain.setVisibility(View.VISIBLE);
                btnBookAgain.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onBookAgainClick(item);
                    }
                });
            }

            // Show review button only if not reviewed yet
            if (btnReview != null && item.rating == 0) {
                btnReview.setVisibility(View.VISIBLE);
                btnReview.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onReviewClick(item);
                    }
                });
            }
        }

        private void setupCancelledAppointment(AppointmentHistoryManager.AppointmentHistoryItem item, OnActionClickListener listener) {
            if (tvAppointmentStatus != null) {
                tvAppointmentStatus.setText("Đã hủy");
                try {
                    tvAppointmentStatus.setBackgroundTintList(
                            itemView.getContext().getColorStateList(R.color.error_red));
                } catch (Exception e) {
                    // Fallback if color resource not found
                }
            }

            // Show book again button
            if (btnBookAgain != null) {
                btnBookAgain.setVisibility(View.VISIBLE);
                btnBookAgain.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onBookAgainClick(item);
                    }
                });
            }
        }

        private String formatDate(String dateString) {
            if (dateString == null || dateString.isEmpty()) {
                return "N/A";
            }

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                return outputFormat.format(inputFormat.parse(dateString));
            } catch (Exception e) {
                return dateString; // Return original if parsing fails
            }
        }

        private String formatCurrency(double amount) {
            if (amount <= 0) {
                return "Miễn phí";
            }
            try {
                NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                return formatter.format(amount) + "đ";
            } catch (Exception e) {
                return String.valueOf((int)amount) + "đ"; // Fallback formatting
            }
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