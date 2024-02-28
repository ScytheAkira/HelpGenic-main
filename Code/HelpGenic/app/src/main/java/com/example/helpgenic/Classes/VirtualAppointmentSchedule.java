package com.example.helpgenic.Classes;

import java.sql.Time;

public class VirtualAppointmentSchedule extends AppointmentSchedule{

    public VirtualAppointmentSchedule( int id,String day, Time sTime,Time eTime,float fee) {

        this.day = day;
        this.sTime = sTime;
        this.eTime = eTime;
        this.fee = fee;
        this.id = id;

    }
    public VirtualAppointmentSchedule( String day, Time sTime,Time eTime,float fee) {

        this.day = day;
        this.sTime = sTime;
        this.eTime = eTime;
        this.fee = fee;

    }


}
