package com.example.project_prm.DataManager.DAO;

import com.example.project_prm.DataManager.Entity.UserBookmark;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class UserBookmarkDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addWithBatch(WriteBatch batch, UserBookmark bookmark) {
        batch.set(db.collection("user_bookmarks").document(), bookmark.toMap());
    }

    public Task<Void> update(String id, UserBookmark bookmark) {
        return db.collection("user_bookmarks").document(id).set(bookmark.toMap(), SetOptions.merge());
    }

    public Task<Void> delete(String id) {
        return db.collection("user_bookmarks").document(id).delete();
    }

    public Task<DocumentSnapshot> getById(String id) {
        return db.collection("user_bookmarks").document(id).get();
    }
}
