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

public class ShowContactFragment extends Fragment {

    private ContactsViewModel viewModel;
    private ActionBar toolbar;
    private LinearLayout layout;
    private Menu menu;
    private Contact contact;
    private TextInputEditText showNameView;
    private TextInputEditText showAddressView;
    private TextInputEditText showPhoneNumberView;
    private TextInputEditText showKitchenView;

    private TextInputEditText showOpeningTime;
    private TextInputEditText showClosingTime;

    private Spinner typeSpinner;
    private boolean editing = false;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_contact_fragment_layout, container, false);

        setHasOptionsMenu(true);
        hideItems();
        viewModel = ViewModelProviders.of(getActivity()).get(ContactsViewModel.class);
        layout = view.findViewById(R.id.show_contact_layout);

        MainActivity.previousFragment = "ShowContactFragment";

        //toolbar
        toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Contact");

        //Text input
        showNameView = view.findViewById(R.id.contact_show_name);
        showPhoneNumberView = view.findViewById(R.id.contact_show_phone_number);

        setText();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean("editing");
            if (editing) {
                setFocusable(true);
            }
            contact = new Contact(savedInstanceState.getString("name"),
                    savedInstanceState.getString("phone_number")
            );
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("editing", editing);
        savedInstanceState.putString("name", contact.getName());
        savedInstanceState.putString("phone_number", contact.getPhoneNumber());
    }

    private void setText() {
        if (contact != null) {
            showNameView.setText(contact.getName());
            showPhoneNumberView.setText(contact.getPhoneNumber());
        }
    }

    private void hideItems() {
        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        RecyclerView recyclerView = getActivity().findViewById(R.id.contacts_recycler_view);
        recyclerView.setVisibility(View.GONE);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
    }

    public void setContact(Contact contact) {
        this.contact = contact;
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
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_confirm).setVisible(true);
        toolbar.setHomeAsUpIndicator(R.drawable.ic_close);
        setFocusable(true);
    }

    public void onActionConfirm() {
        editing = false;
        toolbar.setHomeAsUpIndicator(R.drawable.ic_back);
        menu.findItem(R.id.action_delete).setVisible(true);
        menu.findItem(R.id.action_confirm).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(true);

        if (contact != null) {
            viewModel.delete(contact);
            contact = new Contact(showNameView.getText().toString(),
                    showPhoneNumberView.getText().toString()
            );
            viewModel.insert(contact);
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
        showPhoneNumberView.setFocusableInTouchMode(state);
        showPhoneNumberView.setClickable(state);
        showPhoneNumberView.setFocusable(state);
    }


    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.delete(contact);
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
