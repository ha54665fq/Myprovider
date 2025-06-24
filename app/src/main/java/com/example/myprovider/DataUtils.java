package com.example.myprovider;

import android.content.Context;
import android.util.Log;


import com.example.myprovider.Entityes.Category;
import com.example.myprovider.Entityes.Company;
import com.example.myprovider.Entityes.Notification;
import com.example.myprovider.Entityes.ProviderDetails;
import com.example.myprovider.Entityes.Rating;
import com.example.myprovider.Entityes.Service;
import com.example.myprovider.Entityes.ServiceRequest;
import com.example.myprovider.Entityes.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class DataUtils {

    public static void insertSampleData(Context context, AppDatabase db) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Users
        User user1 = new User();
        user1.id = "user_1";
        user1.name = "Ahmed";
        user1.phone = "0591234567";
        user1.image = "";

        db.userDao().insert(user1);
        firestore.collection("users").document(user1.id).set(user1);
        Log.d("ROOM_TEST", "Inserted user: " + db.userDao().getById("user_1"));

        // Categories
        Category cat1 = new Category();
        cat1.name = "Electrician";
        cat1.description = "Electrical maintenance";

        db.categoryDao().insert(cat1);
        firestore.collection("categories").add(cat1);

        // Company
        Company company = new Company();
        company.owner_id = "user_1";
        company.company_name = "Tech Solutions";
        company.is_verified = true;
        company.description = "Tech company";
        company.address = "Gaza";
        company.latitude = 31.5;
        company.longitude = 34.47;

        db.companyDao().insert(company);
        firestore.collection("companies").add(company);

        // Notification
        Notification noti = new Notification();
        noti.id = UUID.randomUUID().toString();
        noti.user_id = "user_1";
        noti.type = "info";
        noti.message = "Welcome!";
        noti.is_read = false;
        noti.created_at = "2025-06-21";
        noti.target_type = "service";
        noti.target_id = "service_1";

        db.notificationDao().insert(noti);
        firestore.collection("notifications").document(noti.id).set(noti);

        // ProviderDetails
        ProviderDetails pd = new ProviderDetails();
        pd.user_id = "user_1";
        pd.national_id = "123456789";
        pd.id_card_image = "";
        pd.address = "Gaza";
        pd.latitude = 31.5;
        pd.longitude = 34.47;

        db.providerDetailsDao().insert(pd);
        firestore.collection("providerdetails").document(pd.user_id).set(pd);

        // Rating
        Rating rating = new Rating();
        rating.id = UUID.randomUUID().toString();
        rating.service_id = "service_1";
        rating.provider_id = "user_1";
        rating.client_id = "user_1";
        rating.rating = 5;
        rating.comment = "Excellent!";
        rating.created_at = "2025-06-21";

        db.ratingDao().insert(rating);
        firestore.collection("ratings").document(rating.id).set(rating);

        // Service
        Service service = new Service();
        service.id = "service_1";
        service.provider_id = "user_1";
        service.name = "Wiring Service";
        service.description = "House wiring";
        service.price = 100;
        service.image = "";
        service.category_id = cat1.name;
        service.is_active = true;
        service.views_count = 0;

        db.serviceDao().insert(service);
        firestore.collection("services").document(service.id).set(service);

        // Service Request
        ServiceRequest request = new ServiceRequest();
        request.id = UUID.randomUUID().toString();
        request.service_id = "service_1";
        request.client_id = "user_1";
        request.provider_id = "user_1";
        request.request_date = "2025-06-21";
        request.status = "pending";
        request.notes = "Please do it quickly";
        request.price = 100;
        request.location_address = "Gaza";
        request.latitude = 31.5;
        request.longitude = 34.47;

        db.serviceRequestDao().insert(request);
        firestore.collection("servicerequests").document(request.id).set(request);
    }
}
