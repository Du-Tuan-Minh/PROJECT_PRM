package com.example.project_prm.DataManager.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm.DataManager.Entity.CareRecommendation;
import com.example.project_prm.DataManager.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CareRecommendationDAO {
    private final SQLiteDatabase db;

    public CareRecommendationDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long addCareRecommendation(int diseaseId, String medication, String diet, String avoid) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CARE_DISEASE_ID, diseaseId);
        values.put(DatabaseHelper.COLUMN_CARE_MEDICATION, medication);
        values.put(DatabaseHelper.COLUMN_CARE_DIET, diet);
        values.put(DatabaseHelper.COLUMN_CARE_AVOID, avoid);
        return db.insert(DatabaseHelper.TABLE_CARE_RECOMMENDATION, null, values);
    }

    public List<CareRecommendation> getCareRecommendationsByDiseaseId(int diseaseId) {
        List<CareRecommendation> recommendations = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CARE_RECOMMENDATION, new String[]{
                        DatabaseHelper.COLUMN_CARE_ID, DatabaseHelper.COLUMN_CARE_DISEASE_ID,
                        DatabaseHelper.COLUMN_CARE_MEDICATION, DatabaseHelper.COLUMN_CARE_DIET, DatabaseHelper.COLUMN_CARE_AVOID},
                DatabaseHelper.COLUMN_CARE_DISEASE_ID + "=?", new String[]{String.valueOf(diseaseId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CareRecommendation recommendation = new CareRecommendation();
                recommendation.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CARE_ID)));
                recommendation.setDiseaseId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CARE_DISEASE_ID)));
                recommendation.setMedication(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CARE_MEDICATION)));
                recommendation.setDiet(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CARE_DIET)));
                recommendation.setAvoid(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CARE_AVOID)));
                recommendations.add(recommendation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recommendations;
    }
}