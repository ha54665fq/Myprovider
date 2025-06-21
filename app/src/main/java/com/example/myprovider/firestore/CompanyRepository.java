package com.example.myprovider.firestore;





import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.example.myprovider.AppDatabase;
import com.example.myprovider.Entityes.Company;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class CompanyRepository {

    private final AppDatabase db;
    private final FirebaseFirestore firestore;
    private final String COLLECTION = "companies";

    public CompanyRepository(AppDatabase database) {
        this.db = database;
        this.firestore = FirebaseFirestore.getInstance();
        listenToFirestoreUpdates();
    }

    public void insert(Company company) {
        new Thread(() -> {
            db.companyDao().insert(company);
            firestore.collection(COLLECTION)
                    .document(String.valueOf(company.id))
                    .set(company)
                    .addOnSuccessListener(aVoid -> Log.d("CompanyRepo", "Company synced successfully"))
                    .addOnFailureListener(e -> Log.e("CompanyRepo", "Failed to sync company", e));
        }).start();
    }
    public void update(Company company) {
        new Thread(() -> {
            db.companyDao().update(company);
            firestore.collection(COLLECTION)
                    .document(String.valueOf(company.id))
                    .set(company)
                    .addOnSuccessListener(aVoid -> Log.d("CompanyRepo", "Updated"))
                    .addOnFailureListener(e -> Log.e("CompanyRepo", "Failed to update", e));
        }).start();
    }

    public void delete(Company company) {
        new Thread(() -> {
            db.companyDao().delete(company);
            firestore.collection(COLLECTION)
                    .document(String.valueOf(company.id))
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("CompanyRepo", "Deleted"))
                    .addOnFailureListener(e -> Log.e("CompanyRepo", "Failed to delete", e));
        }).start();
    }

    public LiveData<Company> getById(String id) {
        return db.companyDao().getById(id);
    }

    public LiveData<List<Company>> getAll() {
        return db.companyDao().getAll();
    }

    private void listenToFirestoreUpdates() {
        firestore.collection(COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("CompanyRepo", "Listen failed.", e);
                            return;
                        }
                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                Company company = dc.getDocument().toObject(Company.class);
                                new Thread(() -> {
                                    switch (dc.getType()) {
                                        case ADDED:
                                        case MODIFIED:
                                            db.companyDao().insert(company);
                                            break;
                                        case REMOVED:
                                            db.companyDao().delete(company);
                                            break;
                                    }
                                }).start();
                            }
                        }
                    }
                });
    }
}
