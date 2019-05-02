package com.example.nikol.itsadate;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private ContactRepository repository;

    private LiveData<List<Contact>> allEntries;

    public ContactsViewModel(Application application) {
        super(application);
        repository = new ContactRepository(application);
        allEntries = repository.getAllEntries();
    }

    LiveData<List<Contact>> getAllEntries() { return allEntries; }

    public void insert(Contact contact) { repository.insert(contact); }
    public void delete(Contact contact) { repository.delete(contact); }
    public void update(Contact contact) { repository.update(contact); }
    public void deleteAll() { repository.deleteAll(); }

}
