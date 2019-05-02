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
public interface AppointmentDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Appointment appointment);

    @Delete
    void delete(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Query("DELETE FROM appointment_table")
    void deleteAll();

    @Query("SELECT * from appointment_table ORDER BY name ASC")
    LiveData<List<Appointment>> getAllEntries();

    @Query("DELETE from appointment_table WHERE name = :pk")
    void deleteWherePK(String pk);

    @Query("SELECT * from appointment_table WHERE date = :date")
    Appointment checkIfDate(String date);
}
