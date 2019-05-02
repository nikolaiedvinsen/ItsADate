package com.example.nikol.itsadate;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class LocationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LocationsRVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public LocationsViewModel viewModel;
    public static final int NEW_LOCATION_ACTIVITY_REQUEST_CODE = 1;
    public static final int SHOW_LOCATION_ACTIVITY_REQUEST_CODE = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.locations_fragment_layout, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.locations_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<Location> locations = new ArrayList<>();

        adapter = new LocationsRVAdapter(locations);
        adapter.setFragment(this);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(LocationsViewModel.class);
        viewModel.getAllEntries().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(@Nullable final List<Location> locations) {
                // Update the cached copy of the locations in the adapter.
                adapter.setDataSet(locations);
            }
        });

        //eksempler
        viewModel.insert(new Location("Papa Frank's", "Main Street 2", "12345678", "Restaurant", "Indisk", "10:00-18:00"));
        viewModel.insert(new Location("Uncle Jims's", "Side Street 5", "87654321", "Café", "Internettkafé", "10:00-14:00"));
        viewModel.insert(new Location("Grandma Sally's", "Street 10", "11223344", "Bar", "Brun", "10:00-18:00"));

        return view;
    }
    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNewLocationFragment();
            }
        });
    }

    public void launchShowLocationFragment(Location location) {
        ShowLocationFragment showLocationFragment = new ShowLocationFragment();
        showLocationFragment.setLocation(location);
        showLocationFragment.setTargetFragment(LocationsFragment.this, SHOW_LOCATION_ACTIVITY_REQUEST_CODE);
        getFragmentManager().beginTransaction().replace(R.id.location_fragment_container, showLocationFragment).addToBackStack(null).commit();


    }

    public void launchNewLocationFragment() {
        NewLocationFragment newLocationFragment = new NewLocationFragment();
        newLocationFragment.setTargetFragment(LocationsFragment.this, NEW_LOCATION_ACTIVITY_REQUEST_CODE);
        getFragmentManager().beginTransaction().replace(R.id.location_fragment_container, newLocationFragment).addToBackStack(null).commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_LOCATION_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Location location = new Location(data.getStringExtra("name"), data.getStringExtra("address"),data.getStringExtra("phone_number"), data.getStringExtra("type"), data.getStringExtra("kitchen"), data.getStringExtra("opening_time") + "-" + data.getStringExtra("closing_time"));
            viewModel.insert(location);

        } else {
            Toast.makeText(
                    getActivity().getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
