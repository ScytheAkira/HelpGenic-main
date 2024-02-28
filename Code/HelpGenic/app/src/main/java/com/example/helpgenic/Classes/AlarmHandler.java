package com.example.helpgenic.Classes;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.helpgenic.MyReciever;
import android.app.AlarmManager;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmHandler {

    public void setAlarm(int uniqueId , Date dateSelected , Time sTime, Context context, AlarmManager alarmManager){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString = dateSelected.toString() + " " + sTime.toString();


        // formatting the dateString to convert it into a Date
        java.util.Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        //Setting the Calendar date and time to the given date and time
        calendar.setTime(date);


        // setting alarm

        long triggerTime = calendar.getTimeInMillis();

        Intent iBroadCast = new Intent (context, MyReciever.class);
        PendingIntent pi = PendingIntent.getBroadcast(context , uniqueId ,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP,triggerTime,pi);

    }

    public void cancelAlarm(int uniqueId, Context context , AlarmManager alarmManager) {

        Intent iBroadCast = new Intent (context, MyReciever.class);
        PendingIntent pi = PendingIntent.getBroadcast(context , uniqueId ,iBroadCast,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }


}
