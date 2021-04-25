package com.nickmonks.contactroom.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.nickmonks.contactroom.data.ContactDAO;
import com.nickmonks.contactroom.model.Contact;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// With this annotation, we define the Entity class
@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {

    // method to retrive DAO object
   public abstract ContactDAO contactDao();

   public static final int NUMBER_OF_THREADS = 4;

   //volatile means that any object created with volatile would disappear from the compiler
   private static volatile ContactRoomDatabase INSTANCE;

    // as explained in the room model, data must not be retrieved/write from the main thread,
    // so we should manage threads to do this using a executor
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);


   // Singleton pattern - thread safe
   public static ContactRoomDatabase getDatabase(final Context context) {
       if (INSTANCE == null){
           synchronized (ContactRoomDatabase.class){
               INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                                ContactRoomDatabase.class,
                                                "contact_database")
                                                .addCallback(sRoomDatabaseCallack)
                                                .build();
           }
       }

       return INSTANCE;
   }

   // This method will be invoked in when we build our database in the getDatabase method
    private static final RoomDatabase.Callback sRoomDatabaseCallack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // populate the database on the background
            databaseWriteExecutor.execute(() -> {
                // we delete to have a fresh start
                ContactDAO contactDAO = INSTANCE.contactDao();
                contactDAO.deleteALl();

                // Insert contacts
                Contact contact = new Contact("Nick", "Engineer");
                contactDAO.insert(contact);

                contact = new Contact("Bond", "Spy");
                contactDAO.insert(contact);

            });
        }
    };

}
