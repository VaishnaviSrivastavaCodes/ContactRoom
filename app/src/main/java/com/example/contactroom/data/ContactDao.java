package com.example.contactroom.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactroom.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    //Takes care of CRUD Operations
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);

    @Query("DELETE FROM table_name")
    void deleteAll();

    @Query("SELECT * FROM table_name ORDER BY name ASC")
    LiveData<List<Contact>>getAllContacts();

    @Query("SELECT * FROM table_name WHERE table_name.id==:id")
    LiveData<Contact> get(int id);

    @Delete
    void delete(Contact contact);

    @Update
    void update(Contact contact);

//    @Query("SELECT COUNT(*) FROM table_name")
//    int getCount();


}
