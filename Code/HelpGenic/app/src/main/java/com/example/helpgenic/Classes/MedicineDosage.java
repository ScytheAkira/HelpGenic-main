package com.example.helpgenic.Classes;

import java.io.Serializable;

public class MedicineDosage implements Serializable {

    private final String medName;
    private final int medDosage;
    private final boolean morning;
    private final boolean afternoon;
    private final boolean evening;



    public MedicineDosage(String medName, int medDosgae, boolean morning, boolean afternoon, boolean evening) {
        this.medName = medName;
        this.medDosage = medDosgae;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
    }

    public String getMedicineName() {
        return medName;
    }

    public int getMedicineDosage() {
        return medDosage;
    }

    public boolean isMorning() {
        return morning;
    }

    public boolean isAfternoon() {
        return afternoon;
    }

    public boolean isEvening() {
        return evening;
    }
}

