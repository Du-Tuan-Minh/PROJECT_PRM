// UPDATED FILE: app/src/main/java/com/example/project_prm/DataManager/DAO/AppointmentDAO.java
package com.example.project_prm.DataManager.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.AppointmentStatus;
import com.example.project_prm.DataManager.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentDAO {
    private final SQLiteDatabase db;

    public AppointmentDAO(SQLiteDatabase db) {
        this.db = db;
    }

    // ========== EXISTING METHODS (UPDATED) ==========

    public long addAppointment(int userId, String clinic, String doctor, String date, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_USER_ID, userId);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_CLINIC, clinic);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR, doctor);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DATE, date);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TIME, time);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_STATUS, AppointmentStatus.PENDING.getValue());
        values.put(DatabaseHelper.COLUMN_CREATED_AT, getCurrentTimestamp());
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, getCurrentTimestamp());
        return db.insert(DatabaseHelper.TABLE_APPOINTMENT, null, values);
    }

    // NEW METHOD: Add appointment with full patient information
    public long addAppointmentWithPatientInfo(Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_USER_ID, appointment.getUserId());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_CLINIC, appointment.getClinic());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR, appointment.getDoctor());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DATE, appointment.getDate());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TIME, appointment.getTime());

        // Patient Information
        values.put(DatabaseHelper.COLUMN_PATIENT_NAME, appointment.getPatientName());
        values.put(DatabaseHelper.COLUMN_PATIENT_PHONE, appointment.getPatientPhone());
        values.put(DatabaseHelper.COLUMN_PATIENT_AGE, appointment.getPatientAge());
        values.put(DatabaseHelper.COLUMN_PATIENT_GENDER, appointment.getPatientGender());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_SYMPTOMS, appointment.getSymptoms());
        values.put(DatabaseHelper.COLUMN_MEDICAL_HISTORY, appointment.getMedicalHistory());

        // Appointment Details
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TYPE, appointment.getAppointmentType());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_FEE, appointment.getAppointmentFee());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_NOTES, appointment.getNotes());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_STATUS, appointment.getStatus());

        // Timestamps
        values.put(DatabaseHelper.COLUMN_CREATED_AT, getCurrentTimestamp());
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, getCurrentTimestamp());

        return db.insert(DatabaseHelper.TABLE_APPOINTMENT, null, values);
    }

    public List<Appointment> getAppointmentsByUserId(int userId) {
        List<Appointment> appointments = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_APPOINTMENT, getAllColumns(),
                DatabaseHelper.COLUMN_APPOINTMENT_USER_ID + "=?", new String[]{String.valueOf(userId)},
                null, null, DatabaseHelper.COLUMN_CREATED_AT + " DESC");

        if (cursor.moveToFirst()) {
            do {
                appointments.add(cursorToAppointment(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }

    // ========== NEW METHODS FOR TUNG'S FEATURES ==========

    // Chức năng 9: Lịch sử theo status
    public List<Appointment> getAppointmentsByStatus(int userId, String status) {
        List<Appointment> appointments = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_APPOINTMENT, getAllColumns(),
                DatabaseHelper.COLUMN_APPOINTMENT_USER_ID + "=? AND " + DatabaseHelper.COLUMN_APPOINTMENT_STATUS + "=?",
                new String[]{String.valueOf(userId), status},
                null, null, DatabaseHelper.COLUMN_CREATED_AT + " DESC");

        if (cursor.moveToFirst()) {
            do {
                appointments.add(cursorToAppointment(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }

    public List<Appointment> getPendingAppointments(int userId) {
        return getAppointmentsByStatus(userId, AppointmentStatus.PENDING.getValue());
    }

    public List<Appointment> getUpcomingAppointments(int userId) {
        return getAppointmentsByStatus(userId, AppointmentStatus.UPCOMING.getValue());
    }

    public List<Appointment> getCompletedAppointments(int userId) {
        return getAppointmentsByStatus(userId, AppointmentStatus.COMPLETED.getValue());
    }

    public List<Appointment> getCancelledAppointments(int userId) {
        return getAppointmentsByStatus(userId, AppointmentStatus.CANCELLED.getValue());
    }

    // Cancel appointment
    public boolean cancelAppointment(int appointmentId, String reason) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_STATUS, AppointmentStatus.CANCELLED.getValue());
        values.put(DatabaseHelper.COLUMN_CANCELLATION_REASON, reason);
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, getCurrentTimestamp());

        int rowsAffected = db.update(DatabaseHelper.TABLE_APPOINTMENT, values,
                DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointmentId)});
        return rowsAffected > 0;
    }

    // Reschedule appointment
    public boolean rescheduleAppointment(int appointmentId, String newDate, String newTime) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DATE, newDate);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TIME, newTime);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_STATUS, AppointmentStatus.RESCHEDULED.getValue());
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, getCurrentTimestamp());

        int rowsAffected = db.update(DatabaseHelper.TABLE_APPOINTMENT, values,
                DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointmentId)});
        return rowsAffected > 0;
    }

    // Add feedback and rating
    public boolean addFeedback(int appointmentId, int rating, String feedback) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RATING, rating);
        values.put(DatabaseHelper.COLUMN_FEEDBACK, feedback);
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, getCurrentTimestamp());

        int rowsAffected = db.update(DatabaseHelper.TABLE_APPOINTMENT, values,
                DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointmentId)});
        return rowsAffected > 0;
    }

    // Update appointment status
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_STATUS, status);
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, getCurrentTimestamp());

        int rowsAffected = db.update(DatabaseHelper.TABLE_APPOINTMENT, values,
                DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointmentId)});
        return rowsAffected > 0;
    }

    // Get appointment by ID
    public Appointment getAppointmentById(int appointmentId) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_APPOINTMENT, getAllColumns(),
                DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointmentId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Appointment appointment = cursorToAppointment(cursor);
            cursor.close();
            return appointment;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    // EXISTING METHOD (UPDATED to support new fields)
    public int updateAppointment(int id, int userId, String clinic, String doctor, String date, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_USER_ID, userId);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_CLINIC, clinic);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR, doctor);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DATE, date);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TIME, time);
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, getCurrentTimestamp());

        return db.update(DatabaseHelper.TABLE_APPOINTMENT, values,
                DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?", new String[]{String.valueOf(id)});
    }

    // NEW METHOD: Update full appointment
    public boolean updateFullAppointment(Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_USER_ID, appointment.getUserId());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_CLINIC, appointment.getClinic());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR, appointment.getDoctor());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DATE, appointment.getDate());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TIME, appointment.getTime());

        // Patient Information
        values.put(DatabaseHelper.COLUMN_PATIENT_NAME, appointment.getPatientName());
        values.put(DatabaseHelper.COLUMN_PATIENT_PHONE, appointment.getPatientPhone());
        values.put(DatabaseHelper.COLUMN_PATIENT_AGE, appointment.getPatientAge());
        values.put(DatabaseHelper.COLUMN_PATIENT_GENDER, appointment.getPatientGender());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_SYMPTOMS, appointment.getSymptoms());
        values.put(DatabaseHelper.COLUMN_MEDICAL_HISTORY, appointment.getMedicalHistory());

        // Appointment Details
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TYPE, appointment.getAppointmentType());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_FEE, appointment.getAppointmentFee());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_NOTES, appointment.getNotes());
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_STATUS, appointment.getStatus());
        values.put(DatabaseHelper.COLUMN_CANCELLATION_REASON, appointment.getCancellationReason());
        values.put(DatabaseHelper.COLUMN_RATING, appointment.getRating());
        values.put(DatabaseHelper.COLUMN_FEEDBACK, appointment.getFeedback());
        values.put(DatabaseHelper.COLUMN_ORIGINAL_APPOINTMENT_ID, appointment.getOriginalAppointmentId());

        // Timestamps
        values.put(DatabaseHelper.COLUMN_UPDATED_AT, getCurrentTimestamp());

        int rowsAffected = db.update(DatabaseHelper.TABLE_APPOINTMENT, values,
                DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointment.getId())});
        return rowsAffected > 0;
    }

    public void deleteAppointment(int id) {
        db.delete(DatabaseHelper.TABLE_APPOINTMENT, DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    // ========== HELPER METHODS ==========

    private String[] getAllColumns() {
        return new String[]{
                DatabaseHelper.COLUMN_APPOINTMENT_ID,
                DatabaseHelper.COLUMN_APPOINTMENT_USER_ID,
                DatabaseHelper.COLUMN_APPOINTMENT_CLINIC,
                DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR,
                DatabaseHelper.COLUMN_APPOINTMENT_DATE,
                DatabaseHelper.COLUMN_APPOINTMENT_TIME,
                DatabaseHelper.COLUMN_PATIENT_NAME,
                DatabaseHelper.COLUMN_PATIENT_PHONE,
                DatabaseHelper.COLUMN_PATIENT_AGE,
                DatabaseHelper.COLUMN_PATIENT_GENDER,
                DatabaseHelper.COLUMN_APPOINTMENT_SYMPTOMS,
                DatabaseHelper.COLUMN_MEDICAL_HISTORY,
                DatabaseHelper.COLUMN_APPOINTMENT_TYPE,
                DatabaseHelper.COLUMN_APPOINTMENT_FEE,
                DatabaseHelper.COLUMN_APPOINTMENT_NOTES,
                DatabaseHelper.COLUMN_APPOINTMENT_STATUS,
                DatabaseHelper.COLUMN_CANCELLATION_REASON,
                DatabaseHelper.COLUMN_RATING,
                DatabaseHelper.COLUMN_FEEDBACK,
                DatabaseHelper.COLUMN_ORIGINAL_APPOINTMENT_ID,
                DatabaseHelper.COLUMN_CREATED_AT,
                DatabaseHelper.COLUMN_UPDATED_AT
        };
    }

    private Appointment cursorToAppointment(Cursor cursor) {
        Appointment appointment = new Appointment();

        // Basic fields
        appointment.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_ID)));
        appointment.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_USER_ID)));
        appointment.setClinic(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_CLINIC)));
        appointment.setDoctor(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR)));
        appointment.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_DATE)));
        appointment.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_TIME)));

        // Patient Information
        appointment.setPatientName(getStringFromCursor(cursor, DatabaseHelper.COLUMN_PATIENT_NAME));
        appointment.setPatientPhone(getStringFromCursor(cursor, DatabaseHelper.COLUMN_PATIENT_PHONE));
        appointment.setPatientAge(getStringFromCursor(cursor, DatabaseHelper.COLUMN_PATIENT_AGE));
        appointment.setPatientGender(getStringFromCursor(cursor, DatabaseHelper.COLUMN_PATIENT_GENDER));
        appointment.setSymptoms(getStringFromCursor(cursor, DatabaseHelper.COLUMN_APPOINTMENT_SYMPTOMS));
        appointment.setMedicalHistory(getStringFromCursor(cursor, DatabaseHelper.COLUMN_MEDICAL_HISTORY));

        // Appointment Details
        appointment.setAppointmentType(getStringFromCursor(cursor, DatabaseHelper.COLUMN_APPOINTMENT_TYPE));
        appointment.setAppointmentFee(getDoubleFromCursor(cursor, DatabaseHelper.COLUMN_APPOINTMENT_FEE));
        appointment.setNotes(getStringFromCursor(cursor, DatabaseHelper.COLUMN_APPOINTMENT_NOTES));
        appointment.setStatus(getStringFromCursor(cursor, DatabaseHelper.COLUMN_APPOINTMENT_STATUS));

        // Feedback & History
        appointment.setCancellationReason(getStringFromCursor(cursor, DatabaseHelper.COLUMN_CANCELLATION_REASON));
        appointment.setRating(getIntFromCursor(cursor, DatabaseHelper.COLUMN_RATING));
        appointment.setFeedback(getStringFromCursor(cursor, DatabaseHelper.COLUMN_FEEDBACK));
        appointment.setOriginalAppointmentId(getStringFromCursor(cursor, DatabaseHelper.COLUMN_ORIGINAL_APPOINTMENT_ID));

        // Timestamps
        appointment.setCreatedAt(getStringFromCursor(cursor, DatabaseHelper.COLUMN_CREATED_AT));
        appointment.setUpdatedAt(getStringFromCursor(cursor, DatabaseHelper.COLUMN_UPDATED_AT));

        return appointment;
    }

    private String getStringFromCursor(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            return (columnIndex != -1) ? cursor.getString(columnIndex) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private int getIntFromCursor(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            return (columnIndex != -1) ? cursor.getInt(columnIndex) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private double getDoubleFromCursor(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            return (columnIndex != -1) ? cursor.getDouble(columnIndex) : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    // ========== STATISTICS METHODS ==========

    public int getTotalAppointmentsByUser(int userId) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_APPOINTMENT +
                        " WHERE " + DatabaseHelper.COLUMN_APPOINTMENT_USER_ID + "=?",
                new String[]{String.valueOf(userId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getAppointmentCountByStatus(int userId, String status) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_APPOINTMENT +
                        " WHERE " + DatabaseHelper.COLUMN_APPOINTMENT_USER_ID + "=? AND " +
                        DatabaseHelper.COLUMN_APPOINTMENT_STATUS + "=?",
                new String[]{String.valueOf(userId), status});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Get appointments within date range
    public List<Appointment> getAppointmentsByDateRange(int userId, String startDate, String endDate) {
        List<Appointment> appointments = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_APPOINTMENT, getAllColumns(),
                DatabaseHelper.COLUMN_APPOINTMENT_USER_ID + "=? AND " +
                        DatabaseHelper.COLUMN_APPOINTMENT_DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(userId), startDate, endDate},
                null, null, DatabaseHelper.COLUMN_APPOINTMENT_DATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                appointments.add(cursorToAppointment(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }
}