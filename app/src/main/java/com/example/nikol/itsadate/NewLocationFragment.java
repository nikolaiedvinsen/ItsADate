package com.example.nikol.itsadate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NewLocationFragment extends Fragment {

    private TextInputEditText addNameView;
    private TextInputEditText addAddressView;
    private TextInputEditText addPhoneNumberView;
    private TextInputEditText addKitchenView;

    private TextInputEditText addOpeningTime;
    private TextInputEditText addClosingTime;

    private Spinner typeSpinner;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_location_fragment_layout, container, false);

        setHasOptionsMenu(true);
        hideItems();
        MainActivity.previousFragment = "NewLocationFragment";

        ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeAsUpIndicator(R.drawable.ic_close);
        toolbar.setTitle("New location");

        addNameView = view.findViewById(R.id.location_add_name);
        addAddressView = view.findViewById(R.id.location_add_address);
        addPhoneNumberView = view.findViewById(R.id.location_add_phone_number);
        addKitchenView = view.findViewById(R.id.location_add_kitchen);

        addOpeningTime = view.findViewById(R.id.location_opening_time);
        new TimeSetter(addOpeningTime);
        addClosingTime = view.findViewById(R.id.location_closing_time);
        new TimeSetter(addClosingTime);

        typeSpinner = view.findViewById(R.id.location_add_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.type_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        return view;
    }

    private void hideItems() {
        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        RecyclerView recyclerView = getActivity().findViewById(R.id.locations_recycler_view);
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
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        new Intent().
                                putExtra("name", addNameView.getText().toString()).
                                putExtra("address", addAddressView.getText().toString()).
                                putExtra("phone_number", addPhoneNumberView.getText().toString()).
                                putExtra("type", typeSpinner.getSelectedItem().toString()).
                                putExtra("kitchen", addKitchenView.getText().toString()).
                                putExtra("opening_time", addOpeningTime.getText().toString()).
                                putExtra("closing_time", addClosingTime.getText().toString())
                );
                break;
        }

        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
