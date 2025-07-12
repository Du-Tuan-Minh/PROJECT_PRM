// NEW FILE: app/src/main/java/com/example/project_prm/Services/HealthcareService.java
package com.example.project_prm.Services;

import android.content.Context;

import com.example.project_prm.DataManager.BookingManager.AppointmentBookingManager;
import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.Disease;
import com.example.project_prm.DataManager.HistoryManager.AppointmentHistoryManager;
import com.example.project_prm.DataManager.SearchManager.ClinicSearchManager;
import com.example.project_prm.DataManager.SearchManager.DiseaseSearchManager;

import java.util.List;

/**
 * Service layer for healthcare features
 * Provides unified access to search, booking, and appointment history functionality
 */
public class HealthcareService {
    private static HealthcareService instance;

    private final AppointmentHistoryManager historyManager;
    private final ClinicSearchManager clinicSearchManager;
    private final DiseaseSearchManager diseaseSearchManager;
    private final AppointmentBookingManager bookingManager;
    private final Context context;

    private HealthcareService(Context context) {
        this.context = context.getApplicationContext();
        this.historyManager = new AppointmentHistoryManager(context);
        this.clinicSearchManager = new ClinicSearchManager();
        this.diseaseSearchManager = new DiseaseSearchManager(context);
        this.bookingManager = new AppointmentBookingManager(context);
    }

    public static synchronized HealthcareService getInstance(Context context) {
        if (instance == null) {
            instance = new HealthcareService(context);
        }
        return instance;
    }

    // ========== CHỨC NĂNG 6: SEARCH ==========

    /**
     * Search diseases by symptoms
     */
    public void searchDiseasesBySymptoms(String symptoms, DiseaseSearchManager.OnSearchCompleteListener listener) {
        diseaseSearchManager.searchBySymptomsAsync(symptoms, listener);
    }

    /**
     * Search diseases by name
     */
    public List<Disease> searchDiseasesByName(String name) {
        return diseaseSearchManager.searchByName(name);
    }

    /**
     * Search clinics by name
     */
    public void searchClinicsByName(String name, ClinicSearchManager.OnClinicSearchListener listener) {
        clinicSearchManager.searchByName(name, listener);
    }

    /**
     * Search clinics by specialty
     */
    public void searchClinicsBySpecialty(String specialty, ClinicSearchManager.OnClinicSearchListener listener) {
        clinicSearchManager.searchBySpecialty(specialty, listener);
    }

    /**
     * Search clinics by location
     */
    public void searchClinicsByLocation(double userLat, double userLng, double radiusKm, ClinicSearchManager.OnClinicSearchListener listener) {
        clinicSearchManager.searchByLocation(userLat, userLng, radiusKm, listener);
    }

    /**
     * Advanced search with filters
     */
    public void searchClinicsWithFilters(ClinicSearchManager.SearchFilter filter, ClinicSearchManager.OnClinicSearchListener listener) {
        clinicSearchManager.searchWithFilters(filter, listener);
    }

    /**
     * Get clinic details
     */
    public void getClinicDetails(String clinicId, ClinicSearchManager.OnClinicDetailListener listener) {
        clinicSearchManager.getClinicDetails(clinicId, listener);
    }

    /**
     * Get disease details
     */
    public void getDiseaseDetails(int diseaseId, DiseaseSearchManager.OnDiseaseDetailListener listener) {
        diseaseSearchManager.getDiseaseDetailsAsync(diseaseId, listener);
    }

    /**
     * Get all diseases with pagination
     */
    public List<Disease> getAllDiseases(int offset, int limit) {
        return diseaseSearchManager.getAllDiseases(offset, limit);
    }

    /**
     * Get related diseases
     */
    public List<Disease> getRelatedDiseases(int diseaseId, int limit) {
        return diseaseSearchManager.getRelatedDiseases(diseaseId, limit);
    }

    // ========== CHỨC NĂNG 8: APPOINTMENT BOOKING ==========

    /**
     * Get available time slots for booking
     */
    public void getAvailableTimeSlots(String clinicId, String date, AppointmentBookingManager.OnTimeSlotsListener listener) {
        bookingManager.getAvailableTimeSlots(clinicId, date, listener);
    }

    /**
     * Book appointment
     */
    public void bookAppointment(AppointmentBookingManager.AppointmentBookingRequest request, AppointmentBookingManager.OnBookingListener listener) {
        bookingManager.bookAppointment(request, listener);
    }

    /**
     * Calculate appointment fee
     */
    public void calculateAppointmentFee(String clinicId, String appointmentType, AppointmentBookingManager.OnFeeCalculationListener listener) {
        bookingManager.calculateAppointmentFee(clinicId, appointmentType, listener);
    }

    /**
     * Get available dates for booking
     */
    public List<String> getAvailableDates() {
        return bookingManager.getAvailableDates();
    }

    /**
     * Get appointment types
     */
    public List<AppointmentBookingManager.AppointmentType> getAppointmentTypes() {
        return bookingManager.getAppointmentTypes();
    }

    /**
     * Validate appointment data
     */
    public AppointmentBookingManager.ValidationResult validateAppointmentData(AppointmentBookingManager.AppointmentBookingRequest request) {
        return bookingManager.validateAppointmentData(request);
    }

    // ========== CHỨC NĂNG 9: APPOINTMENT HISTORY ==========

    /**
     * Get upcoming appointments
     */
    public void getUpcomingAppointments(int userId, AppointmentHistoryManager.OnHistoryListener listener) {
        historyManager.getUpcomingAppointments(userId, listener);
    }

    /**
     * Get completed appointments
     */
    public void getCompletedAppointments(int userId, AppointmentHistoryManager.OnHistoryListener listener) {
        historyManager.getCompletedAppointments(userId, listener);
    }

    /**
     * Get cancelled appointments
     */
    public void getCancelledAppointments(int userId, AppointmentHistoryManager.OnHistoryListener listener) {
        historyManager.getCancelledAppointments(userId, listener);
    }

    /**
     * Get pending appointments
     */
    public void getPendingAppointments(int userId, AppointmentHistoryManager.OnHistoryListener listener) {
        historyManager.getPendingAppointments(userId, listener);
    }

    /**
     * Cancel appointment
     */
    public void cancelAppointment(int appointmentId, String reason, AppointmentHistoryManager.OnActionListener listener) {
        historyManager.cancelAppointment(appointmentId, reason, listener);
    }

    /**
     * Reschedule appointment
     */
    public void rescheduleAppointment(int appointmentId, String newDate, String newTime, AppointmentHistoryManager.OnActionListener listener) {
        historyManager.rescheduleAppointment(appointmentId, newDate, newTime, listener);
    }

    /**
     * Add feedback for completed appointment
     */
    public void addAppointmentFeedback(int appointmentId, int rating, String feedback, AppointmentHistoryManager.OnActionListener listener) {
        historyManager.addFeedback(appointmentId, rating, feedback, listener);
    }

    /**
     * Get template for booking again
     */
    public void getBookAgainTemplate(int originalAppointmentId, AppointmentHistoryManager.OnBookAgainListener listener) {
        historyManager.bookAgain(originalAppointmentId, listener);
    }

    /**
     * Get appointment statistics
     */
    public void getAppointmentStatistics(int userId, AppointmentHistoryManager.OnStatisticsListener listener) {
        historyManager.getAppointmentStatistics(userId, listener);
    }

    // ========== CHỨC NĂNG 10: APPOINTMENT DETAIL ==========

    /**
     * Get appointment details by ID
     */
    public void getAppointmentDetails(int appointmentId, OnAppointmentDetailListener listener) {
        // This will be implemented with the DAO
        new Thread(() -> {
            try {
                // Get appointment from database
                // For now, we'll create a placeholder implementation
                // You can integrate with your existing DAO here
                listener.onError("Not implemented yet - integrate with DAO");
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }).start();
    }

    // ========== UTILITY METHODS ==========

    /**
     * Get available specialties for filter
     */
    public void getAvailableSpecialties(ClinicSearchManager.OnSpecialtiesListener listener) {
        clinicSearchManager.getAvailableSpecialties(listener);
    }

    /**
     * Get popular diseases
     */
    public List<Disease> getPopularDiseases(int limit) {
        return diseaseSearchManager.getPopularDiseases(limit);
    }

    /**
     * Close all managers and free resources
     */
    public void cleanup() {
        if (historyManager != null) {
            historyManager.close();
        }
        if (diseaseSearchManager != null) {
            diseaseSearchManager.close();
        }
        if (bookingManager != null) {
            bookingManager.close();
        }
    }

    // ========== CALLBACK INTERFACES ==========

    public interface OnAppointmentDetailListener {
        void onSuccess(Appointment appointment);
        void onError(String error);
    }
}