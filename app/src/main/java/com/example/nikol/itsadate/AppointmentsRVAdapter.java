package com.example.nikol.itsadate;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AppointmentsRVAdapter extends RecyclerView.Adapter<AppointmentsRVAdapter.AppointmentViewHolder> implements View.OnClickListener {

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView locationName;
        TextView locationAddress;
        TextView locationType;
        TextView date;
        TextView time;


        AppointmentViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.appointment_card_view);
            name = itemView.findViewById(R.id.appointment_name);
            locationName = itemView.findViewById(R.id.appointment_location_name);
            locationAddress = itemView.findViewById(R.id.appointment_location_address);
            locationType = itemView.findViewById(R.id.appointment_location_type);
            date = itemView.findViewById(R.id.appointment_date);
            time = itemView.findViewById(R.id.appointment_time);
        }
    }

    private AppointmentsFragment appointmentsFragment;
    private List<Appointment> dataSet;
    Location location;

    AppointmentsRVAdapter(List<Appointment> dataSet){
        this.dataSet = dataSet;
    }

    public void setFragment(AppointmentsFragment appointmentsFragment) {
        this.appointmentsFragment = appointmentsFragment;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDataSet(List<Appointment> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.appointment_card, viewGroup, false);
        AppointmentViewHolder holder = new AppointmentViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AppointmentViewHolder holder, int i) {
        location = appointmentsFragment.viewModel.getLocationWherePK(dataSet.get(i).getLocationName());

        holder.name.setText(dataSet.get(i).getName());
        holder.date.setText(dataSet.get(i).getDate());
        holder.time.setText(dataSet.get(i).getTime());

        holder.locationName.setText(dataSet.get(i).getLocationName());
        holder.locationAddress.setText(location.getAddress());
        holder.locationType.setText(location.getType());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentsFragment.launchShowAppointmentFragment(dataSet.get(holder.getAdapterPosition()));
            }
        });

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View view) {

    }
}