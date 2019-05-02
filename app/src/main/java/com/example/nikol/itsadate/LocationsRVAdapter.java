package com.example.nikol.itsadate;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationsRVAdapter extends RecyclerView.Adapter<LocationsRVAdapter.LocationViewHolder> implements View.OnClickListener {

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView photo;
        TextView name;
        TextView type;
        TextView kitchen;

        TextView address;
        TextView phoneNumber;
        TextView openingHours;
        TextView open;

        LocationViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.location_card_view);
            photo = itemView.findViewById(R.id.location_photo);
            name = itemView.findViewById(R.id.location_name);
            type = itemView.findViewById(R.id.location_type);
            kitchen = itemView.findViewById(R.id.location_kitchen);
            open = itemView.findViewById(R.id.location_open);
            phoneNumber = itemView.findViewById(R.id.location_phone_number);
            address = itemView.findViewById(R.id.location_address);
            openingHours = itemView.findViewById(R.id.location_opening_hours);
        }
    }

    private LocationsFragment locationsFragment;
    private List<Location> dataSet;

    LocationsRVAdapter(List<Location> dataSet){
        this.dataSet = dataSet;
    }

    public void setFragment(LocationsFragment locationsFragment) {
        this.locationsFragment = locationsFragment;
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDataSet(List<Location> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_card, viewGroup, false);
        LocationViewHolder holder = new LocationViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LocationViewHolder holder, int i) {
        switch(dataSet.get(i).getType()) {
            case "Restaurant":
                holder.photo.setImageResource(R.drawable.ic_restaurant);
                break;
            case "Café":
                holder.photo.setImageResource(R.drawable.ic_cafe);
                break;
            case "Bar":
                holder.photo.setImageResource(R.drawable.ic_bar);
                break;
        }
        holder.name.setText(dataSet.get(i).getName());
        holder.address.setText(dataSet.get(i).getAddress());
        holder.phoneNumber.setText((dataSet.get(i).getPhoneNumber()));
        holder.type.setText(dataSet.get(i).getType());
        holder.kitchen.setText(dataSet.get(i).getKitchen());
        holder.openingHours.setText(dataSet.get(i).getOpeningHours());
        if (!dataSet.get(i).getOpeningHours().equals("-")) {
            if (checkIfOpen(i)) {
                holder.open.setText("Open");
                holder.open.setTextColor(Color.GREEN);
            }
            else {
                holder.open.setText("Closed");
                holder.open.setTextColor(Color.BLACK);
            }
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationsFragment.launchShowLocationFragment(dataSet.get(holder.getAdapterPosition()));
            }
        });
    }

    private boolean checkIfOpen(int i) {
        SimpleDateFormat format = new SimpleDateFormat("HHmm",
                Locale.getDefault());
        String now = format.format(new Date());
        String[] openingHours = dataSet.get(i).getOpeningHours().split("-");
        String start = openingHours[0].replace(":", "");
        String end = openingHours[1].replace(":", "");
        boolean isBetween = Integer.valueOf(now) > Integer.valueOf(start)
                && Integer.valueOf(now) < Integer.valueOf(end);
        return isBetween;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View view) {

    }
}