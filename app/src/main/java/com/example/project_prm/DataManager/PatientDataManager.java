package com.example.project_prm.DataManager;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.project_prm.Model.PatientInfo;
import com.example.project_prm.Model.ServicePackage;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PatientDataManager {
    private static PatientDataManager instance;
    private SharedPreferences preferences;
    private Gson gson;
    private static final String PREF_NAME = "patient_data";
    private static final String KEY_CURRENT_PATIENT = "current_patient";
    private static final String KEY_PATIENT_HISTORY = "patient_history";
    private static final String KEY_SERVICE_PACKAGES = "service_packages";

    private PatientDataManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        initializeDefaultServices();
    }

    public static synchronized PatientDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new PatientDataManager(context.getApplicationContext());
        }
        return instance;
    }

    public static PatientDataManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PatientDataManager must be initialized with context first");
        }
        return instance;
    }

    // Initialize default service packages
    private void initializeDefaultServices() {
        List<ServicePackage> existingServices = getServicePackages();
        if (existingServices.isEmpty()) {
            List<ServicePackage> defaultServices = createDefaultServices();
            saveServicePackages(defaultServices);
        }
    }

    private List<ServicePackage> createDefaultServices() {
        List<ServicePackage> services = new ArrayList<>();
        
        services.add(new ServicePackage("message", "Nhắn tin", 200000, "15 phút"));
        services.add(new ServicePackage("voice", "Gọi thoại", 300000, "30 phút"));
        services.add(new ServicePackage("video", "Gọi video", 500000, "45 phút"));
        services.add(new ServicePackage("in_person", "Khám trực tiếp", 800000, "60 phút"));
        
        return services;
    }

    // Patient Info Methods
    public void savePatientInfo(PatientInfo patientInfo) {
        String json = gson.toJson(patientInfo);
        preferences.edit().putString(KEY_CURRENT_PATIENT, json).apply();
        
        // Also save to history
        addToPatientHistory(patientInfo);
    }

    public PatientInfo getPatientInfo() {
        String json = preferences.getString(KEY_CURRENT_PATIENT, null);
        if (json != null) {
            return gson.fromJson(json, PatientInfo.class);
        }
        return null;
    }

    public void clearCurrentPatientInfo() {
        preferences.edit().remove(KEY_CURRENT_PATIENT).apply();
    }

    // Patient History Methods
    private void addToPatientHistory(PatientInfo patientInfo) {
        List<PatientInfo> history = getPatientHistory();
        
        // Remove if already exists (update scenario)
        history.removeIf(p -> p.getPhone() != null && p.getPhone().equals(patientInfo.getPhone()));
        
        // Add to beginning
        history.add(0, patientInfo);
        
        // Keep only last 10 records
        if (history.size() > 10) {
            history = history.subList(0, 10);
        }
        
        savePatientHistory(history);
    }

    public List<PatientInfo> getPatientHistory() {
        String json = preferences.getString(KEY_PATIENT_HISTORY, null);
        if (json != null) {
            Type type = new TypeToken<List<PatientInfo>>(){}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    private void savePatientHistory(List<PatientInfo> history) {
        String json = gson.toJson(history);
        preferences.edit().putString(KEY_PATIENT_HISTORY, json).apply();
    }

    public void clearPatientHistory() {
        preferences.edit().remove(KEY_PATIENT_HISTORY).apply();
    }

    // Service Package Methods
    public List<ServicePackage> getServicePackages() {
        String json = preferences.getString(KEY_SERVICE_PACKAGES, null);
        if (json != null) {
            Type type = new TypeToken<List<ServicePackage>>(){}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public List<ServicePackage> getAvailableServicePackages() {
        // Không có hàm isAvailable, trả về toàn bộ service
        return getServicePackages();
    }

    public ServicePackage getServicePackageById(String id) {
        List<ServicePackage> services = getServicePackages();
        for (ServicePackage service : services) {
            if (service.getId().equals(id)) {
                return service;
            }
        }
        return null;
    }

    public void saveServicePackages(List<ServicePackage> services) {
        String json = gson.toJson(services);
        preferences.edit().putString(KEY_SERVICE_PACKAGES, json).apply();
    }

    public void updateServicePackage(ServicePackage updatedService) {
        List<ServicePackage> services = getServicePackages();
        
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getId().equals(updatedService.getId())) {
                services.set(i, updatedService);
                break;
            }
        }
        
        saveServicePackages(services);
    }

    // Quick fill methods for returning patients
    public PatientInfo findPatientByPhone(String phone) {
        List<PatientInfo> history = getPatientHistory();
        for (PatientInfo patient : history) {
            if (patient.getPhone() != null && patient.getPhone().equals(phone)) {
                return patient;
            }
        }
        return null;
    }

    public List<String> getRecentPhoneNumbers() {
        List<PatientInfo> history = getPatientHistory();
        List<String> phones = new ArrayList<>();
        
        for (PatientInfo patient : history) {
            if (patient.getPhone() != null && !phones.contains(patient.getPhone())) {
                phones.add(patient.getPhone());
            }
        }
        
        return phones;
    }

    // Validation and utilities
    public boolean isPatientInfoComplete(PatientInfo patientInfo) {
        // Không có hàm isValid, chỉ kiểm tra các trường cơ bản
        return patientInfo != null
            && patientInfo.getName() != null && !patientInfo.getName().isEmpty()
            && patientInfo.getPhone() != null && !patientInfo.getPhone().isEmpty();
    }

    public void resetAllData() {
        preferences.edit().clear().apply();
        initializeDefaultServices();
    }

    // Statistics
    public int getTotalPatientsCount() {
        return getPatientHistory().size();
    }

    public ServicePackage getMostUsedService() {
        List<PatientInfo> history = getPatientHistory();
        if (history.isEmpty()) return null;

        // Không có getServicePackage, bỏ phần thống kê này hoặc trả về null
        return null;
    }
} 