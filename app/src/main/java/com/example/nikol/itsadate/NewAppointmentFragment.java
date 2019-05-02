package com.example.nikol.itsadate;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewAppointmentFragment extends Fragment {

    private TextInputEditText addNameView;
    private TextInputEditText addLocationView;
    private TextInputEditText addAttendeesView;
    private TextInputEditText addLocationDate;
    private TextInputEditText addLocationTime;

    private AppointmentsViewModel appointmentsViewModel;
    private String selected = null;
    private List<String> selectedContacts;
    String[] names;
    private int selectedIndex = -1;
    private ArrayList<Integer> selectedItemsIndex;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_appointment_fragment_layout, container, false);
        appointmentsViewModel = ViewModelProviders.of(getActivity()).get(AppointmentsViewModel.class);
        setHasOptionsMenu(true);
        hideItems();
        MainActivity.previousFragment = "NewAppointmentFragment";

        ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeAsUpIndicator(R.drawable.ic_close);
        toolbar.setTitle("New appointment");

        addNameView = view.findViewById(R.id.appointment_add_name);
        addLocationView = view.findViewById(R.id.appointment_add_location);
        addAttendeesView = view.findViewById(R.id.appointment_add_attendees);
        addLocationDate = view.findViewById(R.id.appointment_add_date);
        addLocationTime = view.findViewById(R.id.appointment_add_time);
        new DateSetter(addLocationDate);
        new TimeSetter(addLocationTime);

        selectedItemsIndex = new ArrayList<>();
        selectedContacts = new ArrayList<>();
        names = appointmentsViewModel.getAllContactNames();

        addLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSelectLocationDialog();
            }
        });

        addAttendeesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSelectContactsDialog();
            }
        });

        return view;
    }

    private void hideItems() {
        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        RecyclerView recyclerView = getActivity().findViewById(R.id.appointments_recycler_view);
        recyclerView.setVisibility(View.GONE);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_close_or_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsIcon = menu.findItem(R.id.action_settings);
        settingsIcon.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_confirm:
                getTargetFragment().onActivityResult(
                        1,
                        Activity.RESULT_OK,
                        new Intent().
                                putExtra("name", addNameView.getText().toString()).
                                putExtra("location", addLocationView.getText().toString()).
                                putExtra("attendees", addAttendeesView.getText().toString()).
                                putExtra("date", addLocationDate.getText().toString()).
                                putExtra("time", addLocationTime.getText().toString())
                        );
                break;
        }

        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void createSelectLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] locations = appointmentsViewModel.getAllLocationNames();
        if (selectedIndex == -1) {
            selectedIndex = 0;
            selected = locations[selectedIndex];
        }
        builder.setTitle("Select location")
                .setSingleChoiceItems(locations, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        selected = locations[i];
                        selectedIndex = i;
                    }
                })

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addLocationView.setText(selected);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
        }



    public void createSelectContactsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] names = appointmentsViewModel.getAllContactNames();
        boolean[] isSelectedArray = new boolean[names.length];

        for (int i = 0; i < names.length; i++) {
            if (selectedItemsIndex.contains(i)) {
                isSelectedArray[i] = true;
            } else {
                isSelectedArray[i] = false;
            }
        }

        if (isSelectedArray.length == 0) {
            for (int x = 0; x < names.length; x++) {
                boolean temp = false;
                isSelectedArray[x] = temp;
            }
        }
        builder.setTitle("Select contacts")
                .setMultiChoiceItems(names, isSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                        if (isChecked) {
                            selectedItemsIndex.add(i);
                        }
                        else if (selectedItemsIndex.contains(i)) {
                                selectedItemsIndex.remove(Integer.valueOf(i));
                        }
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        StringBuilder stringBuilder = new StringBuilder();
                        if (selectedItemsIndex.size() != 0) {
                            for (int c = 0; c < selectedItemsIndex.size(); c++) {
                                selectedContacts.add(names[selectedItemsIndex.get(c)]);
                                stringBuilder.append(names[selectedItemsIndex.get(c)]);
                                if (c != selectedItemsIndex.size()-1) {
                                    stringBuilder.append(", ");
                                }
                            }
                        addAttendeesView.setText(stringBuilder.toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

}
