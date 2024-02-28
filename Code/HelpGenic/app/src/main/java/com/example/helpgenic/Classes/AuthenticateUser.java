package com.example.helpgenic.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class AuthenticateUser {

    DbHandler db;

    public void setDb(DbHandler db) {
        this.db = db;
    }

    public GuestUser validateCredentials(EditText email , EditText password , Context context ){


        if (email.length() == 0) {
            email.setError("This field is required");
            return null;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Invalid Email!");
            return null;
        }
        if (password.length() == 0) {
            password.setError("This field is required");
            return null;
        }




        // create connection
        if (!db.isConnectionOpen()) {

            // =============== connect with db ===============
            db.connectToDb(context);
            // ===============================================

        }

        // getting user Id and his type
        List<Object> obj = db.matchCredentials(email.getText().toString() , password.getText().toString(), context);

        if(obj != null) {



            String type = (String) obj.get(0);
            int id = (int) obj.get(1);


            // differentiating on the basis of their type

            if (Objects.equals(type, "A")) {

                // if usr is admin


                addToSharedPrefForAdmin(id, type, email.getText().toString(), context);
                return new Admin(id, email.getText().toString());


            }
            else if (Objects.equals(type, "P")) {
                // if user is patient


                List<Object> patientDetails = db.getPatientDetails(id, context);  // get that patient details

                // save them to preferences
                addToSharedPrefForPatient(id, type, email.getText().toString(), (String) patientDetails.get(0), (boolean) patientDetails.get(1), (Date) patientDetails.get(2), (String) patientDetails.get(3), (String) patientDetails.get(4), context);
                // return the corresponding Patient object with these info.
                return new Patient(id, email.getText().toString(), password.getText().toString(), (String) patientDetails.get(0), (boolean) patientDetails.get(1), Date.valueOf(((Date) patientDetails.get(2)).toString()), (String) patientDetails.get(3), (String) patientDetails.get(4));

            }
            else if (Objects.equals(type, "D")) {

                // if usr is Doctor

                List<Object> doctorDetails = db.getDoctorDetails(id, context);  // get that doctor details

                // save them to preferences
                addToSharedPrefForDoctor(id, type, email.getText().toString(), (String) doctorDetails.get(0), (boolean) doctorDetails.get(1), (Date) doctorDetails.get(2), (String) doctorDetails.get(3), (boolean) doctorDetails.get(4), (String) doctorDetails.get(5), (float) doctorDetails.get(6), context);
                // return the corresponding Object object with these info.

                char c = 'F';
                if ((boolean) doctorDetails.get(1)) {
                    c = 'M';
                }

                return new Doctor(id, email.getText().toString(), (String) doctorDetails.get(3), (boolean) doctorDetails.get(4), (String) doctorDetails.get(5), (String) doctorDetails.get(0), c, null, (float) doctorDetails.get(6));


            }else if(Objects.equals(type, "d")){
                Toast.makeText(context, "Sorry! you have no access to login", Toast.LENGTH_SHORT).show();
            }
            else if (Objects.equals(type, "N")) {
                // if not found , i.e not verified
                Toast.makeText(context, "Credentials are not correct !", Toast.LENGTH_SHORT).show();
            }

        }

        // closing connection
        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;



    }


    void addToSharedPrefForPatient(int id , String type, String email ,String name , boolean gender, Date dob, String bloodGrup,String phNum,Context context) {

        SharedPreferences shrd =  context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shrd.edit();

        myEdit.putString("type",type);
        myEdit.putInt("Id" , id);
        myEdit.putString("email", email);
        myEdit.putString("name" , name);
        myEdit.putString("dob" , dob.toString());
        myEdit.putBoolean("gender" , gender);
        myEdit.putString("bloodGroup" , bloodGrup);
        myEdit.putString("phNum" , phNum);
        myEdit.apply();


    }

    void addToSharedPrefForDoctor(int id , String type, String email ,String name , boolean gender, Date dob, String specialization , boolean isSurgeon , String accNum ,float rating, Context context){

        SharedPreferences shrd =  context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shrd.edit();

        myEdit.putString("type",type);
        myEdit.putInt("Id" , id);
        myEdit.putString("email", email);
        myEdit.putString("name" , name);
        myEdit.putString("dob" , dob.toString());
        myEdit.putBoolean("gender" , gender);
        myEdit.putString("specialization" , specialization);
        myEdit.putBoolean("isSurgeon" , isSurgeon);
        myEdit.putString("accNum", accNum);
        myEdit.putFloat("rating", rating);
        myEdit.apply();
    }

    void addToSharedPrefForAdmin(int id , String type, String email, Context context){

        SharedPreferences shrd =  context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shrd.edit();

        myEdit.putString("type",type);
        myEdit.putInt("Id" , id);
        myEdit.putString("email", email);
        myEdit.apply();
    }

}
