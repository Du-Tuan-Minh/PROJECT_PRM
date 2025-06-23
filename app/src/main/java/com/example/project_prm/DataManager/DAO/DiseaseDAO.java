package com.example.project_prm.DataManager.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.Entity.Disease;

import java.util.ArrayList;
import java.util.List;

public class DiseaseDAO {
    private final SQLiteDatabase db;

    public DiseaseDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long addDisease(String name, String symptoms, String causes, String treatment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DISEASE_NAME, name);
        values.put(DatabaseHelper.COLUMN_SYMPTOMS, symptoms);
        values.put(DatabaseHelper.COLUMN_CAUSES, causes);
        values.put(DatabaseHelper.COLUMN_TREATMENT, treatment);
        return db.insert(DatabaseHelper.TABLE_DISEASE, null, values);
    }

    public List<Disease> getAllDiseases() {
        List<Disease> diseases = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_DISEASE, null);

        if (cursor.moveToFirst()) {
            do {
                Disease disease = new Disease();
                disease.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISEASE_ID)));
                disease.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISEASE_NAME)));
                disease.setSymptoms(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SYMPTOMS)));
                disease.setCauses(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CAUSES)));
                disease.setTreatment(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TREATMENT)));
                diseases.add(disease);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return diseases;
    }

    public Disease getDiseaseById(int id) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_DISEASE, new String[]{
                        DatabaseHelper.COLUMN_DISEASE_ID, DatabaseHelper.COLUMN_DISEASE_NAME,
                        DatabaseHelper.COLUMN_SYMPTOMS, DatabaseHelper.COLUMN_CAUSES, DatabaseHelper.COLUMN_TREATMENT},
                DatabaseHelper.COLUMN_DISEASE_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Disease disease = new Disease();
            disease.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISEASE_ID)));
            disease.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISEASE_NAME)));
            disease.setSymptoms(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SYMPTOMS)));
            disease.setCauses(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CAUSES)));
            disease.setTreatment(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TREATMENT)));
            cursor.close();
            return disease;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public List<Disease> findDiseasesBySymptoms(String symptom) {
        List<Disease> diseases = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DISEASE, new String[]{
                        DatabaseHelper.COLUMN_DISEASE_ID, DatabaseHelper.COLUMN_DISEASE_NAME,
                        DatabaseHelper.COLUMN_SYMPTOMS, DatabaseHelper.COLUMN_CAUSES, DatabaseHelper.COLUMN_TREATMENT},
                DatabaseHelper.COLUMN_SYMPTOMS + " LIKE ?", new String[]{"%" + symptom + "%"},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Disease disease = new Disease();
                disease.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISEASE_ID)));
                disease.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISEASE_NAME)));
                disease.setSymptoms(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SYMPTOMS)));
                disease.setCauses(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CAUSES)));
                disease.setTreatment(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TREATMENT)));
                diseases.add(disease);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return diseases;
    }

    public int updateDisease(int id, String name, String symptoms, String causes, String treatment) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DISEASE_NAME, name);
        values.put(DatabaseHelper.COLUMN_SYMPTOMS, symptoms);
        values.put(DatabaseHelper.COLUMN_CAUSES, causes);
        values.put(DatabaseHelper.COLUMN_TREATMENT, treatment);
        return db.update(DatabaseHelper.TABLE_DISEASE, values, DatabaseHelper.COLUMN_DISEASE_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteDisease(int id) {
        db.delete(DatabaseHelper.TABLE_DISEASE, DatabaseHelper.COLUMN_DISEASE_ID + "=?", new String[]{String.valueOf(id)});
    }
}
