package com.example.tonyso.TrafficApp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by soman on 2015/12/26.
 */
public class OnSetTimeListener implements View.OnFocusChangeListener,TimePickerDialog.OnTimeSetListener {
    private EditText editText;
    private Calendar myCalendar;
    private Context context;
    public int hour , minute;

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
            int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = myCalendar.get(Calendar.MINUTE);
            new TimePickerDialog(context, this, hour, minute, true).show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // TODO Auto-generated method stub
        hour = hourOfDay;
        this.minute = minute;
        this.editText.setText( hourOfDay + ":" + minute);
    }

}
