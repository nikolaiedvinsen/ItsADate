package com.example.nikol.itsadate;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ContactRepository {
    private ContactDAO contactDAO;
    private LiveData<List<Contact>> allEntries;

    ContactRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        contactDAO = db.contactDAO();
        allEntries = contactDAO.getAllEntries();
    }

    LiveData<List<Contact>> getAllEntries() {
        return allEntries;
    }

    public void insert (Contact contact) {
        new insertAsyncTask(contactDAO).execute(contact);
    }
    public void delete (Contact contact) {
        new deleteAsyncTask(contactDAO).execute(contact);
    }
    public void update (Contact contact) {
        new updateAsyncTask(contactDAO).execute(contact);
    }
    public void deleteAll () {
        new deleteAllAsyncTask(contactDAO).execute();
    }
    public String[] getAllNames () {
        return contactDAO.getAllNames();
    }

    private static class insertAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDAO asyncTaskDao;

        insertAsyncTask(ContactDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDAO asyncTaskDao;

        deleteAsyncTask(ContactDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDAO asyncTaskDao;

        updateAsyncTask(ContactDAO dao) { asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Contact... params) {
            asyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private ContactDAO asyncTaskDao;

        deleteAllAsyncTask(ContactDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }
}
