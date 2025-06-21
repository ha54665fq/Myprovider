package com.example.myprovider.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myprovider.Entityes.Company;

import java.util.List;

@Dao
public interface CompanyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Company company);

    @Update
    void update(Company company);

    @Delete
    void delete(Company company);

    @Query("SELECT * FROM companies WHERE id = :id LIMIT 1")
    LiveData<Company> getById(String id);

    @Query("SELECT * FROM companies")
    LiveData<List<Company>> getAll();
}
