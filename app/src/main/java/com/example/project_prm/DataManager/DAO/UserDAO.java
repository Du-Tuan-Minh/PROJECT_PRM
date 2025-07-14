package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.User;
import com.example.project_prm.DataManager.Entity.UserProfile;
import com.example.project_prm.utils.HashUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private  final CollectionReference userRef = db.collection("users");

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

    public Task<Boolean> isEmailExist(String email) {
        return db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return !task.getResult().isEmpty(); // ✅ true if email exists
                });
    }

    public Task<Void> changePassword(String userId, String newPassword) {
        String hashedPassword = HashUtil.sha256(newPassword);
        return db.collection("users").document(userId)
                .update("password", hashedPassword);
    }


    public Task<String> login(String email, String password) {
        return db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", HashUtil.sha256(password))
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    QuerySnapshot result = task.getResult();
                    if (result != null && !result.isEmpty()) {
                        DocumentSnapshot document = result.getDocuments().get(0);
                        return document.getId(); // ✅ return document ID
                    } else {
                        throw new Exception("No user found with given email and password");
                    }
                });
    }


    public Task<Void> register(String email, String password, String gender,
                               String dateOfBirth, String name, String role) {
        User user = new User(role, email, HashUtil.sha256(password), "");
        return db.collection("users")
                .add(user.toMap())
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    String userId = task.getResult().getId();
                    UserProfile profile = new UserProfile(0, userId, name, dateOfBirth, gender, "", 0, 0, "", "", "");
                    return db.collection("user_profiles").add(profile.toMap())
                            .continueWith(profileTask -> {
                                if (!profileTask.isSuccessful()) {
                                    throw profileTask.getException();
                                }
                                return null; // success
                            });
                });
    }


    public Task<DocumentSnapshot> getByUserId(String userId) {
        return db.collection("user_profiles")
                .whereEqualTo("user_id", userId)
                .limit(1)
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    QuerySnapshot result = task.getResult();
                    if (result == null || result.isEmpty()) {
                        throw new Exception("Không tìm thấy hồ sơ user_id=" + userId);
                    }
                    return result.getDocuments().get(0);
                });
    }

    // Get userId by email
    public Task<String> getUserIdByEmail(String email) {
        return db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    QuerySnapshot result = task.getResult();
                    if (result != null && !result.isEmpty()) {
                        DocumentSnapshot document = result.getDocuments().get(0);
                        return document.getId(); // ✅ return document
                    } else {
                        throw new Exception("No user found with given email");
                    }
                });
    }

    public Task<Void> update(String docId, UserProfile profile) {
        return db.collection("user_profiles")
                .document(docId)
                .set(profile.toMap());
    }
}
