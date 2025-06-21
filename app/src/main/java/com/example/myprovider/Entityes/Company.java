package com.example.myprovider.Entityes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "companies")
public class Company {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String owner_id;
    public String company_name;
    public boolean is_verified;
    public String description;
    public String address;
    public double latitude;
    public double longitude;

    public Company() {}
}