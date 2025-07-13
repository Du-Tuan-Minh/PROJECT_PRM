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
import com.example.project_prm.ui.SearchScreen.ClinicSearchAdapter;
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
    private MaterialButton btnSearch, btnToggleView, btnMyLocation, btnDistanceFilter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmptyState, tvLocationStatus;
    private ImageView ivBack;

    // OSMDroid Map
    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private boolean isMapView = false;

    // Data & Logic
    private ClinicSearchAdapter adapter;
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
    private double currentDistanceFilter = 0; // 0 = no filter, >0 = max distance in km

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
        btnDistanceFilter = findViewById(R.id.btn_distance_filter);
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
        adapter = new ClinicSearchAdapter(clinicList, new ClinicSearchAdapter.OnClinicClickListener() {
            @Override
            public void onClinicClick(Clinic clinic) {
                ClinicSearchActivity.this.onClinicClick(clinic);
            }

            @Override
            public void onBookAppointmentClick(Clinic clinic) {
                // Navigate to booking activity
                Toast.makeText(ClinicSearchActivity.this, "Đặt lịch với " + clinic.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        // Update adapter with user location
        updateAdapterLocation();
    }

    private void updateAdapterLocation() {
        if (adapter != null) {
            adapter.setUserLocation(userLatitude, userLongitude, locationEnabled);
            adapter.notifyDataSetChanged();
        }
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

        btnDistanceFilter.setOnClickListener(v -> showDistanceFilterDialog());
    }

    // Method to set distance filter (can be called from UI)
    public void setDistanceFilter(double maxDistanceKm) {
        this.currentDistanceFilter = maxDistanceKm;
        searchClinics(); // Re-search with new filter
    }

    // Method to clear distance filter
    public void clearDistanceFilter() {
        this.currentDistanceFilter = 0;
        searchClinics(); // Re-search without distance filter
    }

    // Method to show distance filter dialog
    public void showDistanceFilterDialog() {
        String[] distances = {"Tất cả khoảng cách", "Dưới 2 km", "Dưới 5 km", "Dưới 10 km"};
        double[] distanceValues = {0, 2, 5, 10};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Chọn khoảng cách");
        builder.setItems(distances, (dialog, which) -> {
            setDistanceFilter(distanceValues[which]);
            Toast.makeText(this, "Đã chọn: " + distances[which], Toast.LENGTH_SHORT).show();
        });
        builder.show();
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
        if (locationEnabled) {
            tvLocationStatus.setText(String.format("📍 Vị trí: %.4f, %.4f", userLatitude, userLongitude));
            tvLocationStatus.setTextColor(getColor(android.R.color.holo_green_dark));
        } else {
            tvLocationStatus.setText("📍 Đang tìm vị trí của bạn...");
            tvLocationStatus.setTextColor(getColor(android.R.color.holo_orange_dark));
        }
        
        // Update adapter with new location
        updateAdapterLocation();
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

        // Get search parameters
        String query = etSearch.getText().toString().trim();
        String specialty = actvSpecialty.getText().toString().trim();

        // Apply filters
        List<Clinic> filteredClinics = filterClinics(allClinics, query, specialty);

        // Sort by distance if location available
        if (locationEnabled) {
            filteredClinics = sortByDistance(filteredClinics);
        }

        showLoading(false);
        updateClinicList(filteredClinics);

        // Show result count
        String resultMessage = String.format("Tìm thấy %d phòng khám", filteredClinics.size());
        if (locationEnabled && currentDistanceFilter > 0) {
            resultMessage += String.format(" trong bán kính %.1f km", currentDistanceFilter);
        }
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
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

            // Filter by distance if location is available and distance filter is set
            boolean matchesDistance = true;
            if (locationEnabled && currentDistanceFilter > 0) {
                double distance = calculateDistance(userLatitude, userLongitude,
                        clinic.getLatitude(), clinic.getLongitude());
                matchesDistance = distance <= currentDistanceFilter;
            }

            if (matchesName && matchesSpecialty && matchesDistance) {
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
        
        // Update adapter with current location
        updateAdapterLocation();

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