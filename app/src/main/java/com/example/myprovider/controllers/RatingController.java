package com.example.myprovider.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myprovider.Entityes.Rating;
import com.example.myprovider.firestore.RatingRepository;

import java.util.List;

public class RatingController extends ViewModel {

    private final RatingRepository repository;

    public RatingController(RatingRepository repository) {
        this.repository = repository;
    }

    public void insert(Rating item) { repository.insert(item); }
    public void update(Rating item) { repository.update(item); }
    public void delete(Rating item) { repository.delete(item); }

    public LiveData<Rating> getById(String id) { return repository.getById(id); }
    public LiveData<List<Rating>> getAll() { return repository.getAll(); }
}
