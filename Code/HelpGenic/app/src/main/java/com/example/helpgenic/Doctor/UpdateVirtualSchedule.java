package com.example.helpgenic.Doctor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.VirtualAppointmentSchedule;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpdateVirtualSchedule extends AppCompatActivity {

    DbHandler dbHandler = new DbHandler();
    Spinner spinner;
    String[] Days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    String selectedDay;
    Button startTime,endTime,submit;
    int hour,minutes,sec,hour2,minutes2;
    LocalTime t1,t2;
    String start,end;
    ArrayList<CharSequence> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_virtual_schedule);

        spinner = findViewById(R.id.spinner);
        startTime = findViewById(R.id.start);
        endTime= findViewById(R.id.end);
        submit = findViewById(R.id.submit);
        days = (ArrayList<CharSequence>) getIntent().getCharSequenceArrayListExtra("days");

        VirtualAppointmentSchedule sch = (VirtualAppointmentSchedule) getIntent().getSerializableExtra("vSch");
        selectedDay = sch.getDay();
        Time time1 = sch.getsTime();
        Time time2 = sch.geteTime();

        startTime.setText(time1.toString().substring(0,5));
        endTime.setText(time2.toString().substring(0,5));


        int hr1 = Integer.parseInt(time1.toString().substring(0,2));
        int min1 = Integer.parseInt(time1.toString().substring(3,5));
        int hr2 = Integer.parseInt(time2.toString().substring(0,2));
        int min2 = Integer.parseInt(time2.toString().substring(3,5));


        t1=LocalTime.of(hr1,min1);
        t2=LocalTime.of(hr2,min2);


        // ===================================================================

        //---------------------- CLick Listners for Clock ---------------------//

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateVirtualSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selHour, int selMin) {
                        hour =selHour;
                        minutes=selMin;
                        //initialize  calander
                        Calendar calendar = Calendar.getInstance();
                        //Set selected Time on button
                        startTime.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minutes));
                        t1=LocalTime.of(hour,minutes);
                    }
                },12,0,true);
                timePickerDialog.updateTime(hour,minutes);
                timePickerDialog.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateVirtualSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selHour, int selMin) {
                        hour2 =selHour;
                        minutes2=selMin;
                        //initialize calender
                        Calendar calendar = Calendar.getInstance();
                        //Set selected Time on button
                        endTime.setText(String.format(Locale.getDefault(),"%02d:%02d",hour2,minutes2));
                        t2=LocalTime.of(hour2,minutes2);
                    }
                },12,0,true);
                timePickerDialog.updateTime(hour2,minutes2);
                timePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!days.contains(selectedDay)) {
                    // get time from both buttons in string
                    start = startTime.getText().toString() + ":00";
                    end = endTime.getText().toString() + ":00";

                    //compare the two times
                    int value = t1.compareTo(t2);
                    System.out.println(start);
                    System.out.println(end);
                    Time time1 = Time.valueOf(start); // startTime
                    Time time2 = Time.valueOf(end); // endTime


                    if (returnToProfile(value, start, end)) {


                        dbHandler.connectToDb(getApplicationContext());

                        dbHandler.updateVirtualSchedule(sch.getId(), time1, time2, selectedDay, UpdateVirtualSchedule.this);

                        try {
                            dbHandler.closeConnection();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sh.edit();
                        myEdit.putBoolean("vNeedToUpdate", true);
                        myEdit.apply();
                        finish();

                    }
                }else{
                    Toast.makeText(UpdateVirtualSchedule.this, "Already have a schedule at this day", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //---------------------------------------------------------------------//
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDay = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), selectedDay, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public boolean returnToProfile(int value, String t1, String t2){
        //int value=t1.compareTo(t2);
        boolean eFound=false;
        if(value==0){
            startTime.setError("Start and End times cannot be Equal");
            eFound=true;
        }
        else if(value>0){
            startTime.setError("Invalid Start Time");
            System.out.println(value);
            eFound=true;
        }else if(!checkSelectedTimings(t1,t2)){
            Toast.makeText(this, "Schedule must follow the format, hh:00:00 or hh:30:00", Toast.LENGTH_SHORT).show();
            eFound=true;
        }
        else{
            startTime.setError(null);
        }

        if(eFound)
            return false;
        else
            return true;
    }

    private boolean checkSelectedTimings(String t1 , String t2){
        boolean isvalid = false;
        if(Integer.parseInt(t1.substring(3,5)) == 30 || Integer.parseInt(t1.substring(3,5)) == 0){
            isvalid = true;
        }
        isvalid = false;
        if(Integer.parseInt(t2.substring(3,5)) == 30 || Integer.parseInt(t2.substring(3,5)) == 0){
            isvalid = true;
        }
        return isvalid;
    }

}