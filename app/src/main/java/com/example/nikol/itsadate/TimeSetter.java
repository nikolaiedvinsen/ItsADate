package com.example.nikol.itsadate;

import android.app.TimePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeSetter implements View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private TextInputEditText editText;
    private Calendar calendar;
    private SimpleDateFormat format;

    public TimeSetter(TextInputEditText editText){
        this.editText = editText;
        editText.setOnFocusChangeListener(this);
        editText.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus){
            showPicker(view);
        }
    }

    @Override
    public void onClick(View view) {
        showPicker(view);
    }

    private void showPicker(View view) {
        if (calendar == null)
            calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(view.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this, hour, minute, true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        if (format == null)
            format = new SimpleDateFormat("HH:mm", Locale.getDefault());

        this.editText.setText(format.format(calendar.getTime()));
    }

}