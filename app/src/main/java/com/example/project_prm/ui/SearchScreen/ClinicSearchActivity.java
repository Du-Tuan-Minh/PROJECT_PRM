// NEW FILE: app/src/main/java/com/example/project_prm/ui/SearchScreen/ClinicSearchActivity.java
package com.example.project_prm.ui.SearchScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android:widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.SearchManager.ClinicSearchManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ClinicSearchActivity extends AppCompatActivity {

    private TextInputEditText etSearchClinic;
    private AutoCompleteTextView actvSpecialty;
    private RecyclerView rvSearchResults;
    private View emptyStateView;
    private View progressBar;

    private ClinicSearchAdapter adapter;
    private List<ClinicSearchManager.ClinicSearchResult> clinicList;
    private HealthcareService service;

    private String currentSearchQuery = "";
    private String currentSpecialty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_search);

        // Get search parameters from intent
        String searchSpecialty = getIntent().getStringExtra("search_specialty");
        String diseaseName = getIntent().getStringExtra("disease_name");

        initViews();
        setupRecyclerView();
        setupSearchListeners();
        loadSpecialties();

        // If launched from disease detail, pre-fill specialty
        if (searchSpecialty != null) {
            actvSpecialty.setText(searchSpecialty);
            currentSpecialty = searchSpecialty;
            searchClinics();
        } else {
            loadAllClinics();
        }
    }

    private void initViews() {
        etSearchClinic = findViewById(R.id.et_search_clinic);
        actvSpecialty = findViewById(R.id.actv_specialty);
        rvSearchResults = findViewById(R.id.rv_search_results);
        emptyStateView = findViewById(R.id.empty_state_view);
        progressBar = findViewById(R.id.progress_bar);

        service = HealthcareService.getInstance(this);
        clinicList = new ArrayList<>();

        // Setup back button
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ClinicSearchAdapter(clinicList, new ClinicSearchAdapter.OnClinicClickListener() {
            @Override
            public void onClinicClick(ClinicSearchManager.ClinicSearchResult clinic) {
                // Navigate to clinic detail
                Intent intent = new Intent(ClinicSearchActivity.this, ClinicDetailActivity.class);
                intent.putExtra("clinic_id", clinic.id);
                intent.putExtra("clinic_name", clinic.name);
                startActivity(intent);
            }

            @Override
            public void onBookAppointmentClick(ClinicSearchManager.ClinicSearchResult clinic) {
                // Navigate to appointment booking
                Intent intent = new Intent(ClinicSearchActivity.this, AppointmentBookingActivity.class);
                intent.putExtra("clinic_id", clinic.id);
                intent.putExtra("clinic_name", clinic.name);
                startActivity(intent);
            }
        });

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(adapter);
    }

    private void setupSearchListeners() {
        // Clinic name search
        etSearchClinic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                currentSearchQuery = s.toString().trim();
                searchClinics();
            }
        });

        // Specialty filter
        actvSpecialty.setOnItemClickListener((parent, view, position, id) -> {
            currentSpecialty = actvSpecialty.getText().toString().trim();
            searchClinics();
        });
    }

    private void loadSpecialties() {
        service.getAvailableSpecialties(new ClinicSearchManager.OnSpecialtiesListener() {
            @Override
            public void onSuccess(List<String> specialties) {
                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            ClinicSearchActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            specialties
                    );
                    actvSpecialty.setAdapter(adapter);
                });
            }

            @Override
            public void onError(String error) {
                // Use default specialties
                runOnUiThread(() -> {
                    String[] defaultSpecialties = {
                            "Khám tổng quát", "Tim mạch", "Da liễu", "Nhãn khoa",
                            "Tai - Mũi - Họng", "Cơ xương khớp", "Nội tiết",
                            "Thần kinh", "Ung bướu", "Sản phụ khoa"
                    };
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            ClinicSearchActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            defaultSpecialties
                    );
                    actvSpecialty.setAdapter(adapter);
                });
            }
        });
    }

    private void searchClinics() {
        showLoading(true);

        if (currentSearchQuery.isEmpty() && currentSpecialty.isEmpty()) {
            loadAllClinics();
            return;
        }

        // Create search filter
        ClinicSearchManager.SearchFilter filter = new ClinicSearchManager.SearchFilter();
        filter.name = currentSearchQuery.isEmpty() ? null : currentSearchQuery;
        filter.specialty = currentSpecialty.isEmpty() ? null : currentSpecialty;
        filter.sortBy = "rating"; // Sort by rating by default

        service.searchClinicsWithFilters(filter, new ClinicSearchManager.OnClinicSearchListener() {
            @Override
            public void onSuccess(List<ClinicSearchManager.ClinicSearchResult> results) {
                runOnUiThread(() -> {
                    showLoading(false);
                    updateSearchResults(results);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showError("Search failed: " + error);
                    showEmptyState(true);
                });
            }
        });
    }

    private void loadAllClinics() {
        showLoading(true);

        service.searchClinicsByName("", new ClinicSearchManager.OnClinicSearchListener() {
            @Override
            public void onSuccess(List<ClinicSearchManager.ClinicSearchResult> results) {
                runOnUiThread(() -> {
                    showLoading(false);
                    updateSearchResults(results);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showError("Failed to load clinics: " + error);
                    showEmptyState(true);
                });
            }
        });
    }

    private void updateSearchResults(List<ClinicSearchManager.ClinicSearchResult> results) {
        clinicList.clear();
        clinicList.addAll(results);
        adapter.notifyDataSetChanged();

        showEmptyState(results.isEmpty());
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show) {
        emptyStateView.setVisibility(show ? View.VISIBLE : View.GONE);
        rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}