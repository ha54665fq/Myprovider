package com.example.myprovider.Entityes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "provider_details")
public class ProviderDetails {

    @PrimaryKey
    @NonNull
    public String user_id;

    public String national_id;
    public String id_card_image;
    public String address;
    public double latitude;
    public double longitude;

    public ProviderDetails() {}
}
