package com.example.nikol.itsadate;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;

@Database(entities = {Location.class, Contact.class, Appointment.class}, version = 1, exportSchema = false)
public abstract class RoomDatabase extends android.arch.persistence.room.RoomDatabase {

    public abstract LocationDAO locationDAO();
    public abstract ContactDAO contactDAO();
    public abstract AppointmentDAO appointmentDAO();

    private static volatile RoomDatabase INSTANCE;

    static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, "database").
                            allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
