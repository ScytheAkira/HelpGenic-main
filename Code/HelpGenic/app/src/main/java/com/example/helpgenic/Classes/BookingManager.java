package com.example.helpgenic.Classes;

import android.content.Context;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Objects;

public class BookingManager {

    DbHandler db;

    public void setDb(DbHandler db) {
        this.db = db;
    }

    public ArrayList<VirtualAppointmentSchedule> getDoctorVirtualSchedule(int docId , Context context)  {
        ArrayList<VirtualAppointmentSchedule> obj =  db.getDoctorVirtualAppDetails(docId,context);
        return obj;
    }

    public ArrayList<Slot> getAvailableSlots(int docId , Date dateSelected , String day , ArrayList<Slot> slots,Context context){

        ArrayList<Slot> consumedSlots = db.getConsumedSlots(docId , dateSelected, context);

        ArrayList<Slot> availableSlots = new ArrayList<>();

        boolean isPresent, onSameDay = false;


        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);

        Time t = new Time(date.getTime());


        if(Objects.equals(dateSelected.toString(),date.toString())){
            Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();
            onSameDay = true;
        }

        for (Slot slot: slots ){

            isPresent = false;

            if(onSameDay){
                if( this.compareTo(slot.sTime,t) <= 0){
                    continue;
                }
            }

            for (Slot cSlot: consumedSlots) {


                if (Objects.equals(slot.sTime , cSlot.sTime) && Objects.equals(slot.eTime ,cSlot.eTime) ){
                    isPresent = true;
                    break;
                }

            }
            if(!isPresent) {

                slot.day = day;
                availableSlots.add(slot);

            }


        }
        return availableSlots;
    }

    public ArrayList<Slot> makeSlots( Time sTime , Time eTime , String day){

        return func(sTime , eTime , day);
    }

    private Time add30ToTime(String time){
        int mm = Integer.parseInt(time.substring(3,5));
        int hh = Integer.parseInt(time.substring(0,2));


        mm += 30;
        if (mm == 60){
            mm = 0;
            hh += 1;
            if (hh == 24){
                hh = 0;
            }

        }
        Time t = Time.valueOf(hh +":"+ mm+":" +"00");
        return t;
    }

    private   ArrayList<Slot> func(Time t1, Time t2 , String day) {

        ArrayList<Slot> slots = new ArrayList<>();
        Time temp;

        while(true){
            temp = add30ToTime(t1.toString());
            //System.out.println(t1.toString() + "  " + temp.toString());
            slots.add(new Slot(t1,temp , day));
            t1 = temp;
            if(Objects.equals(t1,t2)){
                break;
            }
        }


        return slots;
    }

    private int compareTo (Time t1 , Time t2) {

        int mm1 =  Integer.parseInt(t1.toString().substring(3,5));
        int hh1 = Integer.parseInt(t1.toString().substring(0,2));
        int mm2 =  Integer.parseInt(t2.toString().substring(3,5));
        int hh2 = Integer.parseInt(t2.toString().substring(0,2));

        if(hh1 > hh2){
            return 1;
        }else if (hh1 < hh2){
            return -1;

        }else {

            if(mm1 > mm2){
                return 1;
            }else if (mm1 < mm2){
                return -1;
            }else {
                return 0;
            }
        }



    }


    public boolean isAlreadyHaveAnAppointmentOnDate(Date dateSelected ,int pId , int docId , Context context){
        return this.db.checkDuplicateAppointment(docId , pId , dateSelected,context);
    }

    public int loadAppointment(int docId , int patientId , Date dateSelected ,Time sTime , Time eTime, Context context){

         return this.db.loadAppointmentToDb(docId , patientId , dateSelected,sTime,eTime,context);
    }




}
