package com.example.myprovider.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myprovider.Entityes.ServiceRequest;
import com.example.myprovider.firestore.ServiceRequestRepository;

import java.util.List;

public class ServiceRequestController extends ViewModel {

    private final ServiceRequestRepository repository;

    public ServiceRequestController(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    public void insert(ServiceRequest item) { repository.insert(item); }
    public void update(ServiceRequest item) { repository.update(item); }
    public void delete(ServiceRequest item) { repository.delete(item); }

    public LiveData<ServiceRequest> getById(String id) { return repository.getById(id); }
    public LiveData<List<ServiceRequest>> getAll() { return repository.getAll(); }
}
