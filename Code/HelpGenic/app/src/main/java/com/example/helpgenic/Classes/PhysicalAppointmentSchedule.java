package com.example.helpgenic.Classes;

import java.sql.Time;

public class PhysicalAppointmentSchedule extends AppointmentSchedule{

    private final String clinicName;
    private  String assistantPhNum;

    public String getClinicName() {
        return clinicName;
    }


    private  double lattitude;
    private  double longitude;
    private String docName;
    private double latts;
    private double longs;





    public String getAssistantPhNum() {
        return assistantPhNum;
    }

    public double getLattitude() {
        return lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDocName() {
        return docName;
    }
    public double getLongs() {
        return longs;
    }

    public double getLatts() {
        return latts;
    }


    public PhysicalAppointmentSchedule(String clinicName, String assistantPhNum , String day, Time sTime, Time eTime, float fee) {

        this.clinicName = clinicName;
        this.assistantPhNum = assistantPhNum;
        this.day = day;
        this.sTime = sTime;
        this.eTime = eTime;
        this.fee = fee;
    }
    public PhysicalAppointmentSchedule(String clinicName, Double lattitude , Double longitude , String docName){
        this.clinicName = clinicName;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.docName = docName;

    }


    public PhysicalAppointmentSchedule(String clinicName, Double lattitude , Double longitude, String contact, String day, Time sTime, Time eTime, float fee){
        this.clinicName=clinicName;
        this.latts=lattitude;
        this.longs=longitude;
        this.assistantPhNum = contact;
        this.day=day;
        this.sTime=sTime;
        this.eTime=eTime;
        this.fee=fee;
    }
}
