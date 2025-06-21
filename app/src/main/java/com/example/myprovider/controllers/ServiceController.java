package com.example.myprovider.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myprovider.Entityes.Service;
import com.example.myprovider.firestore.ServiceRepository;

import java.util.List;

public class ServiceController extends ViewModel {

    private final ServiceRepository repository;

    public ServiceController(ServiceRepository repository) {
        this.repository = repository;
    }

    public void insert(Service item) { repository.insert(item); }
    public void update(Service item) { repository.update(item); }
    public void delete(Service item) { repository.delete(item); }

    public LiveData<Service> getById(String id) { return repository.getById(id); }
    public LiveData<List<Service>> getAll() { return repository.getAll(); }
}
