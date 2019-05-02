package com.example.nikol.itsadate;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "location_table")
public class Location {

    @NonNull
    @PrimaryKey
    private String name;
    private String address;
    private String phoneNumber;
    private String type;
    private String kitchen;
    private String openingHours;

    public Location(String name, String address, String phoneNumber, String type, String kitchen, String openingHours) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.kitchen = kitchen;
        this.openingHours = openingHours;
    }

    public String getName() { return name; }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getType() {
        return type;
    }

    public String getKitchen() {
        return kitchen;
    }

    public String getOpeningHours() {
        return openingHours;
    }

}
