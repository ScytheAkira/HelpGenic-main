package com.example.helpgenic.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.Classes.ReportsHandler;
import com.example.helpgenic.CommonAdapters.ListViewAppointmentsAdapter;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class DocViewingPatientProfile extends AppCompatActivity {

    ListView appointmentsListRef;
    TextView name , email , phNum , bloodGroup , age, gender;
    int patientId = 0,docId= 0;
    ReportsHandler rh = new ReportsHandler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_viewing_patient_profile);

        patientId = getIntent().getIntExtra("patientID", 0);
        docId = getIntent().getIntExtra("docID", 0);

        name = findViewById(R.id.pName);
        email = findViewById(R.id.email);
        phNum = findViewById(R.id.phNum);
        bloodGroup = findViewById(R.id.bloodGrp);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);



        appointmentsListRef = findViewById(R.id.appointments);


        setUpData(DocViewingPatientProfile.this);

        appointmentsListRef.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appointment apt = (Appointment)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(DocViewingPatientProfile.this, AppointmentDocsViewedByDoc.class);
                intent.putExtra("aptId" , apt.getAppId());
                Toast.makeText(DocViewingPatientProfile.this, Integer.toString(apt.getAppId()), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


    }


    void setUpData(Context context){
        // setting up textViews
        DbHandler db = new DbHandler();
        db.connectToDb(context);

        try{
            Patient p = db.getPatientInfo(patientId,context);

            if(p != null){
                name.setText(p.getName());
                email.setText(p.getMail());
                phNum.setText(p.getPhNum());
                bloodGroup.setText(p.getBloodGroup());
                age.setText(Integer.toString(p.getAge()));
                if(Objects.equals(p.getGender(),'M') ){
                    gender.setText("Male");
                }else{
                    gender.setText("Female");
                }
            }

            rh.setDb(db);
            rh.displayPreviousAppointments(docId , patientId ,DocViewingPatientProfile.this, appointmentsListRef);

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }



}