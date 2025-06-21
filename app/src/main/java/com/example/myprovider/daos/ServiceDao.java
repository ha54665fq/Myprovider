package com.example.myprovider.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myprovider.Entityes.Service;

import java.util.List;

@Dao
public interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Service service);

    @Update
    void update(Service service);

    @Delete
    void delete(Service service);

    @Query("SELECT * FROM services WHERE id = :id LIMIT 1")
    LiveData<Service> getById(String id);

    @Query("SELECT * FROM services")
    LiveData<List<Service>> getAll();
}