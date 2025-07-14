package com.example.project_prm.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import com.example.project_prm.Repository.DoctorRepository;
import com.example.project_prm.Model.DoctorModel;
import com.example.project_prm.adapter.DoctorAdapter;
import java.util.ArrayList;
import java.util.List;

public class SearchDoctorActivity extends AppCompatActivity {
    
    private ImageView ivBack;
    private EditText etSearch;
    private RecyclerView rvDoctors;
    private DoctorAdapter adapter;
    private DoctorRepository doctorRepository;
    private List<DoctorModel> allDoctors;
    private List<DoctorModel> filteredDoctors;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);
        
        doctorRepository = DoctorRepository.getInstance();
        initViews();
        loadDoctors();
        setupSearch();
        setupListeners();
    }
    
    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        etSearch = findViewById(R.id.etSearch);
        rvDoctors = findViewById(R.id.rvDoctors);
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
    }
    
    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());
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
                    doctor.getSpecialization().toLowerCase().contains(lowerQuery)) {
                    filteredDoctors.add(doctor);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
    
    private void loadDoctors() {
        allDoctors = doctorRepository.getAllDoctors();
        filteredDoctors = new ArrayList<>(allDoctors);
        
        adapter = new DoctorAdapter(this, filteredDoctors, doctor -> {
            Intent intent = new Intent(this, DoctorDetailActivity.class);
            intent.putExtra("doctor_id", doctor.getId());
            intent.putExtra("doctor_name", doctor.getName());
            intent.putExtra("doctor_specialty", doctor.getSpecialization());
            intent.putExtra("doctor_hospital", doctor.getHospital());
            intent.putExtra("doctor_rating", doctor.getRating());
            intent.putExtra("doctor_experience", doctor.getExperience());
            intent.putExtra("doctor_image", doctor.getImageUrl());
            startActivity(intent);
        });
        rvDoctors.setAdapter(adapter);
    }
} 