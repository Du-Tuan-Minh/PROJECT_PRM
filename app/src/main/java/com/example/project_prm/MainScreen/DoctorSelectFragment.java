package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import com.example.project_prm.MainScreen.DoctorRepository;
import com.example.project_prm.Model.DoctorModel;
import com.example.project_prm.adapter.DoctorBookingAdapter;
import java.util.ArrayList;
import java.util.List;

public class DoctorSelectFragment extends Fragment {
    private EditText etSearch;
    private RecyclerView rvDoctors;
    private DoctorBookingAdapter adapter;
    private DoctorRepository doctorRepository;
    private List<DoctorModel> allDoctors;
    private List<DoctorModel> filteredDoctors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doctorRepository = DoctorRepository.getInstance();
        initViews(view);
        loadDoctors();
        setupSearch();
    }

    private void initViews(View view) {
        etSearch = view.findViewById(R.id.etSearch);
        rvDoctors = view.findViewById(R.id.rvDoctors);
        rvDoctors.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDoctors(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterDoctors(String query) {
        filteredDoctors.clear();
        if (query.isEmpty()) {
            filteredDoctors.addAll(allDoctors);
        } else {
            String lowerQuery = query.toLowerCase();
            for (DoctorModel doctor : allDoctors) {
                if (doctor.getName().toLowerCase().contains(lowerQuery) ||
                    doctor.getHospital().toLowerCase().contains(lowerQuery) ||
                    doctor.getSpecialty().toLowerCase().contains(lowerQuery)) {
                    filteredDoctors.add(doctor);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadDoctors() {
        allDoctors = doctorRepository.getAllDoctors();
        filteredDoctors = new ArrayList<>(allDoctors);
        adapter = new DoctorBookingAdapter(filteredDoctors, doctor -> {
            BookAppointmentActivity activity = (BookAppointmentActivity) getActivity();
            if (activity != null) {
                activity.onDoctorSelected(
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getSpecialty(),
                    doctor.getHospital(),
                    doctor.getImage()
                );
            }
        });
        rvDoctors.setAdapter(adapter);
    }
} 