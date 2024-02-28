package com.example.helpgenic.Classes;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Objects;

public class Doctor extends GuestUser {

    private final String specialization;

    private final boolean isSurgeon;
    private  ArrayList<PhysicalAppointmentSchedule> pSchedule;
    private  ArrayList<VirtualAppointmentSchedule> vSchedule;
    private final String accountNumber;
    private float rating;
    private String meetId;
    private  String degree;
    private ArrayList<Comment> comments = null;

    public Doctor(String name,String specialization,int id){
        this.id = id;
        this.name=name;
        this.specialization=specialization;
        this.isSurgeon=false;
        this.accountNumber=null;
        this.rating=0;
        this.pSchedule=null;
        this.vSchedule=null;
    }

    public Doctor(int id ,String email,String specialization, boolean isSurgeon, String accountNumber , String name  , Character gender , String meetId, float rating) {
        super();

        this.accountNumber = accountNumber;
        this.email = email;
        this.specialization = specialization;
        this.isSurgeon = isSurgeon;
        this.pSchedule=null;
        this.vSchedule=null;
        this.name = name;
        this.gender = gender;
        this.id = id;
        this.rating = rating;
        this.meetId = meetId;

    }
    public Doctor(String email , String password) {
        this.email = email;
        this.password = password;
        this.dob=null;
        this.gender = 0;
        this.specialization=null;
        this.isSurgeon=false;
        this.pSchedule=null;
        this.vSchedule=null;
        this.accountNumber=null;
        this.rating = 0;
    }
    public void setPSch(ArrayList<PhysicalAppointmentSchedule> pList){
        this.pSchedule = pList;
    }

    public void setVSch(ArrayList<VirtualAppointmentSchedule> vList){
        this.vSchedule = vList;
    }

    public void setVSch(String day, Time sTime , Time eTime){
        if(Objects.equals(vSchedule ,null)){

            vSchedule = new  ArrayList<VirtualAppointmentSchedule>();
        }
        vSchedule.add(new VirtualAppointmentSchedule(day,sTime,eTime,0));
    }


    public String getSpecialization() {
        return specialization;
    }

    public float getRating() {
        return rating;
    }

    public boolean isSurgeon() {
        return isSurgeon;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public ArrayList<VirtualAppointmentSchedule> getvSchedule() {
        return vSchedule;
    }

    public Doctor(String specialization, boolean isSurgeon,String accountNumber,String degree){
        this.specialization=specialization;
        this.isSurgeon=isSurgeon;
        this.accountNumber=accountNumber;
        this.degree=degree;
        this.rating=0;
        this.pSchedule=null;
        this.vSchedule=null;
    }

    String getDegree(){
        return degree;
    }

    void setComment(String pname , String comment){
        if(Objects.equals(comments ,null)){
            comments = new ArrayList<>();
        }
        comments.add(new Comment(pname,comment));
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }


    //    public static Comparator<Doctor> ratingDescending = new Comparator<Doctor>() {
//        @Override
//        public int compare(Doctor doctor, Doctor t1) {
//
//            float r1 = doctor.getRating();
//            float r2 = t1.getRating();
//            return Float.compare(r2, r1);
//        }
//    };

//    public static Comparator<Doctor> ratingAscending = new Comparator<Doctor>() {
//        @Override
//        public int compare(Doctor doctor, Doctor t1) {
//
//            int r1 = doctor.get;
//            float r2 = t1.getRating();
//            return Float.compare(r1, r2);
//        }
//    };


}
