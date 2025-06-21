package com.example.myprovider.firestore;



import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.example.myprovider.AppDatabase;
import com.example.myprovider.Entityes.Service;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ServiceRepository {

    private final AppDatabase db;
    private final FirebaseFirestore firestore;
    private final String COLLECTION = "services";

    public ServiceRepository(AppDatabase database) {
        this.db = database;
        this.firestore = FirebaseFirestore.getInstance();
        listenToFirestoreUpdates();
    }

    public void insert(Service service) {
        new Thread(() -> {
            db.serviceDao().insert(service);
            firestore.collection(COLLECTION)
                    .document(service.id)
                    .set(service)
                    .addOnSuccessListener(aVoid -> Log.d("ServiceRepo", "Synced"))
                    .addOnFailureListener(e -> Log.e("ServiceRepo", "Failed to sync", e));
        }).start();
    }
    public void update(Service service) {
        new Thread(() -> {
            db.serviceDao().update(service);
            firestore.collection(COLLECTION)
                    .document(service.id)
                    .set(service)
                    .addOnSuccessListener(aVoid -> Log.d("ServiceRepo", "Updated"))
                    .addOnFailureListener(e -> Log.e("ServiceRepo", "Failed to update", e));
        }).start();
    }

    public void delete(Service service) {
        new Thread(() -> {
            db.serviceDao().delete(service);
            firestore.collection(COLLECTION)
                    .document(service.id)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("ServiceRepo", "Deleted"))
                    .addOnFailureListener(e -> Log.e("ServiceRepo", "Failed to delete", e));
        }).start();
    }

    public LiveData<Service> getById(String id) {
        return db.serviceDao().getById(id);
    }

    public LiveData<List<Service>> getAll() {
        return db.serviceDao().getAll();
    }

    private void listenToFirestoreUpdates() {
        firestore.collection(COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("ServiceRepo", "Listen failed.", e);
                            return;
                        }
                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                Service service = dc.getDocument().toObject(Service.class);
                                new Thread(() -> {
                                    switch (dc.getType()) {
                                        case ADDED:
                                        case MODIFIED:
                                            db.serviceDao().insert(service);
                                            break;
                                        case REMOVED:
                                            db.serviceDao().delete(service);
                                            break;
                                    }
                                }).start();
                            }
                        }
                    }
                });
    }
}
