package com.example.nikol.itsadate;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class LocationsViewModel extends AndroidViewModel {

    private LocationRepository repository;

    private LiveData<List<Location>> allEntries;

    public LocationsViewModel(Application application) {
        super(application);
        repository = new LocationRepository(application);
        allEntries = repository.getAllEntries();
    }

    LiveData<List<Location>> getAllEntries() { return allEntries; }

    public void insert(Location location) { repository.insert(location); }
    public void delete(Location location) { repository.delete(location); }
    public void update(Location location) { repository.update(location); }
    public void deleteAll() { repository.deleteAll(); }
    public void deleteWherePK(String pk) { repository.deleteWherePK(pk); }
    public Location getWherePK(String pk) { return repository.getWherePK(pk); }

}
