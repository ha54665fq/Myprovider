package com.example.myprovider.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myprovider.Entityes.ProviderDetails;
import com.example.myprovider.firestore.ProviderDetailsRepository;

import java.util.List;

public class ProviderDetailsController extends ViewModel {

    private final ProviderDetailsRepository repository;

    public ProviderDetailsController(ProviderDetailsRepository repository) {
        this.repository = repository;
    }

    public void insert(ProviderDetails item) { repository.insert(item); }
    public void update(ProviderDetails item) { repository.update(item); }
    public void delete(ProviderDetails item) { repository.delete(item); }

    public LiveData<ProviderDetails> getById(String id) { return repository.getById(id); }
    public LiveData<List<ProviderDetails>> getAll() { return repository.getAll(); }
}
