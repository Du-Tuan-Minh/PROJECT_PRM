package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.example.project_prm.R;
import java.util.ArrayList;
import java.util.List;

public class DoctorSelectFragment extends Fragment {
    private RecyclerView recyclerView;
    private DoctorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewDoctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DoctorAdapter(getDummyDoctors());
        recyclerView.setAdapter(adapter);
    }

    private List<Doctor> getDummyDoctors() {
        List<Doctor> list = new ArrayList<>();
        int avatar = R.drawable.ic_doctor;
        list.add(new Doctor("Dr. Raul Zirkind", "Voice Call", "Bệnh viện A", avatar));
        list.add(new Doctor("Dr. Keegan Dach", "Messaging", "Bệnh viện B", avatar));
        list.add(new Doctor("Dr. Drake Boeson", "Video Call", "Bệnh viện C", avatar));
        list.add(new Doctor("Dr. Quinn Slatter", "Voice Call", "Bệnh viện D", avatar));
        return list;
    }

    static class Doctor {
        String name, specialty, location;
        int avatarRes;
        Doctor(String name, String specialty, String location, int avatarRes) {
            this.name = name;
            this.specialty = specialty;
            this.location = location;
            this.avatarRes = avatarRes;
        }
    }

    class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
        List<Doctor> doctors;
        DoctorAdapter(List<Doctor> doctors) { this.doctors = doctors; }
        @NonNull
        @Override
        public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_select, parent, false);
            return new DoctorViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
            Doctor d = doctors.get(position);
            holder.tvName.setText(d.name);
            holder.tvSpecialty.setText(d.specialty);
            holder.tvLocation.setText(d.location);
            Glide.with(holder.itemView.getContext()).load(d.avatarRes).into(holder.ivAvatar);
            holder.btnSelect.setOnClickListener(v -> {
                BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
                if (activity != null) {
                    activity.onDoctorSelected(d.name, d.specialty, d.location);
                }
            });
        }
        @Override
        public int getItemCount() { return doctors.size(); }
        class DoctorViewHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar;
            TextView tvName, tvSpecialty, tvLocation;
            MaterialButton btnSelect;
            DoctorViewHolder(View v) {
                super(v);
                ivAvatar = v.findViewById(R.id.ivAvatar);
                tvName = v.findViewById(R.id.tvName);
                tvSpecialty = v.findViewById(R.id.tvSpecialty);
                tvLocation = v.findViewById(R.id.tvLocation);
                btnSelect = v.findViewById(R.id.btnSelect);
            }
        }
    }
} 