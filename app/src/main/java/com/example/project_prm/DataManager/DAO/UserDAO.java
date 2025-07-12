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

    public long registerUser(int role, String name, String dateOfBirth,
                             int gender, String email, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_ROLE, role);
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_GENDER, gender);
        values.put(DatabaseHelper.COLUMN_USER_DATE_OF_BIRTH, dateOfBirth);
        values.put(DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY, new JSONArray().toString());
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, DatabaseHelper.hashPassword(password));
        return db.insert(DatabaseHelper.TABLE_USER, null, values);
    }

    public User loginUser(String email, String password) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, new String[]{
                        DatabaseHelper.COLUMN_USER_ID},
                DatabaseHelper.COLUMN_USER_EMAIL+ "=? AND " + DatabaseHelper.COLUMN_USER_PASSWORD + "=?",
                new String[]{email, DatabaseHelper.hashPassword(password)}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public boolean IsExistEmail(String email) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, new String[]{
                        DatabaseHelper.COLUMN_USER_ID},
                DatabaseHelper.COLUMN_USER_EMAIL+ "=?",
                new String[]{email}, null, null, null);
        boolean isExist = cursor.moveToFirst();
        cursor.close();
        return isExist;
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        if (user == null) return false;
        if (!user.getPassword().equals(DatabaseHelper.hashPassword(oldPassword))) return false;
        user.setPassword(newPassword);
        return updateUser(user) > 0;
    }

//    public List<User> getAllUsers() {
//        List<User> users = new ArrayList<>();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                User user = new User();
//                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
//                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
//                user.setProfile(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PROFILE)));
//                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_USERNAME)));
//                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PASSWORD)));
//                String symptomHistoryJson = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY));
//                try {
//                    user.setSymptomHistory(new JSONArray(symptomHistoryJson));
//                } catch (JSONException e) {
//                    user.setSymptomHistory(new JSONArray());
//                }
//                users.add(user);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return users;
//    }

    public User getUserById(int id) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, new String[]{
                        DatabaseHelper.COLUMN_USER_ID, DatabaseHelper.COLUMN_USER_NAME,
                        DatabaseHelper.COLUMN_USER_GENDER, DatabaseHelper.COLUMN_USER_DATE_OF_BIRTH,
                        DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY,
                        DatabaseHelper.COLUMN_USER_ROLE,
                        DatabaseHelper.COLUMN_USER_PASSWORD,
                        DatabaseHelper.COLUMN_USER_EMAIL},
                DatabaseHelper.COLUMN_USER_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
            user.setGender(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_GENDER)));
            user.setDateOfBirth(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_DATE_OF_BIRTH)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL)));
            user.setRole(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ROLE)));
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
        cursor.close();
        return null;
    }

    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, user.getName());
        values.put(DatabaseHelper.COLUMN_USER_GENDER, user.getGender());
        values.put(DatabaseHelper.COLUMN_USER_DATE_OF_BIRTH, user.getDateOfBirth());
        values.put(DatabaseHelper.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_USER_SYMPTOM_HISTORY, user.getSymptomHistory().toString());
        // Continue update other fields
        return db.update(DatabaseHelper.TABLE_USER, values,
                DatabaseHelper.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(user.getId())});
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
            user.setSymptomHistory(symptomHistory);

            return updateUser(user) > 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}