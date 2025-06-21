package com.example.myprovider;



import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myprovider.Entityes.Category;
import com.example.myprovider.Entityes.Company;
import com.example.myprovider.Entityes.Notification;
import com.example.myprovider.Entityes.ProviderDetails;
import com.example.myprovider.Entityes.Rating;
import com.example.myprovider.Entityes.Service;
import com.example.myprovider.Entityes.ServiceRequest;
import com.example.myprovider.Entityes.User;
import com.example.myprovider.daos.CategoryDao;
import com.example.myprovider.daos.CompanyDao;
import com.example.myprovider.daos.NotificationDao;
import com.example.myprovider.daos.ProviderDetailsDao;
import com.example.myprovider.daos.RatingDao;
import com.example.myprovider.daos.ServiceDao;
import com.example.myprovider.daos.ServiceRequestDao;
import com.example.myprovider.daos.UserDao;

@Database(
        entities = {
                Category.class,
                Company.class,
                Notification.class,
                ProviderDetails.class,
                Rating.class,
                Service.class,
                ServiceRequest.class,
                User.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract CompanyDao companyDao();
    public abstract NotificationDao notificationDao();
    public abstract ProviderDetailsDao providerDetailsDao();
    public abstract RatingDao ratingDao();
    public abstract ServiceDao serviceDao();
    public abstract ServiceRequestDao serviceRequestDao();
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "my_provider_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
