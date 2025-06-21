package com.example.myprovider.firestore;



import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.example.myprovider.AppDatabase;
import com.example.myprovider.Entityes.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CategoryRepository {

    private final AppDatabase db;
    private final FirebaseFirestore firestore;
    private final String COLLECTION = "categories";

    public CategoryRepository(AppDatabase database) {
        this.db = database;
        this.firestore = FirebaseFirestore.getInstance();
        listenToFirestoreUpdates();
    }

    // Insert or update locally and remotely
    public void insert(Category category) {
        // Insert locally
        new Thread(() -> {
            db.categoryDao().insert(category);
            // Upload to Firestore
            firestore.collection(COLLECTION)
                    .document(String.valueOf(category.id))
                    .set(category)
                    .addOnSuccessListener(aVoid -> Log.d("CategoryRepo", "Category synced successfully"))
                    .addOnFailureListener(e -> Log.e("CategoryRepo", "Failed to sync category", e));
        }).start();
    }
    public void update(Category category) {
        new Thread(() -> {
            db.categoryDao().update(category);
            firestore.collection(COLLECTION)
                    .document(String.valueOf(category.id))
                    .set(category)
                    .addOnSuccessListener(aVoid -> Log.d("CategoryRepo", "Updated"))
                    .addOnFailureListener(e -> Log.e("CategoryRepo", "Failed to update", e));
        }).start();
    }

    public void delete(Category category) {
        new Thread(() -> {
            db.categoryDao().delete(category);
            firestore.collection(COLLECTION)
                    .document(String.valueOf(category.id))
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("CategoryRepo", "Deleted"))
                    .addOnFailureListener(e -> Log.e("CategoryRepo", "Failed to delete", e));
        }).start();
    }

    public LiveData<Category> getById(String id) {
        return db.categoryDao().getById(id);
    }

    public LiveData<List<Category>> getAll() {
        return db.categoryDao().getAll();
    }

    // Listen to remote Firestore updates and update local Room DB accordingly
    private void listenToFirestoreUpdates() {
        firestore.collection(COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("CategoryRepo", "Listen failed.", e);
                            return;
                        }

                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                Category category = dc.getDocument().toObject(Category.class);

                                new Thread(() -> {
                                    switch (dc.getType()) {
                                        case ADDED:
                                        case MODIFIED:
                                            db.categoryDao().insert(category);
                                            break;
                                        case REMOVED:
                                            db.categoryDao().delete(category);
                                            break;
                                    }
                                }).start();
                            }
                        }
                    }
                });
    }
}
