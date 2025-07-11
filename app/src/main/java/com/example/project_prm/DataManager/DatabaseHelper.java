// UPDATED FILE: app/src/main/java/com/example/project_prm/DataManager/DatabaseHelper.java
package com.example.project_prm.DataManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HealthcareApp.db";
    private static final int DATABASE_VERSION = 3; // Tăng version từ 2 lên 3

    // Bảng Disease
    public static final String TABLE_DISEASE = "disease";
    public static final String COLUMN_DISEASE_ID = "id";
    public static final String COLUMN_DISEASE_NAME = "name";
    public static final String COLUMN_SYMPTOMS = "symptoms";
    public static final String COLUMN_CAUSES = "causes";
    public static final String COLUMN_TREATMENT = "treatment";

    // Bảng User
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_PROFILE = "profile";
    public static final String COLUMN_USER_SYMPTOM_HISTORY = "symptom_history";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";

    // Bảng Appointment (UPDATED)
    public static final String TABLE_APPOINTMENT = "appointment";
    public static final String COLUMN_APPOINTMENT_ID = "id";
    public static final String COLUMN_APPOINTMENT_USER_ID = "user_id";
    public static final String COLUMN_APPOINTMENT_CLINIC = "clinic";
    public static final String COLUMN_APPOINTMENT_DOCTOR = "doctor";
    public static final String COLUMN_APPOINTMENT_DATE = "date";
    public static final String COLUMN_APPOINTMENT_TIME = "time";

    // NEW APPOINTMENT COLUMNS
    public static final String COLUMN_PATIENT_NAME = "patient_name";
    public static final String COLUMN_PATIENT_PHONE = "patient_phone";
    public static final String COLUMN_PATIENT_AGE = "patient_age";
    public static final String COLUMN_PATIENT_GENDER = "patient_gender";
    public static final String COLUMN_APPOINTMENT_SYMPTOMS = "symptoms";
    public static final String COLUMN_MEDICAL_HISTORY = "medical_history";
    public static final String COLUMN_APPOINTMENT_TYPE = "appointment_type";
    public static final String COLUMN_APPOINTMENT_FEE = "appointment_fee";
    public static final String COLUMN_APPOINTMENT_NOTES = "notes";
    public static final String COLUMN_APPOINTMENT_STATUS = "status";
    public static final String COLUMN_CANCELLATION_REASON = "cancellation_reason";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_FEEDBACK = "feedback";
    public static final String COLUMN_ORIGINAL_APPOINTMENT_ID = "original_appointment_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    // Bảng Care Recommendation
    public static final String TABLE_CARE_RECOMMENDATION = "care_recommendation";
    public static final String COLUMN_CARE_ID = "id";
    public static final String COLUMN_CARE_DISEASE_ID = "disease_id";
    public static final String COLUMN_CARE_MEDICATION = "medication";
    public static final String COLUMN_CARE_DIET = "diet";
    public static final String COLUMN_CARE_AVOID = "avoid";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Disease
        String createDiseaseTable = "CREATE TABLE " + TABLE_DISEASE + " (" +
                COLUMN_DISEASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DISEASE_NAME + " TEXT, " +
                COLUMN_SYMPTOMS + " TEXT, " +
                COLUMN_CAUSES + " TEXT, " +
                COLUMN_TREATMENT + " TEXT)";
        db.execSQL(createDiseaseTable);

        // Tạo bảng User với cột username và password
        String createUserTable = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_PROFILE + " TEXT, " +
                COLUMN_USER_SYMPTOM_HISTORY + " TEXT, " +
                COLUMN_USER_USERNAME + " TEXT UNIQUE, " +
                COLUMN_USER_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        // Tạo bảng Appointment với tất cả columns mới
        String createAppointmentTable = "CREATE TABLE " + TABLE_APPOINTMENT + " (" +
                COLUMN_APPOINTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_APPOINTMENT_USER_ID + " INTEGER, " +
                COLUMN_APPOINTMENT_CLINIC + " TEXT, " +
                COLUMN_APPOINTMENT_DOCTOR + " TEXT, " +
                COLUMN_APPOINTMENT_DATE + " TEXT, " +
                COLUMN_APPOINTMENT_TIME + " TEXT, " +
                COLUMN_PATIENT_NAME + " TEXT, " +
                COLUMN_PATIENT_PHONE + " TEXT, " +
                COLUMN_PATIENT_AGE + " TEXT, " +
                COLUMN_PATIENT_GENDER + " TEXT, " +
                COLUMN_APPOINTMENT_SYMPTOMS + " TEXT, " +
                COLUMN_MEDICAL_HISTORY + " TEXT, " +
                COLUMN_APPOINTMENT_TYPE + " TEXT DEFAULT 'consultation', " +
                COLUMN_APPOINTMENT_FEE + " REAL DEFAULT 0.0, " +
                COLUMN_APPOINTMENT_NOTES + " TEXT, " +
                COLUMN_APPOINTMENT_STATUS + " TEXT DEFAULT 'pending', " +
                COLUMN_CANCELLATION_REASON + " TEXT, " +
                COLUMN_RATING + " INTEGER DEFAULT 0, " +
                COLUMN_FEEDBACK + " TEXT, " +
                COLUMN_ORIGINAL_APPOINTMENT_ID + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT, " +
                COLUMN_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_APPOINTMENT_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "))";
        db.execSQL(createAppointmentTable);

        // Tạo bảng Care Recommendation
        String createCareRecommendationTable = "CREATE TABLE " + TABLE_CARE_RECOMMENDATION + " (" +
                COLUMN_CARE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CARE_DISEASE_ID + " INTEGER, " +
                COLUMN_CARE_MEDICATION + " TEXT, " +
                COLUMN_CARE_DIET + " TEXT, " +
                COLUMN_CARE_AVOID + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_CARE_DISEASE_ID + ") REFERENCES " + TABLE_DISEASE + "(" + COLUMN_DISEASE_ID + "))";
        db.execSQL(createCareRecommendationTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Upgrade từ version 1 lên 2 (thêm username, password)
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN " + COLUMN_USER_USERNAME + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_USER + " ADD COLUMN " + COLUMN_USER_PASSWORD + " TEXT");
        }

        if (oldVersion < 3) {
            // Upgrade từ version 2 lên 3 (thêm các columns mới cho appointment)
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_PATIENT_NAME + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_PATIENT_PHONE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_PATIENT_AGE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_PATIENT_GENDER + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_APPOINTMENT_SYMPTOMS + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_MEDICAL_HISTORY + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_APPOINTMENT_TYPE + " TEXT DEFAULT 'consultation'");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_APPOINTMENT_FEE + " REAL DEFAULT 0.0");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_APPOINTMENT_NOTES + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_APPOINTMENT_STATUS + " TEXT DEFAULT 'pending'");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_CANCELLATION_REASON + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_RATING + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_FEEDBACK + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_ORIGINAL_APPOINTMENT_ID + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_CREATED_AT + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN " + COLUMN_UPDATED_AT + " TEXT");
        }
    }

    // Hàm mã hóa mật khẩu đơn giản (ví dụ: sử dụng SHA-256)
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return password; // Fallback, không nên dùng trong production
        }
    }
}