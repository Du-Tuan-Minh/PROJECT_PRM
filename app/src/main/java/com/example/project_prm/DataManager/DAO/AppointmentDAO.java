package com.example.project_prm.DataManager.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private final SQLiteDatabase db;

    public AppointmentDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long addAppointment(int userId, String clinic, String doctor, String date, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_USER_ID, userId);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_CLINIC, clinic);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR, doctor);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DATE, date);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TIME, time);
        return db.insert(DatabaseHelper.TABLE_APPOINTMENT, null, values);
    }

    public List<Appointment> getAppointmentsByUserId(int userId) {
        List<Appointment> appointments = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_APPOINTMENT, new String[]{
                        DatabaseHelper.COLUMN_APPOINTMENT_ID, DatabaseHelper.COLUMN_APPOINTMENT_USER_ID,
                        DatabaseHelper.COLUMN_APPOINTMENT_CLINIC, DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR,
                        DatabaseHelper.COLUMN_APPOINTMENT_DATE, DatabaseHelper.COLUMN_APPOINTMENT_TIME},
                DatabaseHelper.COLUMN_APPOINTMENT_USER_ID + "=?", new String[]{String.valueOf(userId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment();
                appointment.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_ID)));
                appointment.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_USER_ID)));
                appointment.setClinic(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_CLINIC)));
                appointment.setDoctor(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR)));
                appointment.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_DATE)));
                appointment.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_APPOINTMENT_TIME)));
                appointments.add(appointment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }

    public int updateAppointment(int id, int userId, String clinic, String doctor, String date, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_USER_ID, userId);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_CLINIC, clinic);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR, doctor);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DATE, date);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TIME, time);
        return db.update(DatabaseHelper.TABLE_APPOINTMENT, values, DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteAppointment(int id) {
        db.delete(DatabaseHelper.TABLE_APPOINTMENT, DatabaseHelper.COLUMN_APPOINTMENT_ID + "=?", new String[]{String.valueOf(id)});
    }
}