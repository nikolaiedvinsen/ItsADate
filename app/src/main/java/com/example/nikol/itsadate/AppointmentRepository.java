package com.example.nikol.itsadate;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class AppointmentRepository {
    private AppointmentDAO appointmentDAO;
    private LocationDAO locationDAO;
    private LiveData<List<Appointment>> allEntries;

    AppointmentRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        appointmentDAO = db.appointmentDAO();
        locationDAO = db.locationDAO();
        allEntries = appointmentDAO.getAllEntries();
    }

    LiveData<List<Appointment>> getAllEntries() {
        return allEntries;
    }

    public void insert (Appointment appointment) {
        new insertAsyncTask(appointmentDAO).execute(appointment);
    }
    public void delete (Appointment appointment) {
        new deleteAsyncTask(appointmentDAO).execute(appointment);
    }

    public void update (Appointment appointment) {
        new updateAsyncTask(appointmentDAO).execute(appointment);
    }

    public Appointment checkIfDate (String date) {
        return appointmentDAO.checkIfDate(date);
    }

    public void deleteAll () {
        new deleteAllAsyncTask(appointmentDAO).execute();
    }
    public void deleteWherePK (String pk) {
        new deleteWherePKAsyncTask(appointmentDAO).execute();
    }

    private static class insertAsyncTask extends AsyncTask<Appointment, Void, Void> {

        private AppointmentDAO asyncTaskDao;

        insertAsyncTask(AppointmentDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Appointment... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Appointment, Void, Void> {

        private AppointmentDAO asyncTaskDao;

        deleteAsyncTask(AppointmentDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Appointment... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Appointment, Void, Void> {

        private AppointmentDAO asyncTaskDao;

        updateAsyncTask(AppointmentDAO dao) { asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(final Appointment... params) {
            asyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private AppointmentDAO asyncTaskDao;

        deleteAllAsyncTask(AppointmentDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteWherePKAsyncTask extends AsyncTask<String, Void, Void> {

        private AppointmentDAO asyncTaskDao;

        deleteWherePKAsyncTask(AppointmentDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            asyncTaskDao.deleteWherePK(params[0]);
            return null;
        }
    }
}
