package com.example.project_prm.ui.AppointmentScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.R;

import java.util.ArrayList;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    // Constants for appointment types
    public static final int TYPE_UPCOMING = 1;
    public static final int TYPE_COMPLETED = 2;
    public static final int TYPE_CANCELLED = 3;

    private List<Object> appointments;
    private OnAppointmentClickListener listener;
    private OnActionClickListener actionListener;
    private int appointmentType;

    public interface OnAppointmentClickListener {
        void onAppointmentClick(Appointment appointment);
        default void onCancelClick(Appointment appointment) {}
        default void onRescheduleClick(Appointment appointment) {}
        default void onReviewClick(Appointment appointment) {}
        default void onBookAgainClick(Appointment appointment) {}
    }

    public interface OnActionClickListener {
        void onCancelClick(Object appointment);
        void onRescheduleClick(Object appointment);
        void onReviewClick(Object appointment);
        void onBookAgainClick(Object appointment);
    }

    public AppointmentAdapter(List<?> appointments, OnAppointmentClickListener listener) {
        this.appointments = new ArrayList<>();
        if (appointments != null) {
            this.appointments.addAll(appointments);
        }
        this.listener = listener;
    }

    public AppointmentAdapter(List<?> appointments, int appointmentType) {
        this.appointments = new ArrayList<>();
        if (appointments != null) {
            this.appointments.addAll(appointments);
        }
        this.appointmentType = appointmentType;
    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.actionListener = listener;
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
        Object appointment = appointments.get(position);
        holder.bind(appointment);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivDoctorAvatar;
        private TextView tvDoctorName;
        private TextView tvAppointmentType;
        private TextView tvStatus;
        private TextView tvDateTime;
        private CardView ivAction;
        private ImageView ivActionIcon;
        private View llActionButtons;
        private CardView btnCancelBook;
        private TextView tvCancelBook;
        private CardView btnRescheduleReview;
        private TextView tvRescheduleReview;
        private View llCancelledInfo;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            ivDoctorAvatar = itemView.findViewById(R.id.iv_doctor_avatar);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvAppointmentType = itemView.findViewById(R.id.tv_appointment_type);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            ivAction = itemView.findViewById(R.id.iv_action);
            ivActionIcon = itemView.findViewById(R.id.iv_action_icon);
            llActionButtons = itemView.findViewById(R.id.ll_action_buttons);
            btnCancelBook = itemView.findViewById(R.id.btn_cancel_book);
            tvCancelBook = itemView.findViewById(R.id.tv_cancel_book);
            btnRescheduleReview = itemView.findViewById(R.id.btn_reschedule_review);
            tvRescheduleReview = itemView.findViewById(R.id.tv_reschedule_review);
            llCancelledInfo = itemView.findViewById(R.id.ll_cancelled_info);
        }

        public void bind(Object appointment) {
            if (appointment instanceof Appointment) {
                bindAppointment((Appointment) appointment);
            } else if (appointment instanceof AppointmentHistoryManager.AppointmentHistoryItem) {
                bindHistoryItem((AppointmentHistoryManager.AppointmentHistoryItem) appointment);
            }
        }

        private void bindAppointment(Appointment appointment) {
            // Set doctor name
            tvDoctorName.setText(appointment.getDoctor() != null ? appointment.getDoctor() : "Unknown Doctor");

            // Set appointment type
            tvAppointmentType.setText(appointment.getAppointmentType() != null ? appointment.getAppointmentType() : "Consultation");

            // Set date and time
            String dateTime = "";
            if (appointment.getDate() != null && appointment.getTime() != null) {
                dateTime = appointment.getDate() + " | " + appointment.getTime();
            } else if (appointment.getDate() != null) {
                dateTime = appointment.getDate();
            }
            tvDateTime.setText(dateTime);

            // Set status and styling based on appointment status
            String status = appointment.getStatus() != null ? appointment.getStatus().toLowerCase() : "upcoming";
            setupStatusAndActions(status, appointment);

            // Set action icon based on appointment type
            setupActionIcon(appointment.getAppointmentType());

            // Set click listener for the whole item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAppointmentClick(appointment);
                }
            });
        }

        private void bindHistoryItem(AppointmentHistoryManager.AppointmentHistoryItem item) {
            // Set doctor name
            tvDoctorName.setText(item.doctorName != null ? item.doctorName : "Unknown Doctor");

            // Set appointment type
            tvAppointmentType.setText(item.packageType != null ? item.packageType : "Consultation");

            // Set date and time
            String dateTime = "";
            if (item.appointmentDate != null && item.appointmentTime != null) {
                dateTime = item.appointmentDate + " | " + item.appointmentTime;
            } else if (item.appointmentDate != null) {
                dateTime = item.appointmentDate;
            }
            tvDateTime.setText(dateTime);

            // Set status and styling based on appointment status
            String status = item.status != null ? item.status.toLowerCase() : "upcoming";
            setupStatusAndActions(status, item);

            // Set action icon based on appointment type
            setupActionIcon(item.packageType);

            // Set click listener for the whole item
            itemView.setOnClickListener(v -> {
                if (actionListener != null) {
                    // Handle history item click
                }
            });
        }

        private void setupStatusAndActions(String status, Object appointment) {
            switch (status) {
                case "upcoming":
                    setupUpcomingAppointment(appointment);
                    break;
                case "completed":
                    setupCompletedAppointment(appointment);
                    break;
                case "cancelled":
                    setupCancelledAppointment(appointment);
                    break;
                default:
                    setupUpcomingAppointment(appointment);
                    break;
            }
        }

        private void setupUpcomingAppointment(Object appointment) {
            // Status styling
            tvStatus.setText("Upcoming");
            tvStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_blue_dark));
            tvStatus.setBackgroundResource(R.drawable.status_upcoming_background);

            // Action icon styling
            ivAction.setBackgroundResource(R.drawable.action_icon_background);
            ivActionIcon.setColorFilter(itemView.getContext().getColor(android.R.color.holo_blue_dark));

            // Action buttons
            llActionButtons.setVisibility(View.VISIBLE);
            llCancelledInfo.setVisibility(View.GONE);

            tvCancelBook.setText("Cancel Appointment");
            tvRescheduleReview.setText("Reschedule");

            // Button click listeners
            btnCancelBook.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onCancelClick(appointment);
                } else if (listener != null) {
                    listener.onCancelClick((Appointment) appointment);
                }
            });

            btnRescheduleReview.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onRescheduleClick(appointment);
                } else if (listener != null) {
                    listener.onRescheduleClick((Appointment) appointment);
                }
            });
        }

        private void setupCompletedAppointment(Object appointment) {
            // Status styling
            tvStatus.setText("Completed");
            tvStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
            tvStatus.setBackgroundResource(R.drawable.status_completed_background);

            // Action icon styling
            ivAction.setBackgroundResource(R.drawable.action_icon_background);
            ivActionIcon.setColorFilter(itemView.getContext().getColor(android.R.color.holo_blue_dark));

            // Action buttons
            llActionButtons.setVisibility(View.VISIBLE);
            llCancelledInfo.setVisibility(View.GONE);

            tvCancelBook.setText("Book Again");
            tvRescheduleReview.setText("Leave a Review");

            // Button click listeners
            btnCancelBook.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onBookAgainClick(appointment);
                } else if (listener != null) {
                    listener.onBookAgainClick((Appointment) appointment);
                }
            });

            btnRescheduleReview.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onReviewClick(appointment);
                } else if (listener != null) {
                    listener.onReviewClick((Appointment) appointment);
                }
            });
        }

        private void setupCancelledAppointment(Object appointment) {
            // Status styling
            tvStatus.setText("Cancelled");
            tvStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
            tvStatus.setBackgroundResource(R.drawable.status_cancelled_background);

            // Action icon styling - disabled appearance
            ivAction.setBackgroundResource(R.drawable.action_icon_background);
            ivActionIcon.setColorFilter(itemView.getContext().getColor(android.R.color.darker_gray));

            // Hide action buttons for cancelled appointments
            llActionButtons.setVisibility(View.GONE);
            llCancelledInfo.setVisibility(View.VISIBLE);
        }

        private void setupActionIcon(String appointmentType) {
            if (appointmentType == null) {
                ivActionIcon.setImageResource(R.drawable.ic_message);
                return;
            }

            switch (appointmentType.toLowerCase()) {
                case "messaging":
                    ivActionIcon.setImageResource(R.drawable.ic_message);
                    break;
                case "voice call":
                    ivActionIcon.setImageResource(R.drawable.ic_phone);
                    break;
                case "video call":
                    ivActionIcon.setImageResource(R.drawable.ic_video_call);
                    break;
                default:
                    ivActionIcon.setImageResource(R.drawable.ic_message);
                    break;
            }
        }
    }

    public void updateAppointments(List<?> newAppointments) {
        this.appointments.clear();
        if (newAppointments != null) {
            this.appointments.addAll(newAppointments);
        }
        notifyDataSetChanged();
    }
}