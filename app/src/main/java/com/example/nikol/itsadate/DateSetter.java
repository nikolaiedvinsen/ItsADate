package com.example.nikol.itsadate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateSetter implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, View.OnClickListener {

    TextInputEditText editText;
    Calendar calendar;

    private DateFormat format;

    public DateSetter(TextInputEditText editText){
        this.editText = editText;
        editText.setOnFocusChangeListener(this);
        editText.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

    }

    @Override
    public void onClick(View view) {
        showPicker(view);
    }

    private void showPicker(View view) {
        if (calendar == null)
            calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(view.getContext(),
                this, year, month, day).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);


        if (format == null)
            format = DateFormat.getDateInstance(DateFormat.SHORT);
        this.editText.setText(format.format(cal.getTime()));
    }

}