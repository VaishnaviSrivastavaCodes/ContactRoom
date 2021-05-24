package com.example.contactroom.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.contactroom.data.ContactDao;
import com.example.contactroom.model.Contact;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class},version = 1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {


    public abstract ContactDao contactDao();

    //volatile means it can get rid of itself if need be
    private static volatile ContactRoomDatabase INSTANCE;

    public static final int NUMBER_OF_THREADS = 4;
    //Executor Service will make it run in the back thread
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContactRoomDatabase getDatabase(final Context context) {
        if(INSTANCE ==null){
            synchronized (ContactRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContactRoomDatabase.class,"contact_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExecutor.execute(()->{
                    ContactDao contactDao = INSTANCE.contactDao();
                    contactDao.deleteAll();

                    Contact contact = new Contact("Shipra","Teacher");
                    contactDao.insert(contact);

                    contact = new Contact("Ayesha","Carpenter");
                    contactDao.insert(contact);
                    });

                }
            };
}
