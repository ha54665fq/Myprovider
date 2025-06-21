package com.example.myprovider.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.myprovider.Entityes.Category;
import com.example.myprovider.firestore.CategoryRepository;

import java.util.List;

public class CategoryController extends ViewModel {

    private final CategoryRepository repository;

    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    public void insert(Category item) { repository.insert(item); }
    public void update(Category item) { repository.update(item); }
    public void delete(Category item) { repository.delete(item); }

    public LiveData<Category> getById(String id) { return repository.getById(id); }
    public LiveData<List<Category>> getAll() { return repository.getAll(); }
}
