package com.example.myprovider.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myprovider.Entityes.Notification;
import com.example.myprovider.firestore.NotificationRepository;

import java.util.List;

public class NotificationController extends ViewModel {

    private final NotificationRepository repository;

    public NotificationController(NotificationRepository repository) {
        this.repository = repository;
    }

    public void insert(Notification item) { repository.insert(item); }
    public void update(Notification item) { repository.update(item); }
    public void delete(Notification item) { repository.delete(item); }

    public LiveData<Notification> getById(String id) { return repository.getById(id); }
    public LiveData<List<Notification>> getAll() { return repository.getAll(); }
}
