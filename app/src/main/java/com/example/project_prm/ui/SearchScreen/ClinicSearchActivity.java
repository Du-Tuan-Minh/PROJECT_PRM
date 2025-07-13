/*
 * 📁 ĐƯỜNG DẪN: app/src/main/java/com/example/project_prm/ui/SearchScreen/ClinicSearchActivity.java
 * 🎯 CHỨC NĂNG: Tìm phòng khám sử dụng OSMDroid (100% miễn phí)
 * ⚠️ THAY THẾ: Toàn bộ file ClinicSearchActivity.java hiện tại
 */

package com.example.project_prm.ui.SearchScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.DataManager.Entity.Clinic;
import com.example.project_prm.DataManager.Repository.ClinicRepository;
import com.example.project_prm.R;
import com.example.project_prm.ui.adapter.ClinicAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class ClinicSearchActivity extends AppCompatActivity {
    private static final String TAG = "ClinicSearchActivity";
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    // UI Components
    private TextInputEditText etSearch;
    private AutoCompleteTextView actvSpecialty;
    private MaterialButton btnSearch, btnToggleView, btnMyLocation;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmptyState, tvLocationStatus;
    private ImageView ivBack;

    // OSMDroid Map
    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private boolean isMapView = false;

    // Data & Logic
    private ClinicAdapter adapter;
    private List<Clinic> clinicList;
    private List<Clinic> allClinics;
    private ClinicRepository clinicRepository;
    private LocationManager locationManager;

    // Location
    private double userLatitude = 21.0285; // Default: Hà Nội
    private double userLongitude = 105.8542;
    private boolean locationEnabled = false;

    // Search params
    private String currentSearchQuery = "";
    private String currentSpecialty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_clinic_search_osm);

        // Initialize
        clinicRepository = new ClinicRepository();
        clinicList = new ArrayList<>();
        allClinics = clinicRepository.getAllClinics();

        initViews();
        setupRecyclerView();
        setupMap();
        setupClickListeners();
        requestLocationPermission();
        loadSpecialties();

        // Initial load
        loadAllClinics();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        etSearch = findViewById(R.id.et_search);
        actvSpecialty = findViewById(R.id.actv_specialty);
        btnSearch = findViewById(R.id.btn_search);
        btnToggleView = findViewById(R.id.btn_toggle_view);
        btnMyLocation = findViewById(R.id.btn_my_location);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        tvLocationStatus = findViewById(R.id.tv_location_status);
        mapView = findViewById(R.id.map_view);

        // Set default location status
        updateLocationStatus();

        // Initially show list view
        showListView();
    }

    private void setupRecyclerView() {
        adapter = new ClinicAdapter(clinicList, this::onClinicClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupMap() {
        // Set tile source
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        // Enable zoom controls
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set center to Hanoi
        GeoPoint hanoi = new GeoPoint(21.0285, 105.8542);
        mapView.getController().setCenter(hanoi);
        mapView.getController().setZoom(12.0);

        // Add my location overlay
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            currentSearchQuery = etSearch.getText().toString().trim();
            currentSpecialty = actvSpecialty.getText().toString().trim();
            searchClinics();
        });

        btnToggleView.setOnClickListener(v -> toggleView());

        btnMyLocation.setOnClickListener(v -> moveToMyLocation());
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Cần quyền vị trí để tìm phòng khám gần nhất", Toast.LENGTH_LONG).show();
                locationEnabled = false;
                updateLocationStatus();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Try GPS first
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null && isLocationFresh(gpsLocation)) {
                userLatitude = gpsLocation.getLatitude();
                userLongitude = gpsLocation.getLongitude();
                locationEnabled = true;
                Log.d(TAG, "GPS Location: " + userLatitude + ", " + userLongitude);
                updateLocationStatus();
                updateMapCenter();
                return;
            }

            // Fallback to Network
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (networkLocation != null && isLocationFresh(networkLocation)) {
                userLatitude = networkLocation.getLatitude();
                userLongitude = networkLocation.getLongitude();
                locationEnabled = true;
                Log.d(TAG, "Network Location: " + userLatitude + ", " + userLongitude);
                updateLocationStatus();
                updateMapCenter();
                return;
            }

            // Use default location
            Log.d(TAG, "Using default Hà Nội location");
            locationEnabled = false;
            updateLocationStatus();

        } catch (Exception e) {
            Log.e(TAG, "Error getting location: " + e.getMessage());
            Toast.makeText(this, "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();
            locationEnabled = false;
            updateLocationStatus();
        }
    }

    private boolean isLocationFresh(Location location) {
        // Consider location fresh if it's less than 10 minutes old
        long tenMinutesAgo = System.currentTimeMillis() - (10 * 60 * 1000);
        return location.getTime() > tenMinutesAgo;
    }

    private void updateLocationStatus() {
        if (tvLocationStatus != null) {
            if (locationEnabled) {
                tvLocationStatus.setText("📍 Đang tìm gần vị trí của bạn");
                tvLocationStatus.setTextColor(getColor(android.R.color.holo_green_dark));
            } else {
                tvLocationStatus.setText("📍 Sử dụng vị trí mặc định (Hà Nội)");
                tvLocationStatus.setTextColor(getColor(android.R.color.holo_orange_dark));
            }
        }
    }

    private void updateMapCenter() {
        if (mapView != null) {
            GeoPoint userLocation = new GeoPoint(userLatitude, userLongitude);
            mapView.getController().animateTo(userLocation);
        }
    }

    private void moveToMyLocation() {
        if (locationEnabled) {
            GeoPoint myLocation = new GeoPoint(userLatitude, userLongitude);
            mapView.getController().animateTo(myLocation);
            mapView.getController().setZoom(15.0);
            Toast.makeText(this, "Đã di chuyển đến vị trí của bạn", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không thể xác định vị trí của bạn", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSpecialties() {
        List<String> specialties = clinicRepository.getAllSpecialties();
        specialties.add(0, "Tất cả chuyên khoa");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                specialties
        );
        actvSpecialty.setAdapter(adapter);
    }

    private void searchClinics() {
        showLoading(true);

        if (currentSearchQuery.isEmpty() && (currentSpecialty.isEmpty() || currentSpecialty.equals("Tất cả chuyên khoa"))) {
            loadAllClinics();
            return;
        }

        // Filter clinics
        List<Clinic> filteredClinics = filterClinics(allClinics, currentSearchQuery, currentSpecialty);

        // Sort by distance if location is available
        if (locationEnabled) {
            filteredClinics = sortByDistance(filteredClinics);
        }

        showLoading(false);
        updateClinicList(filteredClinics);

        if (filteredClinics.isEmpty()) {
            showEmptyState("Không tìm thấy phòng khám phù hợp\nThử tìm kiếm với từ khóa khác");
        } else {
            showEmptyState(null);
            Toast.makeText(this, "Tìm thấy " + filteredClinics.size() + " phòng khám", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Clinic> filterClinics(List<Clinic> clinics, String query, String specialty) {
        List<Clinic> filtered = new ArrayList<>();

        for (Clinic clinic : clinics) {
            boolean matchesName = query.isEmpty() ||
                    clinic.getName().toLowerCase().contains(query.toLowerCase()) ||
                    clinic.getAddress().toLowerCase().contains(query.toLowerCase());

            boolean matchesSpecialty = specialty.isEmpty() ||
                    specialty.equals("Tất cả chuyên khoa") ||
                    clinic.hasSpecialty(specialty);

            if (matchesName && matchesSpecialty) {
                filtered.add(clinic);
            }
        }

        return filtered;
    }

    private List<Clinic> sortByDistance(List<Clinic> clinics) {
        List<ClinicWithDistance> clinicsWithDistance = new ArrayList<>();

        for (Clinic clinic : clinics) {
            double distance = calculateDistance(userLatitude, userLongitude,
                    clinic.getLatitude(), clinic.getLongitude());
            clinicsWithDistance.add(new ClinicWithDistance(clinic, distance));
        }

        // Sort by distance
        clinicsWithDistance.sort((c1, c2) -> Double.compare(c1.distance, c2.distance));

        List<Clinic> sortedClinics = new ArrayList<>();
        for (ClinicWithDistance cwd : clinicsWithDistance) {
            sortedClinics.add(cwd.clinic);
        }

        return sortedClinics;
    }

    private void loadAllClinics() {
        showLoading(true);

        List<Clinic> clinics = new ArrayList<>(allClinics);

        // Sort by distance if location available
        if (locationEnabled) {
            clinics = sortByDistance(clinics);
        }

        showLoading(false);
        updateClinicList(clinics);

        if (clinics.isEmpty()) {
            showEmptyState("Chưa có dữ liệu phòng khám");
        } else {
            showEmptyState(null);
        }
    }

    private void updateClinicList(List<Clinic> newClinics) {
        clinicList.clear();
        clinicList.addAll(newClinics);
        adapter.notifyDataSetChanged();

        // Update map markers
        updateMapMarkers(newClinics);

        // Update empty state
        if (clinicList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
        }
    }

    private void updateMapMarkers(List<Clinic> clinics) {
        // Clear existing markers (except my location)
        mapView.getOverlays().clear();
        mapView.getOverlays().add(myLocationOverlay);

        // Add clinic markers
        for (Clinic clinic : clinics) {
            GeoPoint clinicPoint = new GeoPoint(clinic.getLatitude(), clinic.getLongitude());

            Marker marker = new Marker(mapView);
            marker.setPosition(clinicPoint);
            marker.setTitle(clinic.getName());

            // Create snippet with distance if location available
            String snippet = clinic.getAddress();
            if (locationEnabled) {
                double distance = calculateDistance(userLatitude, userLongitude,
                        clinic.getLatitude(), clinic.getLongitude());
                snippet += String.format("\n📍 Cách bạn %.1f km", distance);
            }
            marker.setSnippet(snippet);

            // Set marker icon
            try {
                marker.setIcon(getDrawable(R.drawable.ic_launcher_foreground));
            } catch (Exception e) {
                // Use default marker if custom icon not available
            }

            mapView.getOverlays().add(marker);
        }

        mapView.invalidate();
    }

    private void toggleView() {
        if (isMapView) {
            showListView();
        } else {
            showMapView();
        }
    }

    private void showListView() {
        isMapView = false;
        recyclerView.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.GONE);
        btnToggleView.setText("🗺️ Bản đồ");
        btnMyLocation.setVisibility(View.GONE);
    }

    private void showMapView() {
        isMapView = true;
        recyclerView.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
        btnToggleView.setText("📋 Danh sách");
        btnMyLocation.setVisibility(View.VISIBLE);
    }

    private void onClinicClick(Clinic clinic) {
        // Navigate to clinic detail or show info
        String message = String.format(
                "🏥 %s\n📍 %s\n⭐ %.1f (%d đánh giá)",
                clinic.getName(),
                clinic.getAddress(),
                clinic.getRating(),
                clinic.getReviewCount()
        );

        if (locationEnabled) {
            double distance = calculateDistance(userLatitude, userLongitude,
                    clinic.getLatitude(), clinic.getLongitude());
            message += String.format("\n🚗 Cách bạn %.1f km", distance);
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // distance in km

        return distance;
    }

    private void showLoading(boolean show) {
        if (progressBar != null && recyclerView != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);

            if (tvEmptyState != null && show) {
                tvEmptyState.setVisibility(View.GONE);
            }
        }
    }

    private void showEmptyState(String message) {
        if (tvEmptyState != null) {
            if (message != null) {
                tvEmptyState.setText(message);
                tvEmptyState.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvEmptyState.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    // Helper class for distance calculation
    private static class ClinicWithDistance {
        Clinic clinic;
        double distance;

        ClinicWithDistance(Clinic clinic, double distance) {
            this.clinic = clinic;
            this.distance = distance;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDetach();
        }
    }
}