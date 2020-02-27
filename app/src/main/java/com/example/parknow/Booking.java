package com.example.parknow;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Booking extends AppCompatActivity {

    BookingDatabase myDb;

    EditText selectDate;//arrive date
    EditText leavingDate;//leaving date
    EditText selArrTime;//arrive time
    EditText selLeavTime;//leaving time
    TextView duration;//total duration
    TextView price;//total price

    EditText vehReg;//vehicle registration number

    Calendar c;
    DatePickerDialog datePickerDialog;
    int year, month, day;
    private int hour, min, hourFinal, minFinal;

    //globalise the variables

    String start_time;
    String end_time;

    String dateString1;
    String dateString2;

    long diffMinutes;
    long diffHours;

    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);//custom action bar enabled
        getSupportActionBar().setCustomView(R.layout.booking_actionbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDb = new BookingDatabase(this);

        selectDate = (EditText) findViewById(R.id.dateSelect);
        leavingDate = (EditText) findViewById(R.id.leavDate);
        selArrTime = (EditText) findViewById(R.id.strtArriv);
        selLeavTime = (EditText) findViewById(R.id.endLeav);
        duration = (TextView) findViewById(R.id.duration);
        price = (TextView) findViewById(R.id.price);

        vehReg = findViewById(R.id.vehReg);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(12);
        vehReg.setFilters(filterArray);

        //SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        //date = new Date();
        //showDate.setText(currentDate.format(date));//show current date

        /*SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        Date date2 = new Date();
        //LocalDateTime now = LocalDateTime.now();
        strtTime.setText(currentTime.format(date2));
        //show current time
         */

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);
                hour = c.get(Calendar.HOUR);
                min = c.get(Calendar.MINUTE);


                datePickerDialog = new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int nYear, int nMonth, int nDay) {
                        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        c.set(nYear, nMonth, nDay);
                        dateString1 = sdf.format(c.getTime());
                        selectDate.setText(dateString1);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        leavingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int nYear, int nMonth, int nDay) {
                        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        c.set(nYear, nMonth, nDay);
                        dateString2 = sdf.format(c.getTime());
                        leavingDate.setText(dateString2);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        selArrTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timepickerlistener1 = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        // this is the listener for the first timepicker
                        //get your start time from here

                        hour = i;
                        min = i1;
                        //strtTime.setText(hour +":" + min);

                        start_time = String.format("%02d:%02d", hour, min);
                        Log.d("START: ", String.valueOf(start_time));
                        selArrTime.setText(start_time);

                    }
                };
                TimePickerDialog mTimePicker1 = new TimePickerDialog(Booking.this, timepickerlistener1,
                        hour, min, true);//parameters ---- > context, listener for the timepicker, hour, minute, is24hours?
                mTimePicker1.show();
            }
        });


        selLeavTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timepickerlistener2 = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        // this is the listener for the second timepicker
                        //get your end time from here

                        hourFinal = i;
                        minFinal = i1;
                        //timeEnd.setText(hourFinal +":" + minFinal);
                        end_time = String.format("%02d:%02d", hourFinal, minFinal);

                        String begin_date_time = dateString1 + " " + start_time;
                        String fin_date_time = dateString2 + " " + end_time;

                        //Log.d("DATE/TIME", begin_date_time);
                        //Log.d("DATE/TIME", fin_date_time);

                        selLeavTime.setText(end_time);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                        Date d1 = null;
                        Date d2 = null;

                        try {
                            d1 = format.parse(begin_date_time);
                            d2 = format.parse(fin_date_time);

                            //in milliseconds
                            long diff = d2.getTime() - d1.getTime();

                            diffMinutes = diff / (60 * 1000) % 60;
                            diffHours = diff / (60 * 60 * 1000) % 24;

                            duration.setText(String.valueOf(diffHours)+"h"+" "+ String.valueOf(diffMinutes)+"m");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //Log.d("DURATION ", String.valueOf(diffHours));//TEST PRINT
                        //duration.setText(String.valueOf(dur_hour)+"h"+" "+ String.valueOf(dur_min)+"m");

                        double pricePerHour = diffHours*1.2;
                        double pricePerMin = diffMinutes * 0.017;
                        double totalPrice = pricePerHour + pricePerMin;

                        //Log.d("PRICE ", String.valueOf(totalPrice));//TEST PRINT
                        DecimalFormat number_format = new DecimalFormat("0.00"); //changes decimal places
                        price.setText("£" + String.valueOf(number_format.format(totalPrice)));

                    }
                };
                TimePickerDialog mTimePicker2 = new TimePickerDialog(Booking.this, timepickerlistener2,
                        hourFinal, minFinal, true);//parameters ---- > context, listener for the timepicker, hour, minute, is24hours?
                mTimePicker2.show();

            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
