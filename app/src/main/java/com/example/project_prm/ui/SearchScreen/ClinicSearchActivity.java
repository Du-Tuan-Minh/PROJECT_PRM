package com.example.project_prm.ui.SearchScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.SearchManager.ClinicSearchManager;
import com.example.project_prm.R;
import com.example.project_prm.Services.HealthcareService;
import com.example.project_prm.ui.BookingScreen.AppointmentBookingActivity;
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
        String searchQuery = getIntent().getStringExtra("search_query");

        initViews();
        setupRecyclerView();
        setupSearchListeners();
        loadSpecialties();

        // Pre-fill search if parameters provided
        if (searchQuery != null && !searchQuery.isEmpty()) {
            etSearchClinic.setText(searchQuery);
            currentSearchQuery = searchQuery;
        }

        // If launched from disease detail, pre-fill specialty
        if (searchSpecialty != null) {
            actvSpecialty.setText(searchSpecialty);
            currentSpecialty = searchSpecialty;
            searchClinics();
        } else if (searchQuery != null && !searchQuery.isEmpty()) {
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
                // Add small delay to avoid too many requests
                etSearchClinic.removeCallbacks(searchRunnable);
                etSearchClinic.postDelayed(searchRunnable, 300);
            }
        });

        // Specialty filter
        actvSpecialty.setOnItemClickListener((parent, view, position, id) -> {
            currentSpecialty = actvSpecialty.getText().toString().trim();
            searchClinics();
        });
    }

    private final Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            searchClinics();
        }
    };

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
                            "Tất cả chuyên khoa",
                            "Khám tổng quát",
                            "Tim mạch",
                            "Da liễu",
                            "Nhãn khoa",
                            "Tai - Mũi - Họng",
                            "Cơ xương khớp",
                            "Nội tiết",
                            "Thần kinh",
                            "Ung bướu",
                            "Sản phụ khoa",
                            "Nhi khoa",
                            "Răng hàm mặt"
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

        if (currentSearchQuery.isEmpty() && (currentSpecialty.isEmpty() || currentSpecialty.equals("Tất cả chuyên khoa"))) {
            loadAllClinics();
            return;
        }

        // Create search filter
        ClinicSearchManager.SearchFilter filter = new ClinicSearchManager.SearchFilter();
        filter.name = currentSearchQuery.isEmpty() ? null : currentSearchQuery;

        // Don't filter by specialty if "Tất cả chuyên khoa" is selected
        if (!currentSpecialty.isEmpty() && !currentSpecialty.equals("Tất cả chuyên khoa")) {
            filter.specialty = currentSpecialty;
        }

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
                    showError("Không thể tìm kiếm: " + error);
                    // Don't show empty state on error, keep current results
                    updateSearchResults(new ArrayList<>());
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
                    showError("Không thể tải danh sách phòng khám: " + error);
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

        // Update empty state message based on search criteria
        updateEmptyStateMessage();
    }

    private void updateEmptyStateMessage() {
        if (emptyStateView.getVisibility() == View.VISIBLE) {
            // You can customize empty state message here
            // For now, we'll keep it generic
        }
    }

    private void showLoading(boolean show) {
        if (progressBar != null && rvSearchResults != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showEmptyState(boolean show) {
        if (emptyStateView != null && rvSearchResults != null) {
            emptyStateView.setVisibility(show ? View.VISIBLE : View.GONE);
            rvSearchResults.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up any pending search
        if (etSearchClinic != null) {
            etSearchClinic.removeCallbacks(searchRunnable);
        }
    }
}