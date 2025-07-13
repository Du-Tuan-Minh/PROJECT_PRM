package com.example.project_prm.ui.SearchScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.Clinic;
import com.example.project_prm.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.List;

public class ClinicSearchAdapter extends RecyclerView.Adapter<ClinicSearchAdapter.ClinicViewHolder> {

    private final List<Clinic> clinicList;
    private final OnClinicClickListener listener;
    private double userLatitude = 0;
    private double userLongitude = 0;
    private boolean locationEnabled = false;

    public interface OnClinicClickListener {
        void onClinicClick(Clinic clinic);
        void onBookAppointmentClick(Clinic clinic);
    }

    public ClinicSearchAdapter(List<Clinic> clinicList, OnClinicClickListener listener) {
        this.clinicList = clinicList;
        this.listener = listener;
    }

    public void setUserLocation(double latitude, double longitude, boolean enabled) {
        this.userLatitude = latitude;
        this.userLongitude = longitude;
        this.locationEnabled = enabled;
    }

    @NonNull
    @Override
    public ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clinic_search, parent, false);
        return new ClinicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicViewHolder holder, int position) {
        Clinic clinic = clinicList.get(position);
        holder.bind(clinic, listener);
    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    class ClinicViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivClinicImage;
        private final TextView tvClinicName;
        private final TextView tvSpecialty;
        private final TextView tvAddress;
        private final TextView tvDistance;
        private final RatingBar ratingBar;
        private final TextView tvRating;
        private final TextView tvReviewCount;
        private final TextView tvOpenStatus;
        private final MaterialButton btnBookAppointment;

        public ClinicViewHolder(@NonNull View itemView) {
            super(itemView);
            ivClinicImage = itemView.findViewById(R.id.iv_clinic_image);
            tvClinicName = itemView.findViewById(R.id.tv_clinic_name);
            tvSpecialty = itemView.findViewById(R.id.tv_specialty);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvReviewCount = itemView.findViewById(R.id.tv_review_count);
            tvOpenStatus = itemView.findViewById(R.id.tv_open_status);
            btnBookAppointment = itemView.findViewById(R.id.btn_book_appointment);
        }

        public void bind(Clinic clinic, OnClinicClickListener listener) {
            // Clinic name
            tvClinicName.setText(clinic.getName());

            // Specialty - show first specialty or "Khám tổng quát"
            String[] specialties = clinic.getSpecialties();
            String specialtyText = "Khám tổng quát";
            if (specialties != null && specialties.length > 0) {
                specialtyText = specialties[0];
            }
            tvSpecialty.setText(specialtyText);

            // Address
            tvAddress.setText(clinic.getAddress());

            // Distance (if location enabled)
            if (locationEnabled && userLatitude != 0 && userLongitude != 0) {
                double distance = calculateDistance(userLatitude, userLongitude,
                        clinic.getLatitude(), clinic.getLongitude());
                tvDistance.setText(String.format("📍 %.1f km", distance));
                tvDistance.setVisibility(View.VISIBLE);
            } else {
                tvDistance.setVisibility(View.GONE);
            }

            // Rating
            ratingBar.setRating(clinic.getRating());
            tvRating.setText(String.format("%.1f", clinic.getRating()));
            tvReviewCount.setText(String.format("(%d đánh giá)", clinic.getReviewCount()));

            // Open status
            boolean isOpen = isClinicOpen(clinic);
            tvOpenStatus.setText(isOpen ? "🟢 Đang mở cửa" : "🔴 Đã đóng cửa");
            tvOpenStatus.setTextColor(itemView.getContext().getColor(
                    isOpen ? R.color.success_green : R.color.error_red
            ));

            // Clinic image (use default for now)
            ivClinicImage.setImageResource(R.drawable.default_clinic_image);

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClinicClick(clinic);
                }
            });

            btnBookAppointment.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookAppointmentClick(clinic);
                }
            });
        }

        private boolean isClinicOpen(Clinic clinic) {
            // Simple logic - assume clinic is open during business hours
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            
            // Most clinics are open from 6 AM to 8 PM
            return hour >= 6 && hour <= 20;
        }

        private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            final int R = 6371; // Radius of the earth in km

            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = R * c; // distance in km

            return distance;
        }
    }
}