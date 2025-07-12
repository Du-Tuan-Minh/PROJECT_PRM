package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, String userId, User user) {
        batch.set(db.collection("users").document(userId), user.toMap());
    }

    public Task<Void> add(String userId, User user) {
        return db.collection("users").document(userId).set(user.toMap());
    }

    public Task<Void> update(String userId, User user) {
        return db.collection("users").document(userId).update(user.toMap());
    }

    public Task<Void> delete(String userId) {
        return db.collection("users").document(userId).delete();
    }

    public Task<DocumentSnapshot> getById(String userId) {
        return db.collection("users").document(userId).get();
    }

}
