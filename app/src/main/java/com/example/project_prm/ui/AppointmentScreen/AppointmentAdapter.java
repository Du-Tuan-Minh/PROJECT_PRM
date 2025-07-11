// NEW FILE: app/src/main/java/com/example/project_prm/UI/AppointmentScreen/AppointmentAdapter.java
package com.example.project_prm.ui.AppointmentScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.AppointmentStatus;
import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private final List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList;
    private final OnAppointmentActionListener listener;

    public interface OnAppointmentActionListener {
        void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onBookAgainClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onLeaveReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onContactClick(AppointmentHistoryManager.AppointmentHistoryItem item);
    }

    public AppointmentAdapter(List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList,
                              OnAppointmentActionListener listener) {
        this.appointmentList = appointmentList;
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
        AppointmentHistoryManager.AppointmentHistoryItem item = appointmentList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivDoctorAvatar;
        private final TextView tvDoctorName;
        private final TextView tvSpecialty;
        private final TextView tvStatus;
        private final TextView tvAppointmentDateTime;
        private final MaterialButton btnActionPrimary;
        private final MaterialButton btnActionSecondary;
        private final MaterialButton btnActionMain;
        private final ImageView ivContactIcon;
        private final ImageView ivVideoIcon;
        private final ImageView ivMessageIcon;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDoctorAvatar = itemView.findViewById(R.id.iv_doctor_avatar);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvSpecialty = itemView.findViewById(R.id.tv_specialty);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvAppointmentDateTime = itemView.findViewById(R.id.tv_appointment_datetime);
            btnActionPrimary = itemView.findViewById(R.id.btn_action_primary);
            btnActionSecondary = itemView.findViewById(R.id.btn_action_secondary);
            btnActionMain = itemView.findViewById(R.id.btn_action_main);
            ivContactIcon = itemView.findViewById(R.id.iv_contact_icon);
            ivVideoIcon = itemView.findViewById(R.id.iv_video_icon);
            ivMessageIcon = itemView.findViewById(R.id.iv_message_icon);
        }

        public void bind(AppointmentHistoryManager.AppointmentHistoryItem item, OnAppointmentActionListener listener) {
            // Set doctor info
            tvDoctorName.setText(item.appointment.getDoctor());

            // Set specialty based on appointment type
            String specialty = getSpecialtyDisplay(item.appointment.getAppointmentType());
            tvSpecialty.setText(specialty);

            // Set status
            tvStatus.setText(item.status.getDisplayName());
            setStatusChipStyle(tvStatus, item.status);

            // Set date and time
            String dateTime = formatDateTime(item.appointment.getDate(), item.appointment.getTime());
            tvAppointmentDateTime.setText(dateTime);

            // Configure UI based on appointment status
            configureUIForStatus(item, listener);

            // Set communication icons based on appointment type
            configureCommIcons(item.appointment.getAppointmentType());
        }

        private void configureUIForStatus(AppointmentHistoryManager.AppointmentHistoryItem item, OnAppointmentActionListener listener) {
            AppointmentStatus status = item.status;

            // Hide all initially
            btnActionPrimary.setVisibility(View.GONE);
            ivContactIcon.setVisibility(View.GONE);

            switch (status) {
                case UPCOMING:
                case CONFIRMED:
                    // Show Cancel and Reschedule buttons
                    btnActionSecondary.setText("Cancel Appointment");
                    btnActionSecondary.setVisibility(View.VISIBLE);
                    btnActionSecondary.setOnClickListener(v -> listener.onCancelClick(item));

                    btnActionMain.setText("Reschedule");
                    btnActionMain.setVisibility(View.VISIBLE);
                    btnActionMain.setOnClickListener(v -> listener.onRescheduleClick(item));
                    break;

                case COMPLETED:
                    // Show Book Again and Leave a Review buttons
                    btnActionSecondary.setText("Book Again");
                    btnActionSecondary.setVisibility(View.VISIBLE);
                    btnActionSecondary.setOnClickListener(v -> listener.onBookAgainClick(item));

                    btnActionMain.setText("Leave a Review");
                    btnActionMain.setVisibility(View.VISIBLE);
                    btnActionMain.setOnClickListener(v -> listener.onLeaveReviewClick(item));
                    break;

                case CANCELLED:
                    // Show Contact button (top right)
                    btnActionPrimary.setText("Contact");
                    btnActionPrimary.setVisibility(View.VISIBLE);
                    btnActionPrimary.setOnClickListener(v -> listener.onContactClick(item));

                    // Hide bottom buttons
                    btnActionSecondary.setVisibility(View.GONE);
                    btnActionMain.setVisibility(View.GONE);
                    break;

                case PENDING:
                    // Show Cancel button only
                    btnActionSecondary.setText("Cancel");
                    btnActionSecondary.setVisibility(View.VISIBLE);
                    btnActionSecondary.setOnClickListener(v -> listener.onCancelClick(item));

                    btnActionMain.setVisibility(View.GONE);
                    break;

                default:
                    btnActionSecondary.setVisibility(View.GONE);
                    btnActionMain.setVisibility(View.GONE);
                    break;
            }
        }

        private void configureCommIcons(String appointmentType) {
            // Hide all icons initially
            ivVideoIcon.setVisibility(View.GONE);
            ivMessageIcon.setVisibility(View.GONE);
            ivContactIcon.setVisibility(View.GONE);

            // Show appropriate icon based on appointment type
            if (appointmentType != null) {
                switch (appointmentType.toLowerCase()) {
                    case "video call":
                        ivVideoIcon.setVisibility(View.VISIBLE);
                        break;
                    case "messaging":
                        ivMessageIcon.setVisibility(View.VISIBLE);
                        break;
                    case "voice call":
                        ivContactIcon.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        private String getSpecialtyDisplay(String appointmentType) {
            if (appointmentType == null) return "General";

            switch (appointmentType.toLowerCase()) {
                case "consultation": return "Consultation";
                case "checkup": return "Health Checkup";
                case "followup": return "Follow-up";
                case "specialist": return "Specialist";
                case "emergency": return "Emergency";
                default: return "Voice Call"; // Default from design
            }
        }

        private void setStatusChipStyle(TextView statusView, AppointmentStatus status) {
            int backgroundColor, textColor;

            switch (status) {
                case UPCOMING:
                case CONFIRMED:
                    backgroundColor = ContextCompat.getColor(itemView.getContext(), R.color.primary_blue);
                    textColor = ContextCompat.getColor(itemView.getContext(), R.color.white);
                    break;
                case COMPLETED:
                    backgroundColor = ContextCompat.getColor(itemView.getContext(), R.color.success_green);
                    textColor = ContextCompat.getColor(itemView.getContext(), R.color.white);
                    break;
                case CANCELLED:
                    backgroundColor = ContextCompat.getColor(itemView.getContext(), R.color.error_red);
                    textColor = ContextCompat.getColor(itemView.getContext(), R.color.white);
                    break;
                case PENDING:
                    backgroundColor = ContextCompat.getColor(itemView.getContext(), R.color.warning_orange);
                    textColor = ContextCompat.getColor(itemView.getContext(), R.color.white);
                    break;
                default:
                    backgroundColor = ContextCompat.getColor(itemView.getContext(), R.color.text_gray);
                    textColor = ContextCompat.getColor(itemView.getContext(), R.color.white);
                    break;
            }

            statusView.setBackgroundTintList(android.content.res.ColorStateList.valueOf(backgroundColor));
            statusView.setTextColor(textColor);
        }

        private String formatDateTime(String date, String time) {
            // Convert date format from yyyy-MM-dd to readable format
            try {
                String[] dateParts = date.split("-");
                if (dateParts.length == 3) {
                    int month = Integer.parseInt(dateParts[1]);
                    int day = Integer.parseInt(dateParts[2]);
                    int year = Integer.parseInt(dateParts[0]);

                    String[] monthNames = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                    return monthNames[month] + " " + day + ", " + year + " | " + time;
                }
            } catch (Exception e) {
                // Fall back to original format if parsing fails
            }

            return date + " | " + time;
        }
    }
}