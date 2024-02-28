/*------------------------------------------------Activity-----------------------------------------------*/
package com.example.helpgenic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.AccountHandler;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Donor;
import com.example.helpgenic.Classes.GuestUser;
import com.example.helpgenic.Classes.Slot;
import com.example.helpgenic.Patient.DisplayingSlots;
import com.example.helpgenic.PatientAdapters.ListViewDsiplayingSlotsAdapter;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SignUpDonor extends AppCompatActivity {

    AutoCompleteTextView bloodGroups;
    String[] bloodGrps = {"A+" , "A-" , "B+","B-","AB+","AB-","O+","O-" };
    EditText name, email, password, gender, phoneNo, _address;
    Button createAccount;
    TextView dateOfBirth ;
    java.sql.Date dateSelected = null;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_donor);

        name = findViewById(R.id.editTextTextPersonName5);
        email = findViewById(R.id.editTextTextEmailAddress3);
        password = findViewById(R.id.editTextTextPassword3);
        gender = findViewById(R.id.editTextTextPersonName8);
        phoneNo = findViewById(R.id.editTextTextPersonName9);
        createAccount = findViewById(R.id.submitButtonDonor);
        bloodGroups = findViewById(R.id.bloodGroups);
        dateOfBirth = findViewById(R.id.donorDob);
        _address = findViewById(R.id.address);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext() , android.R.layout.simple_spinner_dropdown_item , bloodGrps);
        bloodGroups.setAdapter(adapter);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                char G = getGender(gender.getText().toString());
                AccountHandler accountsHandler = new AccountHandler();

                if (accountsHandler.verifyCredentials(name,email,password,gender,phoneNo,dateSelected,_address, SignUpDonor.this))
                {
                    GuestUser guestUser = new GuestUser(name.getText().toString(),email.getText().toString(),password.getText().toString(),G, dateSelected);
                    Donor donor = new Donor(name.getText().toString(),email.getText().toString(),password.getText().toString(),G,dateSelected, bloodGroups.getText().toString(),phoneNo.getText().toString(),_address.getText().toString());

                    DbHandler dbHandler = new DbHandler();
                    dbHandler.connectToDb(getApplicationContext());

                    // check if there is already account with this email
                    if (dbHandler.verifyUser(email.getText().toString(), getApplicationContext()))
                    {
                        //Toast.makeText(SignUpDonor.this, "Yes", Toast.LENGTH_SHORT).show();
                        int id = dbHandler.insertUser(guestUser,"d",getApplicationContext());

                        if (id>0)
                        {
                            dbHandler.insertDonor(donor,id,getApplicationContext());
                            Toast.makeText(getApplicationContext(), "Thanks for registration", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"id<0",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Account already exists with this email",Toast.LENGTH_LONG).show();
                    }

                    try {
                        dbHandler.closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                }
            }
        });

        bloodGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "Item: "+ s, Toast.LENGTH_SHORT).show();
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
                new DatePickerDialog(SignUpDonor.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




    }
    private char getGender(String g)
    {
        if (Objects.equals(g, "M"))
            return 'M';
        else
            return 'F';
    }

    @SuppressLint("SimpleDateFormat")
    private void updateLabel(){

        Date t = myCalendar.getTime();


        dateSelected = new java.sql.Date(t.getTime());
        dateOfBirth.setText(dateSelected.toString());


    }

}