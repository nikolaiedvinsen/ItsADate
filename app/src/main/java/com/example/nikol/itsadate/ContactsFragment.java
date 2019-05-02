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

public class ContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsRVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ContactsViewModel viewModel;
    public static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    public static final int SHOW_CONTACT_ACTIVITY_REQUEST_CODE = 2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fragment_layout, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.contacts_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<Contact> contacts = new ArrayList<>();

        adapter = new ContactsRVAdapter(contacts);
        adapter.setFragment(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(ContactsViewModel.class);
        viewModel.getAllEntries().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable final List<Contact> contacts) {
                // Update the cached copy of the contacts in the adapter.
                adapter.setDataSet(contacts);
            }
        });

        //eksempler
        viewModel.insert(new Contact("Ragnar Stormo", "12233423"));
        viewModel.insert(new Contact("Silje Forsum", "85949422"));
        viewModel.insert(new Contact("Oddvar Storjord", "32429843"));


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
                launchNewContactFragment();
            }
        });
    }


    public void launchShowContactFragment(Contact contact) {
        ShowContactFragment showContactFragment = new ShowContactFragment();
        showContactFragment.setContact(contact);
        showContactFragment.setTargetFragment(ContactsFragment.this, 2);
        getFragmentManager().beginTransaction().replace(R.id.contacts_fragment_container, showContactFragment).addToBackStack(null).commit();


    }

    public void launchNewContactFragment() {
        NewContactFragment newContactFragment = new NewContactFragment();
        newContactFragment.setTargetFragment(ContactsFragment.this, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
        getFragmentManager().beginTransaction().replace(R.id.contacts_fragment_container, newContactFragment).addToBackStack(null).commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Contact contact = new Contact(data.getStringExtra("name"), data.getStringExtra("phone_number"));
            viewModel.insert(contact);

        } else {
            Toast.makeText(
                    getActivity().getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

}
