package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import com.example.project_prm.Repository.DoctorRepository;
import com.example.project_prm.Model.DoctorModel;
import com.example.project_prm.Model.ReviewModel;
import com.example.project_prm.adapter.ReviewAdapter;
import java.util.List;

public class DoctorInfoFragment extends Fragment {
    private ImageView ivDoctorImage;
    private TextView tvDoctorName, tvSpecialty, tvHospital, tvRating, tvExperience, tvDoctorDescription;
    private Button btnContinue;
    private RecyclerView rvReviews;
    private ReviewAdapter reviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        loadDoctorInfo();
        setupListeners();
    }

    private void initViews(View view) {
        ivDoctorImage = view.findViewById(R.id.ivDoctorImage);
        tvDoctorName = view.findViewById(R.id.tvDoctorName);
        tvSpecialty = view.findViewById(R.id.tvSpecialty);
        tvHospital = view.findViewById(R.id.tvHospital);
        tvRating = view.findViewById(R.id.tvRating);
        tvExperience = view.findViewById(R.id.tvExperience);
        tvDoctorDescription = view.findViewById(R.id.tvDoctorDescription);
        btnContinue = view.findViewById(R.id.btnContinue);
        rvReviews = view.findViewById(R.id.rvReviews);
        rvReviews.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadDoctorInfo() {
        BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
        if (activity != null && activity.bookingData.doctorId != null) {
            DoctorRepository doctorRepository = DoctorRepository.getInstance();
            DoctorModel doctor = doctorRepository.getDoctorById(activity.bookingData.doctorId);
            if (doctor != null) {
                tvDoctorName.setText(doctor.getName());
                tvSpecialty.setText(doctor.getSpecialty());
                tvHospital.setText(doctor.getHospital());
                tvRating.setText(String.format("%.1f ⭐", doctor.getRating()));
                tvExperience.setText(doctor.getExperience() + " năm kinh nghiệm");
                tvDoctorDescription.setText(doctor.getDescription());
                int imageResource = getContext().getResources().getIdentifier(
                    doctor.getImage(), "drawable", getContext().getPackageName());
                if (imageResource != 0) {
                    ivDoctorImage.setImageResource(imageResource);
                } else {
                    ivDoctorImage.setImageResource(R.drawable.ic_doctor_placeholder);
                }
                List<ReviewModel> reviews = doctorRepository.getDoctorReviews(activity.bookingData.doctorId);
                reviewAdapter = new ReviewAdapter(reviews);
                rvReviews.setAdapter(reviewAdapter);
            }
        }
    }

    private void setupListeners() {
        btnContinue.setOnClickListener(v -> {
            BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
            if (activity != null) {
                activity.onDoctorInfoConfirmed();
            }
        });
    }
} 