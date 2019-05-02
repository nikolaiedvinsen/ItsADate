package com.example.nikol.itsadate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    public ViewPager viewPager;
    Toolbar toolbar;
    public static String previousFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        final String[] tabsTitles = {"Locations", "Appointments", "Contacts"};
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                //here in array pass position as an index value for get title and set that
                toolbar.setTitle(tabsTitles[position]);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        toolbar.setTitle("Appointments");
        viewPager.setCurrentItem(1);
        setAlarm();
    }

    private void showItems() {
        if (previousFragment != null) {
            if (previousFragment.equals("NewLocationFragment") || previousFragment.equals("ShowLocationFragment")) {
                RecyclerView recyclerView = findViewById(R.id.locations_recycler_view);
                recyclerView.setVisibility(View.VISIBLE);
                toolbar.setTitle("Locations");
            }

            else if (previousFragment.equals("NewContactFragment") || previousFragment.equals("ShowContactFragment")) {
                RecyclerView recyclerView = findViewById(R.id.contacts_recycler_view);
                recyclerView.setVisibility(View.VISIBLE);
                toolbar.setTitle("Contacts");
            }

            else if (previousFragment.equals("NewAppointmentFragment") || previousFragment.equals("ShowAppointmentFragment")) {
                RecyclerView recyclerView = findViewById(R.id.appointments_recycler_view);
                recyclerView.setVisibility(View.VISIBLE);
                toolbar.setTitle("Appointments");
            }

            TabLayout tabLayout = findViewById(R.id.tabs);
            tabLayout.setVisibility(View.VISIBLE);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.show();
        }
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        showItems();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            toolbar.setTitle("Settings");
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_main, container, false);
            return view;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new LocationsFragment();
                    break;
                case 1:
                    fragment = new AppointmentsFragment();
                    break;
                case 2:
                    fragment = new ContactsFragment();
                    break;
                default:
                    fragment = new AppointmentsFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public void setAlarm() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("checkbox_notifications", true))  {
            AppointmentsViewModel viewModel = ViewModelProviders.of(this).get(AppointmentsViewModel.class);

            if (viewModel.checkIfDate(prefs.getString("notification_time", "01.01.18")) != null) {
                if (!prefs.getBoolean("firstTime", false)) {
                    Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    String time = prefs.getString("notification_time", "00:00");
                    char c = '0';
                    int hour;
                    int minute;
                    if (time.charAt(0) == c) {
                        hour = time.charAt(1);
                    }
                    else {
                        String s = "time.charAt(0)" + "time.charAt(1)";
                        hour = Integer.parseInt(s);
                    }

                    if (time.charAt(3) == c) {
                        minute = time.charAt(4);
                    }
                    else {
                        StringBuilder b = new StringBuilder();
                        b.append(time.charAt(3));
                        b.append(time.charAt(4));
                        String s = b.toString();
                        minute = Integer.parseInt(s);
                    }
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);

                    manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstTime", true);
                    editor.apply();
                }
            }

        }

    }
}
