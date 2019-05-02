package com.example.nikol.itsadate;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact contact);

    @Delete
    void delete(Contact contact);

    @Update
    void update(Contact contact);

    @Query("DELETE FROM contact_table")
    void deleteAll();

    @Query("SELECT * from contact_table ORDER BY name ASC")
    LiveData<List<Contact>> getAllEntries();

    @Query("SELECT * from contact_table WHERE name = :pk")
    Contact getWherePK(String pk);

    @Query("SELECT name from contact_table ORDER BY name ASC")
    String[] getAllNames();
}
