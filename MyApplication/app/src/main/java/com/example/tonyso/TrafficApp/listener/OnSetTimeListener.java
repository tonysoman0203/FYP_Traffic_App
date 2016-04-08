package com.example.tonyso.TrafficApp.listener;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.tonyso.TrafficApp.fragment.InfoDetailFragment;
import com.example.tonyso.TrafficApp.utility.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by soman on 2015/12/26.
 */
public class OnSetTimeListener implements View.OnFocusChangeListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    public int hour , minute;
    public String date;
    private EditText editText;
    private InfoDetailFragment context;

    public OnSetTimeListener(EditText editText, InfoDetailFragment ctx) {
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        this.context = ctx;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if(hasFocus){
            editText.setInputType(InputType.TYPE_NULL);
            Calendar myCalendar = Calendar.getInstance();
            new DatePickerDialog(context.getContext(), this,
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
        try {
            //check Select Date >= CurrTime
            Date selectTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.TRADITIONAL_CHINESE).parse(date + " " + hour + ":" + minute);
            Date currTime = DateTime.getCurrentDate();
            if (selectTime.before(currTime)) {
                Snackbar.make(context.coordinatorLayout, "開始時間不可大於結束時間", Snackbar.LENGTH_LONG).show();
                new TimePickerDialog(context.getContext(), this, hour, this.minute, true).show();
            } else
                this.editText.setText(String.format("%s %02d:%02d", date, hourOfDay, minute));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
        Log.e("Date Set", date);
        Calendar myCalendar = Calendar.getInstance();
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        new TimePickerDialog(context.getContext(), this, hour, minute, true).show();
    }
}
