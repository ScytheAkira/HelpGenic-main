package com.example.helpgenic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.se.omapi.Session;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import java.util.Properties;

public class MyReciever extends BroadcastReceiver {

    MediaPlayer mp;

    private static final  int NOTIFICATION_ID = 100;
    @Override
    public void onReceive(Context context, Intent intent) {
        mp = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mp.setLooping(true);
        mp.start();
        Notification notification;
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            notification = new Notification.Builder( context )

                    .setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
                    .setContentTitle("HelpGenic")
                    .setContentText("Hey! Soft Reminder For Your Appointment Today !")
                    .setChannelId("MyChannel")
                    .setSubText("New Message").build();

            nm.createNotificationChannel(new NotificationChannel("MyChannel" ,"New Channel" , NotificationManager.IMPORTANCE_HIGH));
        }else{
            notification = new Notification.Builder( context )
                    .setSmallIcon(R.drawable.ic_baseline_calendar_today_24)
                    .setContentTitle("HelpGenic")
                    .setContentText("Hey! Soft Reminder")
                    .setSubText("Hurry Up ! Your turn arrived.").build();

        }

        nm.notify(NOTIFICATION_ID,notification);

        



    }





}

