package com.example.helpgenic.Classes;

import java.sql.Time;

public class Slot {
    public Time sTime;

    public Time eTime;
    public String day;


    public Slot(Time sTime, Time eTime , String day) {
        this.sTime = sTime;
        this.eTime = eTime;
        this.day = day;

    }
}
