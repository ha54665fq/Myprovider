package com.example.myprovider.firestore;



import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.example.myprovider.AppDatabase;
import com.example.myprovider.Entityes.ServiceRequest;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ServiceRequestRepository {

    private final AppDatabase db;
    private final FirebaseFirestore firestore;
    private final String COLLECTION = "service_requests";

    public ServiceRequestRepository(AppDatabase database) {
        this.db = database;
        this.firestore = FirebaseFirestore.getInstance();
        listenToFirestoreUpdates();
    }

    public void insert(ServiceRequest request) {
        new Thread(() -> {
            db.serviceRequestDao().insert(request);
            firestore.collection(COLLECTION)
                    .document(request.id)
                    .set(request)
                    .addOnSuccessListener(aVoid -> Log.d("ServiceRequestRepo", "Synced"))
                    .addOnFailureListener(e -> Log.e("ServiceRequestRepo", "Failed to sync", e));
        }).start();
    }
    public void update(ServiceRequest request) {
        new Thread(() -> {
            db.serviceRequestDao().update(request);
            firestore.collection(COLLECTION)
                    .document(request.id)
                    .set(request)
                    .addOnSuccessListener(aVoid -> Log.d("ServiceRequestRepo", "Updated"))
                    .addOnFailureListener(e -> Log.e("ServiceRequestRepo", "Failed to update", e));
        }).start();
    }

    public void delete(ServiceRequest request) {
        new Thread(() -> {
            db.serviceRequestDao().delete(request);
            firestore.collection(COLLECTION)
                    .document(request.id)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("ServiceRequestRepo", "Deleted"))
                    .addOnFailureListener(e -> Log.e("ServiceRequestRepo", "Failed to delete", e));
        }).start();
    }

    public LiveData<ServiceRequest> getById(String id) {
        return db.serviceRequestDao().getById(id);
    }

    public LiveData<List<ServiceRequest>> getAll() {
        return db.serviceRequestDao().getAll();
    }


    private void listenToFirestoreUpdates() {
        firestore.collection(COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("ServiceRequestRepo", "Listen failed.", e);
                            return;
                        }
                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                ServiceRequest request = dc.getDocument().toObject(ServiceRequest.class);
                                new Thread(() -> {
                                    switch (dc.getType()) {
                                        case ADDED:
                                        case MODIFIED:
                                            db.serviceRequestDao().insert(request);
                                            break;
                                        case REMOVED:
                                            db.serviceRequestDao().delete(request);
                                            break;
                                    }
                                }).start();
                            }
                        }
                    }
                });
    }
}
