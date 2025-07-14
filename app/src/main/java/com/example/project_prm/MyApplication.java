package com.example.project_prm;

import android.app.Application;
import android.util.Log;

import com.example.project_prm.DataManager.PatientDataManager;
import com.example.project_prm.MainScreen.AppointmentRepository;
import com.example.project_prm.MainScreen.DoctorRepository;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        
        try {
            // Initialize data managers
            PatientDataManager.getInstance(this);
            AppointmentRepository.getInstance();
            DoctorRepository.getInstance();
            
            Log.d(TAG, "Application initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing application: " + e.getMessage());
        }
    }
} 