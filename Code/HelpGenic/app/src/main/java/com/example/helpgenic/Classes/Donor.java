package com.example.helpgenic.Classes;

import java.sql.Date;

public class Donor extends GuestUser {

    String bloodGroup;
    String phNum,address;

    public Donor(String bloodGroup, Date dob, String phNum, String address) {
        this.bloodGroup = bloodGroup;
        this.dob = dob;
        this.phNum = phNum;
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getPhNum() {
        return phNum;
    }

    public String getAddress() {
        return address;
    }

    public Donor(String name, String mail, String password, char gender, java.sql.Date d1, String bloodGroup, String phNum, String address) {
        super(name, mail, password, gender, d1);
        this.bloodGroup = bloodGroup;
        this.phNum = phNum;
        this.address = address;
    }
}
