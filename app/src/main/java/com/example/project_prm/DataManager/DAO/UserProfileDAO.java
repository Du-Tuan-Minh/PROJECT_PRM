package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.UserProfile;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserProfileDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, UserProfile userProfile) {
        batch.set(db.collection("user_profiles").document(), userProfile.toMap());
    }

    public Task<Void> add(String profileId, UserProfile profile) {
        return db.collection("user_profiles").document(profileId).set(profile.toMap());
    }

    public Task<Void> update(String profileId, UserProfile profile) {
        return db.collection("user_profiles").document(profileId).update(profile.toMap());
    }

    public Task<Void> delete(String profileId) {
        return db.collection("user_profiles").document(profileId).delete();
    }

    public Task<DocumentSnapshot> getById(String profileId) {
        return db.collection("user_profiles").document(profileId).get();
    }

    public Task<DocumentSnapshot> getByUserId(String userId) {
        return db.collection("user_profiles").whereEqualTo("user_id", userId).get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            return document.getReference().get();
                        }
                    }
                    return null;
                });
    }
}
