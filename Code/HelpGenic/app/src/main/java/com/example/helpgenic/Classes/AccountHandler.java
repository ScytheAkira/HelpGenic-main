package com.example.helpgenic.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import aws.sdk.kotlin.services.s3.model.SelectObjectContentEventStream;


public class AccountHandler {



    DbHandler db;

    public void setDb(DbHandler db) {
        this.db = db;
    }

    public GuestUser validatePatientCredentials(EditText name , EditText email, EditText phoneNumber , EditText gender , EditText password , EditText password2 , Date dob, AutoCompleteTextView bloodGroup , Context context ){

        if (name.length()==0)
        {
            name.setError("This field is required");
            return null;
        }
        if (email.length() == 0) {
            email.setError("This field is required");
            return null;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Invalid Email!");
            return null;
        }
        if (phoneNumber.length() == 0) {
            phoneNumber.setError("This field is required");
            return null;
        }
        if (password.length() == 0) {
            password.setError("This field is required");
            return null;
        }
        if (password2.length() == 0) {
            password2.setError("This field is required");
            return null;

        }if (dob== null) {
            Toast.makeText(context, "DOB can't be empty", Toast.LENGTH_SHORT).show();
            return null;
        }
        if(!password.getText().toString().equals(password2.getText().toString())){
            password2.setError("Passwords don't match");
            return null;
        }
        if(gender.length() == 0){
            gender.setError("This field is required");

            return null;
        }

        if(gender.length() != 0){


            if( ! (Objects.equals(gender.getText().toString(),"Male") || Objects.equals(gender.getText().toString(),"male") || Objects.equals(gender.getText().toString(),"Female") || Objects.equals(gender.getText().toString(),"female")) ){
                gender.setError("Invalid gender entered");
                return null;
            }

        }

        if(dob != null) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            String date = sdf.format(calendar.getTime());
            java.util.Date currdate1 = null;
            try {
                currdate1 =new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert currdate1 != null;
            if(currdate1.compareTo(dob) < 0){

                Toast.makeText(context, "Invalid DOB !", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        // TODO: check gender field


        db = new DbHandler();
        // create connection
        if (!db.isConnectionOpen()) {
            // =============== connect with db ===============
            db.connectToDb(context);
            // ==================================================
        }



        boolean isAlreadyExists = db.matchPatientCredentials(email.getText().toString()  ,context);





        if (!isAlreadyExists) {


            boolean g = false;
            if(Objects.equals(gender.getText().toString(),"Male") || Objects.equals(gender.getText().toString(),"male")){
                g = true;
            }

            Date dateOfBirth = null;
            DateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date temp = null;

            try {
                temp = (java.util.Date) formatter.parse(dob.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateOfBirth = new Date(temp.getTime());


            Toast.makeText(context, "Inserted", Toast.LENGTH_SHORT).show();
            int id = db.insertPatientDetailsInDb(name.getText().toString(), email.getText().toString(), password.getText().toString(),g ,dateOfBirth,bloodGroup.getText().toString(),phoneNumber.getText().toString(),context );


            // closing connection
            try {
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            return new Patient(id,email.getText().toString(),null,name.getText().toString(),g,dateOfBirth,bloodGroup.getText().toString(),phoneNumber.getText().toString());

        }

        return null;

    }


    public boolean verifyCredentials(EditText name, EditText email, EditText password, EditText gender, EditText phoneNo, Date _date, EditText _addr, Context c) {
        boolean flag = true;
        if (name.length() == 0) {
            name.setError("Name cannot be empty!");
            flag = false;
        } else if (name.length() <= 4) {
            name.setError("Minimum length should be 5!");
            flag = false;
        }

        if (email.length() == 0) {
            email.setError("Email cannot be empty!");
            flag = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Invalid Email!");
            flag = false;
        }
        if (password.length() == 0) {
            password.setError("Password cannot be empty!");
            flag = false;
        } else if (password.length() <= 8) {
            password.setError("Password must contain at least 8 characters!");
            flag = false;
        }
        if (!gender.getText().toString().equals("M") && !gender.getText().toString().equals("F")) {
            gender.setError("Please enter either M or F");
            flag = false;
        }
        if (phoneNo.length() != 11) {
            phoneNo.setError("Phone no must contain exactly 11 characters!");
            flag = false;
        }
        if (_addr.length() == 0) {
            _addr.setError("Address cannot be null");
            flag = false;
        }
        if (_date == null){
            Toast.makeText(c, "DOB Missing", Toast.LENGTH_SHORT).show();
            flag=false;
        }
        else if (_date!=null)
        {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            String date = sdf.format(calendar.getTime());
            java.util.Date currdate1 = null;
            try {
               currdate1 =new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            java.util.Date d = new Date(_date.getTime());

            assert currdate1 != null;
            if(currdate1.compareTo(_date) < 0){

                Toast.makeText(c, "Invalid DOB !", Toast.LENGTH_SHORT).show();
                flag=false;
            }
        }
        return flag;
    }

}
