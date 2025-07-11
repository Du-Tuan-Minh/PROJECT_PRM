package com.example.project_prm.ui.AppointmentScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.AppointmentStatus;
import com.example.project_prm.DataManager.Entity.Doctor;
import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.DataManager.Repository.DoctorRepository;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList;
    private OnAppointmentActionListener listener;
    private DoctorRepository doctorRepository;

    public interface OnAppointmentActionListener {
        void onCancelClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onRescheduleClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onBookAgainClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onLeaveReviewClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onContactClick(AppointmentHistoryManager.AppointmentHistoryItem item);
        void onItemClick(AppointmentHistoryManager.AppointmentHistoryItem item);
    }

    public AppointmentAdapter(List<AppointmentHistoryManager.AppointmentHistoryItem> appointmentList,
                              OnAppointmentActionListener listener) {
        this.appointmentList = appointmentList;
        this.listener = listener;
        this.doctorRepository = DoctorRepository.getInstance();
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
        holder.bind(item, listener, doctorRepository);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivDoctorAvatar, ivVideoIcon, ivMessageIcon, ivContactIcon;
        private TextView tvDoctorName, tvSpecialty, tvAppointmentDateTime, tvStatusChip;
        private MaterialButton btnActionMain, btnActionSecondary, btnActionPrimary;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            ivDoctorAvatar = itemView.findViewById(R.id.iv_doctor_avatar);
            ivVideoIcon = itemView.findViewById(R.id.iv_video_icon);
            ivMessageIcon = itemView.findViewById(R.id.iv_message_icon);
            ivContactIcon = itemView.findViewById(R.id.iv_contact_icon);

            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvSpecialty = itemView.findViewById(R.id.tv_specialty);
            tvAppointmentDateTime = itemView.findViewById(R.id.tv_appointment_date_time);
            tvStatusChip = itemView.findViewById(R.id.tv_status_chip);

            btnActionMain = itemView.findViewById(R.id.btn_action_main);
            btnActionSecondary = itemView.findViewById(R.id.btn_action_secondary);
            btnActionPrimary = itemView.findViewById(R.id.btn_action_primary);
        }

        public void bind(AppointmentHistoryManager.AppointmentHistoryItem item,
                         OnAppointmentActionListener listener,
                         DoctorRepository doctorRepository) {

            // Get doctor information from repository
            Doctor doctor = doctorRepository.getDoctorByName(item.appointment.getDoctor());

            // Set doctor info
            tvDoctorName.setText(item.appointment.getDoctor());

            if (doctor != null) {
                // Use real doctor specialty
                tvSpecialty.setText(doctor.getSpecialty());

                // Set doctor avatar based on doctor data
                setDoctorAvatar(doctor);
            } else {
                // Fallback to appointment type based specialty
                tvSpecialty.setText(getSpecialtyFromAppointmentType(item.appointment.getAppointmentType()));
                ivDoctorAvatar.setImageResource(R.drawable.default_doctor_avatar);
            }

            // Set status chip
            setStatusChipStyle(tvStatusChip, item.status);

            // Format and set date time
            String dateTime = formatDateTime(item.appointment.getDate(), item.appointment.getTime());
            tvAppointmentDateTime.setText(dateTime);

            // Configure UI based on appointment status
            configureUIForStatus(item, listener);

            // Set communication icons based on appointment type
            configureCommIcons(item.appointment.getAppointmentType());

            // Set item click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }

        private void setDoctorAvatar(Doctor doctor) {
            // You can implement avatar loading logic here
            // For now, use specialty-based avatars or default
            String avatarResource = doctor.getAvatarResource();

            if (avatarResource != null) {
                // Try to get resource ID by name
                int resourceId = itemView.getContext().getResources().getIdentifier(
                        avatarResource, "drawable", itemView.getContext().getPackageName());

                if (resourceId != 0) {
                    ivDoctorAvatar.setImageResource(resourceId);
                } else {
                    // Use specialty-based avatar
                    setAvatarBySpecialty(doctor.getSpecialtyCode());
                }
            } else {
                setAvatarBySpecialty(doctor.getSpecialtyCode());
            }
        }

        private void setAvatarBySpecialty(String specialtyCode) {
            int avatarResource;
            switch (specialtyCode.toLowerCase()) {
                case "dermatology":
                    avatarResource = R.drawable.avatar_dermatologist;
                    break;
                case "neurology":
                    avatarResource = R.drawable.avatar_neurologist;
                    break;
                case "cardiology":
                    avatarResource = R.drawable.avatar_cardiologist;
                    break;
                case "pediatrics":
                    avatarResource = R.drawable.avatar_pediatrician;
                    break;
                case "orthopedics":
                    avatarResource = R.drawable.avatar_orthopedist;
                    break;
                case "gastroenterology":
                    avatarResource = R.drawable.avatar_gastroenterologist;
                    break;
                default:
                    avatarResource = R.drawable.default_doctor_avatar;
                    break;
            }
            ivDoctorAvatar.setImageResource(avatarResource);
        }

        private void configureUIForStatus(AppointmentHistoryManager.AppointmentHistoryItem item, OnAppointmentActionListener listener) {
            AppointmentStatus status = item.status;

            // Hide all initially
            btnActionPrimary.setVisibility(View.GONE);
            btnActionSecondary.setVisibility(View.GONE);
            btnActionMain.setVisibility(View.GONE);

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

        private String getSpecialtyFromAppointmentType(String appointmentType) {
            // Fallback method when doctor not found in repository
            if (appointmentType == null) return "General Practice";

            switch (appointmentType.toLowerCase()) {
                case "messaging":
                    return "Dermatologist";
                case "video call":
                    return "Neurologist";
                case "voice call":
                    return "Cardiologist";
                default:
                    return "General Practice";
            }
        }

        private void setStatusChipStyle(TextView statusView, AppointmentStatus status) {
            int backgroundColor, textColor;

            switch (status) {
                case UPCOMING:
                case CONFIRMED:
                    backgroundColor = R.color.status_upcoming_bg;
                    textColor = R.color.status_upcoming_text;
                    statusView.setText("Upcoming");
                    break;
                case COMPLETED:
                    backgroundColor = R.color.status_completed_bg;
                    textColor = R.color.status_completed_text;
                    statusView.setText("Completed");
                    break;
                case CANCELLED:
                    backgroundColor = R.color.status_cancelled_bg;
                    textColor = R.color.status_cancelled_text;
                    statusView.setText("Cancelled");
                    break;
                case PENDING:
                    backgroundColor = R.color.status_pending_bg;
                    textColor = R.color.status_pending_text;
                    statusView.setText("Pending");
                    break;
                default:
                    backgroundColor = R.color.status_default_bg;
                    textColor = R.color.status_default_text;
                    statusView.setText("Unknown");
                    break;
            }

            statusView.setBackgroundResource(backgroundColor);
            statusView.setTextColor(itemView.getContext().getColor(textColor));
        }

        private String formatDateTime(String date, String time) {
            try {
                // Parse the date
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date dateObj = inputDateFormat.parse(date);

                // Check if it's today
                SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String today = todayFormat.format(new Date());

                String displayDate;
                if (date.equals(today)) {
                    displayDate = "Today";
                } else {
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    displayDate = outputDateFormat.format(dateObj);
                }

                // Format time (assuming time is in HH:mm format)
                SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Date timeObj = inputTimeFormat.parse(time);
                SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String formattedTime = outputTimeFormat.format(timeObj);

                return displayDate + " | " + formattedTime;
            } catch (ParseException e) {
                // If parsing fails, return raw date and time
                return date + " | " + time;
            }
        }
    }
}