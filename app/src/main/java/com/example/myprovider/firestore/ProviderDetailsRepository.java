package com.example.myprovider.firestore;



import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.example.myprovider.AppDatabase;
import com.example.myprovider.Entityes.ProviderDetails;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ProviderDetailsRepository {

    private final AppDatabase db;
    private final FirebaseFirestore firestore;
    private final String COLLECTION = "provider_details";

    public ProviderDetailsRepository(AppDatabase database) {
        this.db = database;
        this.firestore = FirebaseFirestore.getInstance();
        listenToFirestoreUpdates();
    }

    public void insert(ProviderDetails details) {
        new Thread(() -> {
            db.providerDetailsDao().insert(details);
            firestore.collection(COLLECTION)
                    .document(details.user_id)
                    .set(details)
                    .addOnSuccessListener(aVoid -> Log.d("ProviderDetailsRepo", "Synced"))
                    .addOnFailureListener(e -> Log.e("ProviderDetailsRepo", "Failed to sync", e));
        }).start();
    }
    public void update(ProviderDetails details) {
        new Thread(() -> {
            db.providerDetailsDao().update(details);
            firestore.collection(COLLECTION)
                    .document(details.user_id)
                    .set(details)
                    .addOnSuccessListener(aVoid -> Log.d("ProviderDetailsRepo", "Updated"))
                    .addOnFailureListener(e -> Log.e("ProviderDetailsRepo", "Failed to update", e));
        }).start();
    }

    public void delete(ProviderDetails details) {
        new Thread(() -> {
            db.providerDetailsDao().delete(details);
            firestore.collection(COLLECTION)
                    .document(details.user_id)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("ProviderDetailsRepo", "Deleted"))
                    .addOnFailureListener(e -> Log.e("ProviderDetailsRepo", "Failed to delete", e));
        }).start();
    }

    public LiveData<ProviderDetails> getById(String id) {
        return db.providerDetailsDao().getById(id);
    }

    public LiveData<List<ProviderDetails>> getAll() {
        return db.providerDetailsDao().getAll();
    }

    private void listenToFirestoreUpdates() {
        firestore.collection(COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("ProviderDetailsRepo", "Listen failed.", e);
                            return;
                        }
                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                ProviderDetails details = dc.getDocument().toObject(ProviderDetails.class);
                                new Thread(() -> {
                                    switch (dc.getType()) {
                                        case ADDED:
                                        case MODIFIED:
                                            db.providerDetailsDao().insert(details);
                                            break;
                                        case REMOVED:
                                            db.providerDetailsDao().delete(details);
                                            break;
                                    }
                                }).start();
                            }
                        }
                    }
                });
    }
}
