package com.example.helpgenic.Classes;
import android.content.Context;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.login;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;


public class GuestUser extends User{


    public GuestUser(){};
    public GuestUser(String name, String mail, String password, char gender, Date d1) {
        super();
        this.name = name;
        this.email = mail;
        this.password = password;
        this.gender = gender;
        this.dob = d1;
    }

    public GuestUser logIn(EditText email , EditText password , Context context ){


        DbHandler db = new DbHandler();


        this.au.setDb(db);
        GuestUser obj =  this.au.validateCredentials(email,password,context);


        return obj;

    }

    public GuestUser SignUpPatient (EditText name , EditText email, EditText phoneNumber , EditText password1, EditText password2 , EditText gender , Date dob, AutoCompleteTextView bloodGroup, Context context )
    {


        GuestUser obj = this.ah.validatePatientCredentials(name,email,phoneNumber,gender,password1, password2,dob,bloodGroup,context);


        return obj;
    }

}
