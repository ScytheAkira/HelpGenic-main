package com.example.helpgenic.Classes;

public class Admin  extends GuestUser {

    public Admin(){
        this.email = null;
        this.name = null;
        this.password = null;
        this.dob=null;
        this.gender = 0;
    }
    public Admin( int id, String email ) {
        this.email = email;
        this.password = null;
        this.dob=null;
        this.gender = 0;
        this.id = id;
    }
    public String getMail(){
        return email;
    }

}



