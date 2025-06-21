package com.example.myprovider.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myprovider.Entityes.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notification notification);

    @Update
    void update(Notification notification);

    @Delete
    void delete(Notification notification);

    @Query("SELECT * FROM notifications WHERE id = :id LIMIT 1")
    LiveData<Notification> getById(String id);

    @Query("SELECT * FROM notifications")
    LiveData<List<Notification>> getAll();
}
