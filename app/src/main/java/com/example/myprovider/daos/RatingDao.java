package com.example.myprovider.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myprovider.Entityes.Rating;

import java.util.List;

@Dao
public interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Rating rating);

    @Update
    void update(Rating rating);

    @Delete
    void delete(Rating rating);

    @Query("SELECT * FROM ratings WHERE id = :id LIMIT 1")
    LiveData<Rating> getById(String id);

    @Query("SELECT * FROM ratings")
    LiveData<List<Rating>> getAll();
}
