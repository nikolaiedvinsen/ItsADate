package com.example.nikol.itsadate;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocationRepository {
    private LocationDAO locationDAO;
    private LiveData<List<Location>> allEntries;


    LocationRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        locationDAO = db.locationDAO();
        allEntries = locationDAO.getAllEntries();
    }

    LiveData<List<Location>> getAllEntries() {
        return allEntries;
    }

    public void insert (Location location) {
        new insertAsyncTask(locationDAO).execute(location);
    }
    public void delete (Location location) {
        new deleteAsyncTask(locationDAO).execute(location);
    }
    public void update (Location location) {
        new updateAsyncTask(locationDAO).execute(location);
    }
    public void deleteAll () {
        new deleteAllAsyncTask(locationDAO).execute();
    }
    public void deleteWherePK (String pk) {
        new deleteWherePKAsyncTask(locationDAO).execute();
    }
    public List<Location> getAllLocations () {
        return locationDAO.getAllLocations();
    }
    public String[] getAllNames () {
        return locationDAO.getAllNames();
    }
    public Location getWherePK (String pk) {
        return locationDAO.getWherePK(pk);
    }

    private static class insertAsyncTask extends AsyncTask<Location, Void, Void> {

        private LocationDAO asyncTaskDao;

        insertAsyncTask(LocationDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Location... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends android.os.AsyncTask<Location, Void, Void> {

        private LocationDAO asyncTaskDao;

        deleteAsyncTask(LocationDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Location... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Location, Void, Void> {

        private LocationDAO asyncTaskDao;

        updateAsyncTask(LocationDAO dao) { asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Location... params) {
            asyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private LocationDAO asyncTaskDao;

        deleteAllAsyncTask(LocationDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteWherePKAsyncTask extends AsyncTask<String, Void, Void> {

        private LocationDAO asyncTaskDao;

        deleteWherePKAsyncTask(LocationDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            asyncTaskDao.deleteWherePK(params[0]);
            return null;
        }
    }

    private static class getAllAsyncTask extends android.os.AsyncTask<Void, Void, List<Location>> {

        private LocationDAO mAsyncTaskDao;
        List<Location> list;

        getAllAsyncTask(LocationDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Location> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAllLocations();
        }
    }

    public static class getWherePKAsyncTask extends AsyncTask<String, Void, Location> {

        private LocationDAO asyncTaskDao;
        Location location;

        getWherePKAsyncTask(LocationDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Location doInBackground(String... params) {
            return asyncTaskDao.getWherePK(params[0]);
        }
    }
}
