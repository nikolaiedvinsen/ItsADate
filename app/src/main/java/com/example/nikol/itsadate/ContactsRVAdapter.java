package com.example.nikol.itsadate;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactsRVAdapter extends RecyclerView.Adapter<ContactsRVAdapter.ContactViewHolder>{

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView contactName;
        TextView contactNumber;

        ContactViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.contact_card_view);
            contactName = itemView.findViewById(R.id.contact_name);
            contactNumber = itemView.findViewById(R.id.contact_number);
        }
    }

    private ContactsFragment contactsFragment;
    private List<Contact> dataSet;

    ContactsRVAdapter(List<Contact> dataSet){
        this.dataSet = dataSet;
    }

    public void setFragment(ContactsFragment contactsFragment) {
        this.contactsFragment = contactsFragment;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDataSet(List<Contact> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_card, viewGroup, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ContactViewHolder holder, int i) {
        holder.contactName.setText(dataSet.get(i).getName());
        holder.contactNumber.setText(dataSet.get(i).getPhoneNumber());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactsFragment.launchShowContactFragment(dataSet.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}