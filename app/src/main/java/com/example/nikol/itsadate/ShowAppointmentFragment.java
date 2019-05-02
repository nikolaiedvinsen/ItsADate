package com.example.nikol.itsadate;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowAppointmentFragment extends Fragment {

    private TextInputEditText showNameView;
    private TextInputEditText showLocationView;
    private TextInputEditText showAttendeesView;
    private TextInputEditText showDateView;
    private TextInputEditText showTimeView;
    private TimeSetter timeSetter;
    private DateSetter dateSetter;

    private Appointment appointment;
    private AppointmentsViewModel viewModel;
    private Menu menu;
    private ActionBar toolbar;
    private LinearLayout layout;

    private String selected = null;
    private List<String> selectedContacts;
    private String[] names;
    private int selectedIndex = -1;
    private ArrayList<Integer> selectedItemsIndex;
    private boolean editing = false;
    private Boolean firstTime;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_appointment_fragment_layout, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(AppointmentsViewModel.class);
        layout = view.findViewById(R.id.show_appointment_layout);

        setHasOptionsMenu(true);
        hideItems();
        MainActivity.previousFragment = "ShowAppointmentFragment";

        toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Appointment");

        //Text input
        showNameView = view.findViewById(R.id.appointment_show_name);
        showLocationView = view.findViewById(R.id.appointment_show_location);
        showAttendeesView = view.findViewById(R.id.appointment_show_attendees);
        showDateView = view.findViewById(R.id.appointment_show_date);
        showTimeView = view.findViewById(R.id.appointment_show_time);

        //Selectors
        selectedItemsIndex = new ArrayList<>();
        selectedContacts = new ArrayList<>();
        names = viewModel.getAllContactNames();

        showLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    createSelectLocationDialog();
                }
            }
        });

        showAttendeesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editing) {
                    createSelectContactsDialog();
                }
            }
        });

        setText();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean("editing");
            appointment = new Appointment(savedInstanceState.getString("name"),
                    savedInstanceState.getString("location_name"),
                    savedInstanceState.getString("attendees"),
                    savedInstanceState.getString("date"),
                    savedInstanceState.getString("time"));
            if (editing) {
                setFocusable(true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("editing", editing);
        savedInstanceState.putString("name", appointment.getName());
        savedInstanceState.putString("location_name", appointment.getLocationName());
        savedInstanceState.putString("attendees", appointment.getAttendees());
        savedInstanceState.putString("date", appointment.getDate());
        savedInstanceState.putString("time", appointment.getTime());
    }

    private void setText() {
        if (appointment != null) {
            showNameView.setText(appointment.getName());
            showLocationView.setText(appointment.getLocationName());
            showDateView.setText(appointment.getDate());
            showTimeView.setText(appointment.getTime());
            showAttendeesView.setText(appointment.getAttendees());

        }
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
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
        this.menu = menu;
        inflater.inflate(R.menu.menu_show_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        if (editing) {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_confirm).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_delete:
                confirmDialog();
                break;

            case R.id.action_edit:
                onActionEdit();
                break;

            case R.id.action_confirm:
                onActionConfirm();
                break;
            case android.R.id.home:
                onActionHome();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onActionEdit() {
        editing = true;
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_confirm).setVisible(true);
        menu.findItem(R.id.action_edit).setVisible(false);
        toolbar.setHomeAsUpIndicator(R.drawable.ic_close);
        setFocusable(true);
    }

    public void onActionConfirm() {
        editing = false;
        toolbar.setHomeAsUpIndicator(R.drawable.ic_back);
        menu.findItem(R.id.action_delete).setVisible(true);
        menu.findItem(R.id.action_confirm).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(true);

        if (appointment != null) {
            viewModel.delete(appointment);
            appointment = new Appointment(showNameView.getText().toString(),
                    showLocationView.getText().toString(),
                    showAttendeesView.getText().toString(),
                    showDateView.getText().toString(),
                    showTimeView.getText().toString()
            );
            viewModel.insert(appointment);
            setFocusable(false);
        }
    }

    public void onActionHome() {
        if (editing) {
            editing = false;
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_confirm).setVisible(false);
            menu.findItem(R.id.action_edit).setVisible(true);
            toolbar.setHomeAsUpIndicator(R.drawable.ic_back);
            setFocusable(false);
            setText();
        } else {
            getActivity().onBackPressed();
        }
    }

        public void createSelectLocationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final String[] locations = viewModel.getAllLocationNames();
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
                            showAttendeesView.setText(selected);


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

        public void createSelectContactsDialog () {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final String[] names = viewModel.getAllContactNames();
            boolean[] isSelectedArray = new boolean[names.length];
            if (firstTime == null) {
                String[] already_selected = appointment.getAttendees().replace(" ", "").split(",");
                for (int i = 0; i < names.length; i++) {
                    if (Arrays.asList(already_selected).contains(names[i])) {
                        selectedItemsIndex.add(i);
                    }
                }
                firstTime = false;
            }

            for (int i = 0; i < names.length; i++) {
                if (selectedItemsIndex.contains(i)) {
                    isSelectedArray[i] = true;
                } else {
                    isSelectedArray[i] = false;
                }
            }

            if (isSelectedArray.length == 0) {
                for (int i = 0; i < names.length; i++) {
                    boolean temp = false;
                    isSelectedArray[i] = temp;
                }
            }
            builder.setTitle("Select contacts")
                    .setMultiChoiceItems(names, isSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                            if (isChecked) {
                                selectedItemsIndex.add(i);
                            } else if (selectedItemsIndex.contains(i)) {
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
                                    if (c != selectedItemsIndex.size() - 1) {
                                        stringBuilder.append(", ");
                                    }
                                }
                                showAttendeesView.setText(stringBuilder.toString());
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

    private void setFocusable(boolean state) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        showNameView.setFocusableInTouchMode(state);
        showNameView.setClickable(state);
        showNameView.setFocusable(state);
        showLocationView.setFocusableInTouchMode(state);
        showLocationView.setClickable(state);
        showLocationView.setFocusable(state);

        if (state) {
            dateSetter = new DateSetter(showDateView);
            timeSetter = new TimeSetter(showTimeView);
        }
        else {
            dateSetter = null;
            timeSetter = null;
        }

    }


    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.delete(appointment);
                        getActivity().onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

}
