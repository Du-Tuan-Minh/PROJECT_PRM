package com.example.project_prm.DataManager.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.DataManager.Entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final SQLiteDatabase db;

    public UserDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long registerUser(String name, String profile, String username, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_PROFILE, profile);
        values.put(DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY, new JSONArray().toString());
        values.put(DatabaseHelper.COLUMN_USER_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, DatabaseHelper.hashPassword(password));
        return db.insert(DatabaseHelper.TABLE_USER, null, values);
    }

    public User loginUser(String username, String password) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, new String[]{
                        DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_USER_NAME,
                        DatabaseHelper.COLUMN_USER_PROFILE, DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY,
                        DatabaseHelper.COLUMN_USER_USERNAME, DatabaseHelper.COLUMN_USER_PASSWORD},
                DatabaseHelper.COLUMN_USER_USERNAME + "=? AND " + DatabaseHelper.COLUMN_USER_PASSWORD + "=?",
                new String[]{username, DatabaseHelper.hashPassword(password)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
            user.setProfile(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PROFILE)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)));
            String symptomHistoryJson = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY));
            try {
                user.setSymptomHistory(new JSONArray(symptomHistoryJson));
            } catch (JSONException e) {
                user.setSymptomHistory(new JSONArray());
            }
            cursor.close();
            return user;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
                user.setProfile(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PROFILE)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)));
                String symptomHistoryJson = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY));
                try {
                    user.setSymptomHistory(new JSONArray(symptomHistoryJson));
                } catch (JSONException e) {
                    user.setSymptomHistory(new JSONArray());
                }
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public User getUserById(int id) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, new String[]{
                        DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_USER_NAME,
                        DatabaseHelper.COLUMN_USER_PROFILE, DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY,
                        DatabaseHelper.COLUMN_USER_USERNAME, DatabaseHelper.COLUMN_USER_PASSWORD},
                DatabaseHelper.COLUMN_USER_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
            user.setProfile(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PROFILE)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)));
            String symptomHistoryJson = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY));
            try {
                user.setSymptomHistory(new JSONArray(symptomHistoryJson));
            } catch (JSONException e) {
                user.setSymptomHistory(new JSONArray());
            }
            cursor.close();
            return user;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public int updateUser(int id, String name, String profile, String username, String password, JSONArray symptomHistory) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_PROFILE, profile);
        values.put(DatabaseHelper.COLUMN_USER_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, DatabaseHelper.hashPassword(password));
        values.put(DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY, symptomHistory.toString());
        return db.update(DatabaseHelper.TABLE_USER, values, DatabaseHelper.COLUMN_USER_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteUser(int id) {
        db.delete(DatabaseHelper.TABLE_USER, DatabaseHelper.COLUMN_USER_ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean addSymptomToUser(int userId, String symptom, String date, String severity) {
        User user = getUserById(userId);
        if (user == null) return false;

        try {
            JSONArray symptomHistory = user.getSymptomHistory();
            JSONObject newSymptom = new JSONObject();
            newSymptom.put("symptom", symptom);
            newSymptom.put("date", date);
            newSymptom.put("severity", severity);
            symptomHistory.put(newSymptom);

            return updateUser(userId, user.getName(), user.getProfile(), user.getUsername(), user.getPassword(), symptomHistory) > 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}