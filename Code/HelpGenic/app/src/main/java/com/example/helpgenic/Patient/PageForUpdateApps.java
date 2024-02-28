package com.example.helpgenic.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.AlarmHandler;
import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.BookingManager;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Slot;
import com.example.helpgenic.PatientAdapters.ListViewDsiplayingSlotsAdapter;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PageForUpdateApps extends AppCompatActivity {

    Appointment app;
    TextView date;
    ArrayList<Slot> slots;
    ListView lst;
    int selectedPos = -1;
    Slot selectedSlot=null;
    Button updateBtn, cancelBtn;
    DbHandler db = null;

    public void setSharedPref(){
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        myEdit.putBoolean("isNeedToUpdate" , true);
        myEdit.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_for_update_apps);


        // get previous appointment details

        app = (Appointment) getIntent().getExtras().getSerializable("appointment");
        Toast.makeText(this, app.getAppDate().toString(), Toast.LENGTH_SHORT).show();

        // // setting prior appointment date to current page
        date = findViewById(R.id.date);
        date.setText(app.getAppDate().toString());


        // getting corresponding week to appointment date
        String dayWeekText = new SimpleDateFormat("EEEE", Locale.US).format(app.getAppDate());

        db = new DbHandler();
        db.connectToDb(this);

        BookingManager bm = new BookingManager();
        bm.setDb(db);

        // getting available slots
        slots = bm.makeSlots(app.getDoc().getvSchedule().get(0).getsTime() , app.getDoc().getvSchedule().get(0).geteTime() ,dayWeekText);
        ArrayList<Slot> availableSlots = bm.getAvailableSlots(app.getDoc().getId() , app.getAppDate(),dayWeekText,slots ,this);


        // displaying slots to patient
        lst = findViewById(R.id.list);
        ListViewDsiplayingSlotsAdapter adapter = new ListViewDsiplayingSlotsAdapter(this,0,availableSlots);
        lst.setAdapter(adapter);





        // getting selected slot
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectedPos != -1){
                    lst.getChildAt(selectedPos).setBackgroundColor(Color.TRANSPARENT);
                }

                try{
                    selectedSlot = (Slot)adapterView.getItemAtPosition(i);
                    lst.getChildAt(i).setBackgroundColor(Color.rgb(165,184,166));
                    selectedPos = i;

                }catch (Exception e){
                    Toast.makeText(PageForUpdateApps.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateBtn = findViewById(R.id.updateApp);

        updateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(selectedSlot != null){

                    // updating appointment
                    db.updateAppointmentInDatabase(app.getAppId() ,selectedSlot.sTime , selectedSlot.eTime,PageForUpdateApps.this);

                    // Change Alarm timings that is already set
                    AlarmHandler ah = new AlarmHandler();
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    ah.setAlarm(app.getAppId() , app.getAppDate() , selectedSlot.sTime , PageForUpdateApps.this , alarmManager);


                    // TODO: Send email to doctor again for this change


                    // signal that we have to know display the updated list of upcoming appointments
                    setSharedPref();

                    finish();


                }
                else{
                    Toast.makeText(PageForUpdateApps.this, "Select some slot first !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // cancel Appointment

        cancelBtn = findViewById(R.id.cancel_Btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.cancelAppointment(app.getAppId() , PageForUpdateApps.this);

                setSharedPref();

                // Change Alarm timings that is already set
                AlarmHandler ah = new AlarmHandler();
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                ah.cancelAlarm(app.getAppId() , PageForUpdateApps.this,alarmManager);

                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //    @Override
//    protected void onPause() {
//
//        super.onPause();
//        try {
//            db.closeConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}