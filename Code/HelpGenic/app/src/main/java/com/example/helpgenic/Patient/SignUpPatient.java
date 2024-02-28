package com.example.helpgenic.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.helpgenic.Classes.AccountHandler;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.GuestUser;

import com.example.helpgenic.R;
import com.example.helpgenic.SignUpDonor;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class SignUpPatient extends AppCompatActivity {

    Button submitBtn;
    private GuestUser usr = new GuestUser();
    AutoCompleteTextView bloodGroups;
    String[] bloodGrps = {"A+" , "A-" , "B+","B-","AB+","AB-","O+","O-" };
    EditText nameField, emailField,phoneNumField, password1Field , password2Field, gender;
    TextView dateOfBirth ;
    Date dob = null;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_patient);

        submitBtn = findViewById(R.id.submitButtonPatient);

        nameField = findViewById(R.id.patientName);
        emailField = findViewById(R.id.patientEmail);
        phoneNumField = findViewById(R.id.patientPhoneNumber);
        password1Field = findViewById(R.id.patientPassword);
        password2Field = findViewById(R.id.patientConfirmPassword);
        bloodGroups = findViewById(R.id.bloodGroups);
        dateOfBirth = findViewById(R.id.patientDOB);
        gender = findViewById(R.id.patientGender);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext() , android.R.layout.simple_spinner_dropdown_item , bloodGrps);
        bloodGroups.setAdapter(adapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AccountHandler ah =  new AccountHandler();
                usr.setAh(ah);

                GuestUser obj = usr.SignUpPatient(nameField,emailField,phoneNumField, password1Field,password2Field,gender,dob, bloodGroups,SignUpPatient.this);


                if (obj != null) {

                    finish();


                }


            }


        });

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpPatient.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private void updateLabel(){

        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);

        java.util.Date t = myCalendar.getTime();


        dob = new java.sql.Date(t.getTime());
        dateOfBirth.setText(dob.toString());


    }

}
