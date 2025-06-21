package com.example.myprovider.Entityes;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FirestoreSeeder {

    public static void seed() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("id", "user_001");
        user.put("name", "أحمد");
        user.put("phone", "+970599000000");
        user.put("image", "https://example.com/image.jpg");
        db.collection("users").document("user_001").set(user);


    }
}