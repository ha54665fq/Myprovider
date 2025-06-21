package com.example.myprovider.firestore;


import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.example.myprovider.AppDatabase;
import com.example.myprovider.Entityes.Notification;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NotificationRepository {

    private final AppDatabase db;
    private final FirebaseFirestore firestore;
    private final String COLLECTION = "notifications";

    public NotificationRepository(AppDatabase database) {
        this.db = database;
        this.firestore = FirebaseFirestore.getInstance();
        listenToFirestoreUpdates();
    }

    public void insert(Notification notification) {
        new Thread(() -> {
            db.notificationDao().insert(notification);
            firestore.collection(COLLECTION)
                    .document(notification.id)
                    .set(notification)
                    .addOnSuccessListener(aVoid -> Log.d("NotificationRepo", "Notification synced"))
                    .addOnFailureListener(e -> Log.e("NotificationRepo", "Failed to sync", e));
        }).start();
    }
    public void update(Notification notification) {
        new Thread(() -> {
            db.notificationDao().update(notification);
            firestore.collection(COLLECTION)
                    .document(notification.id)
                    .set(notification)
                    .addOnSuccessListener(aVoid -> Log.d("NotificationRepo", "Updated"))
                    .addOnFailureListener(e -> Log.e("NotificationRepo", "Failed to update", e));
        }).start();
    }

    public void delete(Notification notification) {
        new Thread(() -> {
            db.notificationDao().delete(notification);
            firestore.collection(COLLECTION)
                    .document(notification.id)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("NotificationRepo", "Deleted"))
                    .addOnFailureListener(e -> Log.e("NotificationRepo", "Failed to delete", e));
        }).start();
    }


    public LiveData<Notification> getById(String id) {
        return db.notificationDao().getById(id);
    }

    public LiveData<List<Notification>> getAll() {
        return db.notificationDao().getAll();
    }

    private void listenToFirestoreUpdates() {
        firestore.collection(COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("NotificationRepo", "Listen failed.", e);
                            return;
                        }
                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                Notification notification = dc.getDocument().toObject(Notification.class);
                                new Thread(() -> {
                                    switch (dc.getType()) {
                                        case ADDED:
                                        case MODIFIED:
                                            db.notificationDao().insert(notification);
                                            break;
                                        case REMOVED:
                                            db.notificationDao().delete(notification);
                                            break;
                                    }
                                }).start();
                            }
                        }
                    }
                });
    }
}
