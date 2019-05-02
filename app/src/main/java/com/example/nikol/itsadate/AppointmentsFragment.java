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

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppointmentsRVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public AppointmentsViewModel viewModel;
    public static final int NEW_APPOINTMENT_ACTIVITY_REQUEST_CODE = 1;
    public static final int SHOW_APPOINTMENT_ACTIVITY_REQUEST_CODE = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointments_fragment_layout, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.appointments_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<Appointment> appointments = new ArrayList<>();
        adapter = new AppointmentsRVAdapter(appointments);
        adapter.setFragment(this);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(AppointmentsViewModel.class);
        viewModel.getAllEntries().observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(@Nullable final List<Appointment> appointments) {
                // Update the cached copy of the appointments in the adapter.
                adapter.setDataSet(appointments);
            }
        });

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
                launchNewAppointmentFragment();
            }
        });
    }
    public void launchShowAppointmentFragment(Appointment appointment) {
        ShowAppointmentFragment showAppointmentFragment = new ShowAppointmentFragment();
        showAppointmentFragment.setAppointment(appointment);
        showAppointmentFragment.setTargetFragment(AppointmentsFragment.this, SHOW_APPOINTMENT_ACTIVITY_REQUEST_CODE);
        getFragmentManager().beginTransaction().replace(R.id.appointment_fragment_container, showAppointmentFragment).addToBackStack(null).commit();


    }

    public void launchNewAppointmentFragment() {
        NewAppointmentFragment newAppointmentFragment = new NewAppointmentFragment();
        newAppointmentFragment.setTargetFragment(AppointmentsFragment.this, NEW_APPOINTMENT_ACTIVITY_REQUEST_CODE);
        getFragmentManager().beginTransaction().replace(R.id.appointment_fragment_container, newAppointmentFragment).addToBackStack(null).commit();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_APPOINTMENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Appointment appointment = new Appointment(data.getStringExtra("name"), data.getStringExtra("location"), data.getStringExtra("attendees"), data.getStringExtra("date"), data.getStringExtra("time"));
            viewModel.insert(appointment);

        } else {
            Toast.makeText(
                    getActivity().getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
