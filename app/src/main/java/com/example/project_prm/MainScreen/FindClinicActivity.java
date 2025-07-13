package com.example.project_prm.MainScreen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.project_prm.R;
import java.util.ArrayList;
import java.util.List;

public class FindClinicActivity extends AppCompatActivity implements OnMapReadyCallback {
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final LatLng HANOI_CENTER = new LatLng(21.0285, 105.8542);
    
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SupportMapFragment mapFragment;
    
    private ImageView ivBack, ivSearch;
    private TextView tvTitle;
    private EditText etSearch;
    private ChipGroup chipGroupFilter;
    private RecyclerView rvClinics;
    private MaterialButton btnMyLocation;
    
    private ClinicAdapter adapter;
    private List<ClinicModel> allClinics;
    private List<ClinicModel> filteredClinics;
    private List<Marker> mapMarkers = new ArrayList<>();
    
    private ClinicRepository clinicRepository;
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_clinic);
        
        clinicRepository = ClinicRepository.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        initViews();
        setupListeners();
        setupMap();
        loadClinics();
    }

    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        ivSearch = findViewById(R.id.ivSearch);
        tvTitle = findViewById(R.id.tvTitle);
        etSearch = findViewById(R.id.etSearch);
        chipGroupFilter = findViewById(R.id.chipGroupFilter);
        rvClinics = findViewById(R.id.rvClinics);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        
        tvTitle.setText("Tìm phòng khám");
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());
        
        ivSearch.setOnClickListener(v -> {
            etSearch.setVisibility(etSearch.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        
        btnMyLocation.setOnClickListener(v -> {
            if (checkLocationPermission()) {
                getCurrentLocation();
            } else {
                requestLocationPermission();
            }
        });
        
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterClinics(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        setupFilterChips();
    }

    private void setupFilterChips() {
        // Distance filter chips
        Chip chipAll = findViewById(R.id.chipAll);
        Chip chipNearby = findViewById(R.id.chipNearby);
        Chip chipRating = findViewById(R.id.chipRating);
        
        chipAll.setOnClickListener(v -> {
            showAllClinics();
            updateMapMarkers(allClinics);
        });
        
        chipNearby.setOnClickListener(v -> {
            showNearbyClinics();
            updateMapMarkers(filteredClinics);
        });
        
        chipRating.setOnClickListener(v -> {
            showTopRatedClinics();
            updateMapMarkers(filteredClinics);
        });
    }

    private void setupMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        
        // Set default location (Hà Nội)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HANOI_CENTER, 12));
        
        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        
        // Check location permission
        if (checkLocationPermission()) {
            getCurrentLocation();
        }
        
        // Load clinics on map
        loadClinicsOnMap();
    }

    private void loadClinics() {
        allClinics = clinicRepository.getAllClinics();
        filteredClinics = new ArrayList<>(allClinics);
        
        adapter = new ClinicAdapter(filteredClinics, new ClinicAdapter.OnClinicClickListener() {
            @Override
            public void onClinicClick(ClinicModel clinic) {
                // Move camera to clinic location
                if (mMap != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(clinic.getLocation(), 15));
                }
            }
            
            @Override
            public void onCallClick(ClinicModel clinic) {
                // Handle call action
            }
            
            @Override
            public void onDirectionClick(ClinicModel clinic) {
                // Handle direction action
            }
        });
        
        rvClinics.setLayoutManager(new LinearLayoutManager(this));
        rvClinics.setAdapter(adapter);
    }

    private void loadClinicsOnMap() {
        if (mMap == null) return;
        
        // Clear existing markers
        for (Marker marker : mapMarkers) {
            marker.remove();
        }
        mapMarkers.clear();
        
        // Add markers for all clinics
        for (ClinicModel clinic : allClinics) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(clinic.getLocation())
                    .title(clinic.getName())
                    .snippet(clinic.getAddress())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_clinic_marker));
            
            Marker marker = mMap.addMarker(markerOptions);
            mapMarkers.add(marker);
        }
    }

    private void updateMapMarkers(List<ClinicModel> clinics) {
        // Clear existing markers
        for (Marker marker : mapMarkers) {
            marker.remove();
        }
        mapMarkers.clear();
        
        // Add markers for filtered clinics
        for (ClinicModel clinic : clinics) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(clinic.getLocation())
                    .title(clinic.getName())
                    .snippet(clinic.getAddress())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_clinic_marker));
            
            Marker marker = mMap.addMarker(markerOptions);
            mapMarkers.add(marker);
        }
    }

    private void filterClinics(String query) {
        if (query.trim().isEmpty()) {
            filteredClinics = new ArrayList<>(allClinics);
        } else {
            filteredClinics = clinicRepository.searchClinics(query);
        }
        adapter.updateClinics(filteredClinics);
        updateMapMarkers(filteredClinics);
    }

    private void showAllClinics() {
        filteredClinics = new ArrayList<>(allClinics);
        adapter.updateClinics(filteredClinics);
    }

    private void showNearbyClinics() {
        filteredClinics = clinicRepository.getClinicsByDistance(5.0); // Within 5km
        adapter.updateClinics(filteredClinics);
    }

    private void showTopRatedClinics() {
        filteredClinics = clinicRepository.getClinicsByRating(4.0); // 4+ stars
        adapter.updateClinics(filteredClinics);
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        clinicRepository.setUserLocation(userLocation);
                        
                        // Move camera to user location
                        if (mMap != null) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
                        }
                        
                        // Refresh clinic list with updated distances
                        allClinics = clinicRepository.getAllClinics();
                        filteredClinics = new ArrayList<>(allClinics);
                        adapter.updateClinics(filteredClinics);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
}