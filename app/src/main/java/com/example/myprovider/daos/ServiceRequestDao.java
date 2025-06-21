package com.example.myprovider.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myprovider.Entityes.ServiceRequest;

import java.util.List;

@Dao
public interface ServiceRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ServiceRequest serviceRequest);

    @Update
    void update(ServiceRequest serviceRequest);

    @Delete
    void delete(ServiceRequest serviceRequest);

    @Query("SELECT * FROM service_requests WHERE id = :id LIMIT 1")
    LiveData<ServiceRequest> getById(String id);

    @Query("SELECT * FROM service_requests")
    LiveData<List<ServiceRequest>> getAll();
}
