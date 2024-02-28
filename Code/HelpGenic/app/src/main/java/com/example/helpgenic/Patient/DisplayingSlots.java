package com.example.helpgenic.Patient;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helpgenic.Classes.AlarmHandler;
import com.example.helpgenic.Classes.BookingManager;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.Classes.Slot;
import com.example.helpgenic.Classes.VirtualAppointmentSchedule;
import com.example.helpgenic.MyReciever;
import com.example.helpgenic.PatientAdapters.ListViewDsiplayingSlotsAdapter;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DisplayingSlots extends AppCompatActivity {


    ListView lst;
    Doctor d;
    VirtualAppointmentSchedule vs;
    Patient p;
    final Calendar myCalendar= Calendar.getInstance();
    Button dateBtn;
    ArrayList<Slot> slots;
    java.sql.Date dateSelected;
    BookingManager bm = new BookingManager();
    int selectedPosition = -1;
    Slot selectedSlot = null;
    Button bookApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_slots);

        lst = findViewById(R.id.slotsList);
        bookApp = findViewById(R.id.bookApp);


        // getting doctor , patient , and corresponding clicked vSchedule row's information.
        d = (Doctor) getIntent().getSerializableExtra("doc");
        vs = (VirtualAppointmentSchedule) getIntent().getSerializableExtra("vs");
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        p = new Patient(sh.getInt("Id" , 0),null, null , null , false , null  , null , null);

        slots = bm.makeSlots(vs.getsTime(),vs.geteTime(),vs.getDay()); // all total possibilities


        // ====================== selecting date ==================================

        dateBtn =(Button) findViewById(R.id.date);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();



                long millis = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);

                String dayWeekText = new SimpleDateFormat("EEEE").format(dateSelected);

                if( (Objects.equals(vs.getDay() , dayWeekText)) && (dateSelected.compareTo(date) > 0 || Objects.equals(dateSelected.toString() , date.toString() )) ){

                    DbHandler db = new DbHandler();
                    db.connectToDb(DisplayingSlots.this);
                    bm.setDb(db);

                    ArrayList<Slot> availableSlots = bm.getAvailableSlots(d.getId() , dateSelected, vs.getDay() , slots, DisplayingSlots.this );
                    ListViewDsiplayingSlotsAdapter adapter = new ListViewDsiplayingSlotsAdapter(DisplayingSlots.this,0,availableSlots);

                    lst.setAdapter(adapter);

                    lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                            if(selectedPosition != -1){
                                lst.getChildAt(selectedPosition).setBackgroundColor(Color.TRANSPARENT);
                            }

                            try{
                                selectedSlot = (Slot)adapterView.getItemAtPosition(i);
                                lst.getChildAt(i).setBackgroundColor(Color.rgb(165,184,166));
                                selectedPosition = i;
                            }catch (Exception e){
                                Toast.makeText(DisplayingSlots.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    try {
                        db.closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }else{
                    Toast.makeText(DisplayingSlots.this, "Invalid Date Selected", Toast.LENGTH_SHORT).show();
                }




            }
        };


        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DisplayingSlots.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // ===========================================================================



        // listen for book appointment button

        bookApp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                p.setBm(bm);
                int appId = p.confirmAppointment(p.getId() , d.getId() ,dateSelected ,selectedSlot , DisplayingSlots.this);

                if(appId != -1){

                    AlarmHandler ah = new AlarmHandler();
                    // setting alarm
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    ah.setAlarm(appId , dateSelected , selectedSlot.sTime , DisplayingSlots.this , alarmManager);

                    Toast.makeText(DisplayingSlots.this, "Appointment Scheduled", Toast.LENGTH_SHORT).show();

                    finish();

                }

            }
        });

    }


    @SuppressLint("SimpleDateFormat")
    private void updateLabel(){

        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);

        Date t = myCalendar.getTime();

        String date= dateFormat.format(myCalendar.getTime());
        dateBtn.setText(date);


        dateSelected = new java.sql.Date(t.getTime());




    }



}