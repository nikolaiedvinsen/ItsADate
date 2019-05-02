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
public interface LocationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Location location);

    @Delete
    void delete(Location location);

    @Update
    void update(Location location);

    @Query("DELETE FROM location_table")
    void deleteAll();

    @Query("SELECT * from location_table ORDER BY name ASC")
    LiveData<List<Location>> getAllEntries();

    @Query("DELETE from location_table WHERE name = :pk")
    void deleteWherePK(String pk);

    @Query("SELECT * from location_table WHERE name = :pk")
    Location getWherePK(String pk);

    @Query("SELECT * from location_table ORDER BY name ASC")
    List<Location> getAllLocations();

    @Query("SELECT name from location_table ORDER BY name ASC")
    String[] getAllNames();

}
