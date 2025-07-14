package com.example.project_prm.MainScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.project_prm.R;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    
    private List<DoctorModel> doctors;
    private OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(DoctorModel doctor);
    }

    public DoctorAdapter(List<DoctorModel> doctors, OnDoctorClickListener listener) {
        this.doctors = doctors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        DoctorModel doctor = doctors.get(position);
        holder.bind(doctor);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public void updateDoctors(List<DoctorModel> newDoctors) {
        this.doctors = newDoctors;
        notifyDataSetChanged();
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivDoctorImage;
        private TextView tvDoctorName, tvDoctorSpecialty, tvDoctorLocation;
        private TextView tvDoctorRating, tvDoctorReviewCount, tvDoctorExperience;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDoctorImage = itemView.findViewById(R.id.ivDoctorImage);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDoctorSpecialty = itemView.findViewById(R.id.tvDoctorSpecialty);
            tvDoctorLocation = itemView.findViewById(R.id.tvDoctorLocation);
            tvDoctorRating = itemView.findViewById(R.id.tvDoctorRating);
            tvDoctorReviewCount = itemView.findViewById(R.id.tvDoctorReviewCount);
            tvDoctorExperience = itemView.findViewById(R.id.tvDoctorExperience);
        }

        public void bind(DoctorModel doctor) {
            tvDoctorName.setText(doctor.name);
            tvDoctorSpecialty.setText(doctor.specialty);
            tvDoctorLocation.setText(doctor.location);
            tvDoctorRating.setText(String.format("%.1f", doctor.rating));
            tvDoctorReviewCount.setText("(" + doctor.reviewCount + " đánh giá)");
            tvDoctorExperience.setText(doctor.experienceYears + " năm kinh nghiệm");

            // Load doctor image
            Glide.with(itemView.getContext())
                    .load(R.drawable.ic_doctor)
                    .into(ivDoctorImage);

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDoctorClick(doctor);
                }
            });
        }
    }
} 