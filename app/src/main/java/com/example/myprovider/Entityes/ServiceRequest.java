package com.example.myprovider.Entityes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "service_requests")
public class ServiceRequest {

    @PrimaryKey
    @NonNull
    public String id;

    public String service_id;
    public String client_id;
    public String provider_id;
    public String request_date;
    public String status;
    public String notes;
    public double price;
    public String location_address;
    public double latitude;
    public double longitude;

    public ServiceRequest() {}
}
