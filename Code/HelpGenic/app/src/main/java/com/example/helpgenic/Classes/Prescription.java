package com.example.helpgenic.Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Prescription implements Serializable {

    private ArrayList<MedicineDosage> medicines = new ArrayList<>();
    private int days;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
    public void addMedicine(MedicineDosage medicine){
        medicines.add(medicine);
    }
    public ArrayList<MedicineDosage> getMedicines() {
        return medicines;
    }


}
