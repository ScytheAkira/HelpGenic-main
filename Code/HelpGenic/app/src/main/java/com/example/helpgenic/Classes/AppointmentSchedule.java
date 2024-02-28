package com.example.helpgenic.Classes;

import java.io.Serializable;
import java.sql.Time;

public abstract class AppointmentSchedule  implements Serializable {
    protected String day;
    protected Time sTime;
    protected Time eTime;
    protected float fee;
    protected int id;

    public int getId() {
        return id;
    }

    public void setETime(Time eTime) {
        this.eTime = eTime;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setSTime(Time sTime) {
        this.sTime = sTime;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getDay() {
        return day;
    }

    public Time getsTime() {
        return sTime;
    }

    public Time geteTime() {
        return eTime;
    }

    public float getFee() {
        return fee;
    }
}
