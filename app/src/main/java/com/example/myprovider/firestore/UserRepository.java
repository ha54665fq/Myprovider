package com.example.myprovider.firestore;



import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.example.myprovider.AppDatabase;
import com.example.myprovider.Entityes.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserRepository {

    private final AppDatabase db;
    private final FirebaseFirestore firestore;
    private final String COLLECTION = "users";

    public UserRepository(AppDatabase database) {
        this.db = database;
        this.firestore = FirebaseFirestore.getInstance();
        listenToFirestoreUpdates();
    }

    public void insert(User user) {
        new Thread(() -> {
            db.userDao().insert(user);
            firestore.collection(COLLECTION)
                    .document(user.id)
                    .set(user)
                    .addOnSuccessListener(aVoid -> Log.d("UserRepo", "Synced"))
                    .addOnFailureListener(e -> Log.e("UserRepo", "Failed to sync", e));
        }).start();
    }
    public void update(User user) {
        new Thread(() -> {
            db.userDao().update(user);
            firestore.collection(COLLECTION)
                    .document(user.id)
                    .set(user)
                    .addOnSuccessListener(aVoid -> Log.d("UserRepo", "Updated"))
                    .addOnFailureListener(e -> Log.e("UserRepo", "Failed to update", e));
        }).start();
    }

    public void delete(User user) {
        new Thread(() -> {
            db.userDao().delete(user);
            firestore.collection(COLLECTION)
                    .document(user.id)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("UserRepo", "Deleted"))
                    .addOnFailureListener(e -> Log.e("UserRepo", "Failed to delete", e));
        }).start();
    }

    public LiveData<User> getById(String id) {
        return db.userDao().getById(id);
    }

    public LiveData<List<User>> getAll() {
        return db.userDao().getAll();
    }

    private void listenToFirestoreUpdates() {
        firestore.collection(COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("UserRepo", "Listen failed.", e);
                            return;
                        }
                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                User user = dc.getDocument().toObject(User.class);
                                new Thread(() -> {
                                    switch (dc.getType()) {
                                        case ADDED:
                                        case MODIFIED:
                                            db.userDao().insert(user);
                                            break;
                                        case REMOVED:
                                            db.userDao().delete(user);
                                            break;
                                    }
                                }).start();
                            }
                        }
                    }
                });
    }
}
