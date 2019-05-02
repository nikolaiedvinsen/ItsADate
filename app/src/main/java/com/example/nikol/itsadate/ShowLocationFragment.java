package com.example.nikol.itsadate;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ShowLocationFragment extends Fragment {

    private LocationsViewModel viewModel;
    private ActionBar toolbar;
    private LinearLayout layout;
    private Menu menu;
    private Location location;
    private TextInputEditText showNameView;
    private TextInputEditText showAddressView;
    private TextInputEditText showPhoneNumberView;
    private TextInputEditText showKitchenView;

    private TextInputEditText showOpeningTime;
    private TextInputEditText showClosingTime;
    private TimeSetter openingTime;
    private TimeSetter closingTime;

    private Spinner typeSpinner;
    private boolean editing = false;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_location_fragment_layout, container, false);

        setHasOptionsMenu(true);
        hideItems();
        viewModel = ViewModelProviders.of(getActivity()).get(LocationsViewModel.class);
        layout = view.findViewById(R.id.show_location_layout);

        MainActivity.previousFragment = "ShowLocationFragment";

        //toolbar
        toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Location");

        //Text input
        showNameView = view.findViewById(R.id.location_show_name);
        showAddressView = view.findViewById(R.id.location_show_address);
        showPhoneNumberView = view.findViewById(R.id.location_show_phone_number);
        showKitchenView = view.findViewById(R.id.location_show_kitchen);

        //time picker text input
        showOpeningTime = view.findViewById(R.id.location_opening_time);
        showClosingTime = view.findViewById(R.id.location_closing_time);

        //spinner
        typeSpinner = view.findViewById(R.id.location_show_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.type_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        setText();

        typeSpinner.setEnabled(false);
        typeSpinner.setBackgroundColor(Color.parseColor("#FAFAFA"));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean("editing");
            location = new Location(savedInstanceState.getString("name"),
                    savedInstanceState.getString("address"),
                    savedInstanceState.getString("phone_number"),
                    savedInstanceState.getString("type"),
                    savedInstanceState.getString("kitchen"),
                    savedInstanceState.getString("opening_hours"));
            if (editing) {
                setFocusable(true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("editing", editing);
        savedInstanceState.putString("name", location.getName());
        savedInstanceState.putString("address", location.getAddress());
        savedInstanceState.putString("phone_number", location.getPhoneNumber());
        savedInstanceState.putString("type", location.getType());
        savedInstanceState.putString("kitchen", location.getKitchen());
        savedInstanceState.putString("opening_hours", location.getOpeningHours());



    }

    private void setText() {
        if (location != null) {
            showNameView.setText(location.getName());
            showAddressView.setText(location.getAddress());
            showPhoneNumberView.setText(location.getPhoneNumber());
            showKitchenView.setText(location.getKitchen());
            if (!location.getOpeningHours().equals("-")) {
                String[] openingHours = location.getOpeningHours().split("-");
                showOpeningTime.setText(openingHours[0]);
                showClosingTime.setText(openingHours[1]);
            }
            int spinnerIndex = 0;
            switch (location.getType()) {
                case "Restaurant":
                    spinnerIndex = 0;
                    break;
                case "Café":
                    spinnerIndex = 1;
                    break;
                case "Bar":
                    spinnerIndex = 2;
                    break;
            }
            typeSpinner.setSelection(spinnerIndex);
        }
    }

    private void hideItems() {
        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        RecyclerView recyclerView = getActivity().findViewById(R.id.locations_recycler_view);
        recyclerView.setVisibility(View.GONE);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
    }

    public void setLocation(Location location) {
        this.location = location;
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

        if (location != null) {
            viewModel.delete(location);
            location = new Location(showNameView.getText().toString(),
                    showAddressView.getText().toString(),
                    showPhoneNumberView.getText().toString(),
                    typeSpinner.getSelectedItem().toString(),
                    showKitchenView.getText().toString(),
                    showOpeningTime.getText().toString() + "-" + showClosingTime.getText().toString() );
            viewModel.insert(location);
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
        }
        else {
            getActivity().onBackPressed();
        }
    }

    //Lukker keyboard dersom det er åpent, og aktiverer/deaktiverer focus på TextInputViews
    private void setFocusable(boolean state) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        showNameView.setFocusableInTouchMode(state);
        showNameView.setClickable(state);
        showNameView.setFocusable(state);
        showAddressView.setFocusableInTouchMode(state);
        showAddressView.setClickable(state);
        showAddressView.setFocusable(state);
        showPhoneNumberView.setFocusableInTouchMode(state);
        showPhoneNumberView.setClickable(state);
        showPhoneNumberView.setFocusable(state);
        showKitchenView.setFocusableInTouchMode(state);
        showKitchenView.setClickable(state);
        showKitchenView.setFocusable(state);
        typeSpinner.setEnabled(state);
        if (state) {
            openingTime = new TimeSetter(showOpeningTime);
            closingTime = new TimeSetter(showClosingTime);
            typeSpinner.setBackgroundColor(0);
        }
        else {
            openingTime = null;
            closingTime = null;
            typeSpinner.setBackgroundColor(Color.parseColor("#FAFAFA"));
        }

    }


    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.delete(location);
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
