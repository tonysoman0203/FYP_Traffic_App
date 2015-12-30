package com.example.tonyso.TrafficApp.listener;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by soman on 2015/12/26.
 */
public class OnSetTimeListener implements View.OnFocusChangeListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private EditText editText;
    private Calendar myCalendar;
    private Context context;
    public int hour , minute;
    public String date;

    public OnSetTimeListener(EditText editText, Context ctx){
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        this.myCalendar = Calendar.getInstance();
        this.context = ctx;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if(hasFocus){
            editText.setInputType(InputType.TYPE_NULL);
            new DatePickerDialog(context, this,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // TODO Auto-generated method stub
        this.hour = hourOfDay;
        this.minute = minute;
        this.editText.setText(String.format("%s %02d:%02d", date, hourOfDay, minute));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
        Log.e("Date Set", date);
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        new TimePickerDialog(context, this, hour, minute, true).show();
    }
}
