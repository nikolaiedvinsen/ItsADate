package com.example.nikol.itsadate;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class AppointmentsViewModel extends AndroidViewModel {

    private AppointmentRepository repository;
    private LocationRepository locationRepository;
    private ContactRepository contactRepository;

    private LiveData<List<Appointment>> allEntries;

    public AppointmentsViewModel(Application application) {
        super(application);
        repository = new AppointmentRepository(application);
        locationRepository = new LocationRepository(application);
        contactRepository = new ContactRepository((application));
        allEntries = repository.getAllEntries();
    }

    LiveData<List<Appointment>> getAllEntries() { return allEntries; }

    public void insert(Appointment appointment) { repository.insert(appointment); }
    public void delete(Appointment appointment) { repository.delete(appointment); }
    public void update(Appointment appointment) { repository.update(appointment); }
    public void deleteAll() { repository.deleteAll(); }
    public void deleteWherePK(String pk) { repository.deleteWherePK(pk); }
    public Location getLocationWherePK(String pk) {return locationRepository.getWherePK(pk); }
    public Appointment checkIfDate(String date) {return repository.checkIfDate(date); }
    public List<Location> getAllLocations() {return locationRepository.getAllLocations(); }
    public String[] getAllLocationNames() {return locationRepository.getAllNames(); }
    public String[] getAllContactNames() {return contactRepository.getAllNames(); }

}
