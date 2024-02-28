package com.example.helpgenic.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.CommonAdapters.ListViewAppointmentsAdapter;
import com.example.helpgenic.CommonAdapters.ListViewMedicinesListAdapter;
import com.example.helpgenic.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ReportsHandler {

    DbHandler db;

    public void setDb(DbHandler db) {
        this.db = db;
    }

    public String loadImage(Uri selectedImageUri, Context context){

        Bitmap selectedImageBitmap = null;

        try {
            selectedImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);

        byte[] bytesImage = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(bytesImage, Base64.DEFAULT);

        return encodedImage;

    }

    public void loadImageFromByteData(ImageView iv ,String bytes){

        byte[] decodeString = Base64.decode(bytes, Base64.DEFAULT);
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        iv.setImageBitmap(decodebitmap);

    }



    public  ArrayList<Boolean> checkFields(EditText medName , EditText medDosgae , CheckBox morning,CheckBox evening , CheckBox afternoon , Context context){

        ArrayList<Boolean> checkedBoxes =  new ArrayList<>();
        checkedBoxes.add(false);
        checkedBoxes.add(false);
        checkedBoxes.add(false);


        if(Objects.equals(medName.length()  ,0)){
            medName.setError("This field can't be empty");
            return null;
        }
        if(Objects.equals(medDosgae.length()  ,0)){
            medDosgae.setError("This field can't be empty");
            return null;
        }
        boolean checked = false;

        if(morning.isChecked()){
            checked =  true;
            checkedBoxes.set(0,true);
        }
        if(evening.isChecked()){
            checked =  true;
            checkedBoxes.set(1,true);
        }
        if(afternoon.isChecked()){
            checked =  true;
            checkedBoxes.set(2,true);
        }

        if(!checked){
            Toast.makeText(context, "At least one box should be selected", Toast.LENGTH_SHORT).show();
            return null;
        }

        return checkedBoxes;
    }


    public void loadPrescription(int aptId ,Prescription p , Context context){
        db.loadPrescriptionToDb(aptId,p,context);
    }

    public  ArrayList<Prescription> setUpPrescriptions(int aptId , Context context){

        return db.getPrescriptionInfo(aptId,context);
    }

    public void displayPrescription(ListView lst , Prescription p , TextView days, Context context){
        ListViewMedicinesListAdapter adapter = new ListViewMedicinesListAdapter(context, R.layout.list_cell_medicines_list_custom_design, p.getMedicines());
        lst.setAdapter(adapter);

        days.setText(Integer.toString(p.getDays()) + " Days");
    }

    public void displayPreviousAppointments(int docId , int patientId , Context context, ListView lst){

        try {

            // getting previous Appointments
            ArrayList<Appointment> prevAppointments = null;
            prevAppointments = db.getPreviousAppointmentInfo(docId, patientId, context);


            ListViewAppointmentsAdapter adapter = new ListViewAppointmentsAdapter(context, R.layout.appointments_list_custom_design, prevAppointments);
            lst.setAdapter(adapter);

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Document> getDocuments(int aptId , Context context){
        return db.getDocumentsFromDb(aptId,context);

    }




}
