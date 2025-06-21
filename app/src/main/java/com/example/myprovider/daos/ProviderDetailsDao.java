package com.example.myprovider.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myprovider.Entityes.ProviderDetails;

import java.util.List;

@Dao
public interface ProviderDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProviderDetails providerDetails);

    @Update
    void update(ProviderDetails providerDetails);

    @Delete
    void delete(ProviderDetails providerDetails);

    @Query("SELECT * FROM provider_details WHERE user_id = :id LIMIT 1")
    LiveData<ProviderDetails> getById(String id);

    @Query("SELECT * FROM provider_details")
    LiveData<List<ProviderDetails>> getAll();
}
