package com.example.helpgenic.Classes;

import android.content.Context;
import android.widget.Toast;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Objects;

public class Patient  extends GuestUser{

    private String bloodGroup ;
    private String phNum;

    public  Patient(){}

    public Patient( String name ,String email , String phNum , String bloodGroup , int age, boolean gender){
        this.name = name ;
        this.phNum = phNum;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.age = age;

        if(gender){
            this.gender = 'M';
        }else{
            this.gender = 'F';
        }

    }

    public Patient (String name , String email ,String phoneNumber, String password, Date dob) {
        this.name = name;
        this.email = email;
        this.phNum = phoneNumber;
        this.password= password;
        this.dob=dob;
        this.gender = 0;
        this.bloodGroup=null;
    }

    public Patient (int id , String email , String password,String name,boolean gender, Date dob, String bloodGrup,String phNum) {

        this.email = email;
        this.password = password;
        this.id = id;
        this.dob=dob;
        this.name = name;

        if(gender){
            this.gender = 'M';
        }else{
            this.gender = 'F';
        }

        this.bloodGroup=bloodGrup;
        this.phNum = phNum;
    }

    public Patient(int id , String name ){
        this.name = name;
        this.id = id;
    }

    public int confirmAppointment(int patientId , int docId , Date selectedDate, Slot slot, Context context){
        boolean confirmApp = false;

        if(Objects.equals(slot , null)) {

            Toast.makeText(context, "No Slot selected !", Toast.LENGTH_SHORT).show();

        } else {

            DbHandler db = new DbHandler();
            db.connectToDb(context);

            bm.setDb(db);
            confirmApp = this.bm.isAlreadyHaveAnAppointmentOnDate(selectedDate , patientId , docId ,context);

            if(confirmApp){
                Toast.makeText(context, "You have already got an appointment !", Toast.LENGTH_SHORT).show();
            }else{

                return this.bm.loadAppointment(docId , patientId , selectedDate , slot.sTime , slot.eTime , context);

            }



            try {
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String getPhNum() {
        return phNum;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }
}
