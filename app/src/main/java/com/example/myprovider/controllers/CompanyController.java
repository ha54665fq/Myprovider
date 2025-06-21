package com.example.myprovider.controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.myprovider.Entityes.Company;
import com.example.myprovider.firestore.CompanyRepository;

import java.util.List;

public class CompanyController extends ViewModel {

    private final CompanyRepository repository;

    public CompanyController(CompanyRepository repository) {
        this.repository = repository;
    }

    public void insert(Company item) { repository.insert(item); }
    public void update(Company item) { repository.update(item); }
    public void delete(Company item) { repository.delete(item); }

    public LiveData<Company> getById(String id) { return repository.getById(id); }
    public LiveData<List<Company>> getAll() { return repository.getAll(); }
}
