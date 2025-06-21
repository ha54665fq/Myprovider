package com.example.myprovider.firestore;



import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.example.myprovider.AppDatabase;
import com.example.myprovider.Entityes.Rating;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RatingRepository {

    private final AppDatabase db;
    private final FirebaseFirestore firestore;
    private final String COLLECTION = "ratings";

    public RatingRepository(AppDatabase database) {
        this.db = database;
        this.firestore = FirebaseFirestore.getInstance();
        listenToFirestoreUpdates();
    }

    public void insert(Rating rating) {
        new Thread(() -> {
            db.ratingDao().insert(rating);
            firestore.collection(COLLECTION)
                    .document(rating.id)
                    .set(rating)
                    .addOnSuccessListener(aVoid -> Log.d("RatingRepo", "Synced"))
                    .addOnFailureListener(e -> Log.e("RatingRepo", "Failed to sync", e));
        }).start();
    }
    public void update(Rating rating) {
        new Thread(() -> {
            db.ratingDao().update(rating);
            firestore.collection(COLLECTION)
                    .document(rating.id)
                    .set(rating)
                    .addOnSuccessListener(aVoid -> Log.d("RatingRepo", "Updated"))
                    .addOnFailureListener(e -> Log.e("RatingRepo", "Failed to update", e));
        }).start();
    }

    public void delete(Rating rating) {
        new Thread(() -> {
            db.ratingDao().delete(rating);
            firestore.collection(COLLECTION)
                    .document(rating.id)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("RatingRepo", "Deleted"))
                    .addOnFailureListener(e -> Log.e("RatingRepo", "Failed to delete", e));
        }).start();
    }

    public LiveData<Rating> getById(String id) {
        return db.ratingDao().getById(id);
    }

    public LiveData<List<Rating>> getAll() {
        return db.ratingDao().getAll();
    }

    private void listenToFirestoreUpdates() {
        firestore.collection(COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("RatingRepo", "Listen failed.", e);
                            return;
                        }
                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                Rating rating = dc.getDocument().toObject(Rating.class);
                                new Thread(() -> {
                                    switch (dc.getType()) {
                                        case ADDED:
                                        case MODIFIED:
                                            db.ratingDao().insert(rating);
                                            break;
                                        case REMOVED:
                                            db.ratingDao().delete(rating);
                                            break;
                                    }
                                }).start();
                            }
                        }
                    }
                });
    }
}

