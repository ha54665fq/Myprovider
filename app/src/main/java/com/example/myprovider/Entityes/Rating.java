package com.example.myprovider.Entityes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ratings")
public class Rating {

    @PrimaryKey
    @NonNull
    public String id;

    public String service_id;
    public String provider_id;
    public String client_id;
    public int rating;
    public String comment;
    public String created_at;

    public Rating() {}
}
