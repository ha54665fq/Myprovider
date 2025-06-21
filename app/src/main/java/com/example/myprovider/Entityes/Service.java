package com.example.myprovider.Entityes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "services")
public class Service {

    @PrimaryKey
    @NonNull
    public String id;

    public String provider_id;
    public String name;
    public String description;
    public double price;
    public String image;
    public String category_id;
    public boolean is_active;
    public int views_count;

    public Service() {}
}
