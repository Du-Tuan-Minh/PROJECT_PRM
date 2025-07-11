// NEW FILE: app/src/main/java/com/example/project_prm/ui/SearchScreen/DiseaseDetailActivity.java
package com.example.project_prm.ui.SearchScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.Disease;
import com.example.project_prm.DataManager.SearchManager.DiseaseSearchManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.example.project_prm.ui.SearchScreen.ClinicSearchActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DiseaseDetailActivity extends AppCompatActivity {

    private TextView tvDiseaseName, tvDiseaseSymptoms, tvDiseaseCauses, tvDiseaseTreatment;
    private RecyclerView rvRelatedDiseases;
    private MaterialButton btnFindClinics;
    private View progressBar;

    private Disease currentDisease;
    private HealthcareService service;
    private DiseaseSearchAdapter relatedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);

        // Get disease ID from intent
        int diseaseId = getIntent().getIntExtra("disease_id", -1);
        String diseaseName = getIntent().getStringExtra("disease_name");

        if (diseaseId == -1) {
            Toast.makeText(this, "Invalid disease ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        loadDiseaseDetails(diseaseId);
    }

    private void initViews() {
        // Header
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // Disease information
        tvDiseaseName = findViewById(R.id.tv_disease_name);
        tvDiseaseSymptoms = findViewById(R.id.tv_disease_symptoms);
        tvDiseaseCauses = findViewById(R.id.tv_disease_causes);
        tvDiseaseTreatment = findViewById(R.id.tv_disease_treatment);

        // Related diseases
        rvRelatedDiseases = findViewById(R.id.rv_related_diseases);

        // Action button
        btnFindClinics = findViewById(R.id.btn_find_clinics);

        // Progress bar
        progressBar = findViewById(R.id.progress_bar);

        // Initialize service
        service = HealthcareService.getInstance(this);

        // Setup related diseases RecyclerView
        setupRelatedDiseasesRecyclerView();
    }

    private void setupClickListeners() {
        btnFindClinics.setOnClickListener(v -> {
            if (currentDisease != null) {
                // Navigate to clinic search with disease-related specialty
                Intent intent = new Intent(this, ClinicSearchActivity.class);
                intent.putExtra("search_specialty", getSpecialtyFromDisease(currentDisease.getName()));
                intent.putExtra("disease_name", currentDisease.getName());
                startActivity(intent);
            }
        });
    }

    private void setupRelatedDiseasesRecyclerView() {
        rvRelatedDiseases.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        relatedAdapter = new DiseaseSearchAdapter(new java.util.ArrayList<>(), new DiseaseSearchAdapter.OnDiseaseClickListener() {
            @Override
            public void onDiseaseClick(Disease disease) {
                // Navigate to another disease detail
                Intent intent = new Intent(DiseaseDetailActivity.this, DiseaseDetailActivity.class);
                intent.putExtra("disease_id", disease.getId());
                intent.putExtra("disease_name", disease.getName());
                startActivity(intent);
            }
        });
        rvRelatedDiseases.setAdapter(relatedAdapter);
    }

    private void loadDiseaseDetails(int diseaseId) {
        showLoading(true);

        service.getDiseaseDetails(diseaseId, new DiseaseSearchManager.OnDiseaseDetailListener() {
            @Override
            public void onSuccess(Disease disease) {
                runOnUiThread(() -> {
                    currentDisease = disease;
                    displayDiseaseDetails(disease);
                    loadRelatedDiseases(diseaseId);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showError("Failed to load disease details: " + error);
                });
            }
        });
    }

    private void displayDiseaseDetails(Disease disease) {
        tvDiseaseName.setText(disease.getName());
        tvDiseaseSymptoms.setText(disease.getSymptoms() != null ? disease.getSymptoms() : "Không có thông tin về triệu chứng");
        tvDiseaseCauses.setText(disease.getCauses() != null ? disease.getCauses() : "Không có thông tin về nguyên nhân");
        tvDiseaseTreatment.setText(disease.getTreatment() != null ? disease.getTreatment() : "Không có thông tin về điều trị");

        showLoading(false);
    }

    private void loadRelatedDiseases(int diseaseId) {
        new Thread(() -> {
            try {
                List<Disease> relatedDiseases = service.getRelatedDiseases(diseaseId, 10);
                runOnUiThread(() -> {
                    if (relatedAdapter != null) {
                        relatedAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                // Ignore errors for related diseases (non-critical)
            }
        }).start();
    }

    private String getSpecialtyFromDisease(String diseaseName) {
        if (diseaseName == null) return "Khám tổng quát";

        String lowerName = diseaseName.toLowerCase();

        if (lowerName.contains("tim") || lowerName.contains("huyết áp")) {
            return "Tim mạch";
        } else if (lowerName.contains("da") || lowerName.contains("ngoài")) {
            return "Da liễu";
        } else if (lowerName.contains("mắt")) {
            return "Nhãn khoa";
        } else if (lowerName.contains("tai") || lowerName.contains("mũi") || lowerName.contains("họng")) {
            return "Tai - Mũi - Họng";
        } else if (lowerName.contains("xương") || lowerName.contains("khớp")) {
            return "Cơ xương khớp";
        } else {
            return "Khám tổng quát";
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}