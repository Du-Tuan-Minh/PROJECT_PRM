package com.example.project_prm.ui.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.project_prm.R;
import java.util.ArrayList;
import java.util.List;
import com.example.project_prm.Model.DoctorModel;

public class FindDoctorActivity extends AppCompatActivity {
    
    private ImageView ivBack, ivSearch;
    private TextView tvTitle;
    private EditText etSearch;
    private ChipGroup chipGroupFilter;
    private RecyclerView rvDoctors;
    
    private DoctorAdapter adapter;
    private List<DoctorModel> allDoctors;
    private List<DoctorModel> filteredDoctors;
    private DoctorRepository doctorRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);
        
        doctorRepository = DoctorRepository.getInstance();
        
        initViews();
        setupListeners();
        loadDoctors();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        ivSearch = findViewById(R.id.ivSearch);
        tvTitle = findViewById(R.id.tvTitle);
        etSearch = findViewById(R.id.etSearch);
        chipGroupFilter = findViewById(R.id.chipGroupFilter);
        rvDoctors = findViewById(R.id.rvDoctors);
        
        tvTitle.setText("Tìm bác sĩ");
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());
        
        ivSearch.setOnClickListener(v -> {
            etSearch.setVisibility(etSearch.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        
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
        
        setupFilterChips();
    }

    private void setupFilterChips() {
        Chip chipAll = findViewById(R.id.chipAll);
        Chip chipTimMach = findViewById(R.id.chipTimMach);
        Chip chipDaLieu = findViewById(R.id.chipDaLieu);
        Chip chipNoiTongQuat = findViewById(R.id.chipNoiTongQuat);
        Chip chipThanKinh = findViewById(R.id.chipThanKinh);
        
        chipAll.setOnClickListener(v -> {
            showAllDoctors();
        });
        
        chipTimMach.setOnClickListener(v -> {
            showDoctorsBySpecialty("Tim mạch");
        });
        
        chipDaLieu.setOnClickListener(v -> {
            showDoctorsBySpecialty("Da liễu");
        });
        
        chipNoiTongQuat.setOnClickListener(v -> {
            showDoctorsBySpecialty("Nội tổng quát");
        });
        
        chipThanKinh.setOnClickListener(v -> {
            showDoctorsBySpecialty("Thần kinh");
        });
    }

    private void loadDoctors() {
        allDoctors = doctorRepository.getAllDoctors();
        filteredDoctors = new ArrayList<>(allDoctors);
        
        adapter = new DoctorAdapter(filteredDoctors, new DoctorAdapter.OnDoctorClickListener() {
            @Override
            public void onDoctorClick(DoctorModel doctor) {
                // Open doctor details or booking
                Intent intent = new Intent(FindDoctorActivity.this, BookAppointmentActivity.class);
                intent.putExtra("doctor_id", doctor.getId());
                intent.putExtra("doctor_name", doctor.getName());
                intent.putExtra("doctor_specialty", doctor.getSpecialty());
                intent.putExtra("doctor_hospital", doctor.getHospital());
                startActivity(intent);
            }
        });
        
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvDoctors.setAdapter(adapter);
    }

    private void filterDoctors(String query) {
        if (query.isEmpty()) {
            filteredDoctors = new ArrayList<>(allDoctors);
        } else {
            filteredDoctors = doctorRepository.searchDoctors(query);
        }
        adapter.updateDoctors(filteredDoctors);
    }

    private void showAllDoctors() {
        filteredDoctors = new ArrayList<>(allDoctors);
        adapter.updateDoctors(filteredDoctors);
    }

    private void showDoctorsBySpecialty(String specialty) {
        filteredDoctors = doctorRepository.getDoctorsBySpecialty(specialty);
        adapter.updateDoctors(filteredDoctors);
    }
} 