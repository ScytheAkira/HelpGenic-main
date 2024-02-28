/*------------------------------------------------Activity-----------------------------------------------*/
package com.example.helpgenic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helpgenic.Admin.AdminPage;
import com.example.helpgenic.Classes.Admin;
import com.example.helpgenic.Classes.AuthenticateUser;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.GuestUser;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.Doctor.DocPage;
import com.example.helpgenic.Patient.PatientPage;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class login extends AppCompatActivity {

    private Button signUp, logIn;
    private GuestUser usr = new GuestUser();

    EditText emailField, passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // ==================================== Checking if already user was login =====================================
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);


        if (sh.getInt("Id", 0) != 0) {

            if (Objects.equals(sh.getString("type", null), "A")) {

                 Intent intent = new Intent(login.this, AdminPage.class);
                 intent.putExtra("admin", new Admin(sh.getInt("Id",0),sh.getString("email", null)));

                startActivity(intent);
                finish();

            }else if (Objects.equals(sh.getString("type", null), "P"))
            {

                Intent intent = new Intent(login.this, PatientPage.class);
                Date date1 = null;

                try {

                    // convert date(String) to date(Date)
                    DateFormat formatter;
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Toast.makeText(this, sh.getString("dob", null), Toast.LENGTH_SHORT).show();
                    java.util.Date temp = (java.util.Date) formatter.parse(sh.getString("dob", null));
                    date1 = new Date(temp.getTime());


                } catch (Exception e) {

                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                // get the patient object to pass on to next activity
                Patient p = new Patient(sh.getInt("Id" , 0) , sh.getString("email", null)  , null  ,  sh.getString("name", null)  ,  sh.getBoolean("gender", false), date1, sh.getString("bloodGroup", null), sh.getString("phNum", null) );


                intent.putExtra("patient", p );
                startActivity(intent);
                finish();

            }

            else if ( Objects.equals(sh.getString("type", null), "D") ) {

                Intent intent = new Intent(login.this, DocPage.class);
//                Date date1 = null;
//
//                try {
//
//                    // convert date(String) to date(Date)
//                    DateFormat formatter;
//                    formatter = new SimpleDateFormat("yyyy-MM-dd");
//                    java.util.Date temp = (java.util.Date) formatter.parse(sh.getString("dob", null));
//                    date1 = new Date(temp.getTime());
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }


                char c = 'F';
                if(sh.getBoolean("gender", false)){
                    c = 'M';
                }
                Doctor d  = new Doctor(sh.getInt("Id",0), sh.getString("email",null), sh.getString("specialization", null), sh.getBoolean("isSurgeon",false),sh.getString("accNum",null), sh.getString("name",null), c,null , sh.getFloat("rating",0.0f) );
                intent.putExtra("doctor", d);
                startActivity(intent);
                finish();

            } else {
                /* TODO: donor work latter */
            }

        }

        //========================================================================================================


        signUp = findViewById(R.id.getStarted);
        logIn = findViewById(R.id.loginBtn);



        // when submit button clicked
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AuthenticateUser au = new AuthenticateUser();

                // get reference to the fields on the xml file
                emailField = findViewById(R.id.loginEmail);
                passwordField = findViewById(R.id.loginPassword);

                usr.setAu(au);
                // Make the user login , if get verified
                GuestUser obj = usr.logIn(emailField, passwordField, login.this);


                // if user verified
                if (obj != null) {

                    // get type of the user
                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    String type = sh.getString("type", null);


                    //------------------------------------------

                    if (Objects.equals(type, "A")) {
                        // if admin then take him to the admin page
                        Intent intent = new Intent(login.this, AdminPage.class);
                        intent.putExtra("admin", obj);
                        startActivity(intent);
                        finish();

                    } else if (Objects.equals(type, "P")) {
                        // if patient then take him to the patient page
                        Intent intent = new Intent(login.this, PatientPage.class);
                        intent.putExtra("patient", obj);
                        startActivity(intent);
                        finish();


                    } else if (Objects.equals(type, "D")) {
                        // if doctor then take him to the doctor page
                        Intent intent = new Intent(login.this, DocPage.class);
                        intent.putExtra("doctor", obj);
                        startActivity(intent);
                        finish();

                    } else {
                        // donor work
                    }


                }


            }
        });

        // if sign up  button pressed then take him to the sign up page
        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, GetStarted.class));
            }
        });
    }

}