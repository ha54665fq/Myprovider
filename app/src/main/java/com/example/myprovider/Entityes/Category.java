package com.example.myprovider.Entityes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String name;
    public String description;

    public Category() {}
}