package com.example.myprovider.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myprovider.Entityes.User;
import com.example.myprovider.firestore.UserRepository;

import java.util.List;

public class UserController extends ViewModel {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    public void insert(User item) { repository.insert(item); }
    public void update(User item) { repository.update(item); }
    public void delete(User item) { repository.delete(item); }

    public LiveData<User> getById(String id) { return repository.getById(id); }
    public LiveData<List<User>> getAll() { return repository.getAll(); }
}
