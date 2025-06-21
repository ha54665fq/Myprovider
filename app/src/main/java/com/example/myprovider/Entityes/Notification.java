package com.example.myprovider.Entityes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notification {

    @PrimaryKey
    @NonNull
    public String id;

    public String user_id;
    public String type;
    public String message;
    public boolean is_read;
    public String created_at;
    public String target_type;
    public String target_id;

    public Notification() {}
}
