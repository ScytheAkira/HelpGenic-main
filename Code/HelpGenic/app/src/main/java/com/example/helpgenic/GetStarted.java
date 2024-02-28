package com.example.helpgenic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Doctor.SignUpDoc;
import com.example.helpgenic.Patient.SignUpPatient;

import java.sql.Connection;
import java.sql.DriverManager;

public class GetStarted extends AppCompatActivity {
    Button doc , donor , patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        doc = findViewById(R.id.signUpDoc);
        donor = findViewById(R.id.signUpDonor);
        patient = findViewById(R.id.signUpPatient);


        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetStarted.this, SignUpDoc.class));
            }
        });

        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetStarted.this, SignUpDonor.class));
            }
        });


        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetStarted.this, SignUpPatient.class));
            }
        });
    }
}