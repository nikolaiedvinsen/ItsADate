package com.example.nikol.itsadate;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "appointment_table")
public class Appointment {

    @NonNull
    @PrimaryKey
    private String name;
    private String locationName;
    private String attendees;
    private String date;
    private String time;


    public Appointment(String name, String locationName, String attendees, String date, String time) {
        this.name = name;
        this.locationName = locationName;
        this.attendees = attendees;
        this.date = date;
        this.time = time;
    }
    public String getName() {
        return name;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getAttendees() {
        return attendees;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
