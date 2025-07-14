package com.example.project_prm.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import com.example.project_prm.Model.DoctorModel;
import java.util.List;

public class DoctorBookingAdapter extends RecyclerView.Adapter<DoctorBookingAdapter.ViewHolder> {
    
    private List<DoctorModel> doctors;
    private OnDoctorClickListener listener;
    
    public interface OnDoctorClickListener {
        void onDoctorClick(DoctorModel doctor);
    }
    
    public DoctorBookingAdapter(List<DoctorModel> doctors, OnDoctorClickListener listener) {
        this.doctors = doctors;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_booking, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorModel doctor = doctors.get(position);
        holder.bind(doctor);
    }
    
    @Override
    public int getItemCount() {
        return doctors.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDoctorImage;
        TextView tvDoctorName, tvSpecialty, tvHospital, tvRating, tvExperience;
        
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDoctorImage = itemView.findViewById(R.id.ivDoctorImage);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvHospital = itemView.findViewById(R.id.tvHospital);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvExperience = itemView.findViewById(R.id.tvExperience);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDoctorClick(doctors.get(getAdapterPosition()));
                }
            });
        }
        
        void bind(DoctorModel doctor) {
            tvDoctorName.setText(doctor.getName());
            tvSpecialty.setText(doctor.getSpecialty());
            tvHospital.setText(doctor.getHospital());
            tvRating.setText(String.format("%.1f ⭐", doctor.getRating()));
            tvExperience.setText(doctor.getExperience() + " years experience");
            
            // Load doctor image
            if (doctor.getImage() != null && !doctor.getImage().isEmpty()) {
                // Use Glide to load image from URL
                com.bumptech.glide.Glide.with(itemView.getContext())
                        .load(doctor.getImage())
                        .placeholder(R.drawable.ic_doctor)
                        .error(R.drawable.ic_doctor)
                        .into(ivDoctorImage);
            } else {
                ivDoctorImage.setImageResource(R.drawable.ic_doctor);
            }
        }
    }
} 