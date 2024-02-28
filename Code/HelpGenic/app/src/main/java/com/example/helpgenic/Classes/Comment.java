package com.example.helpgenic.Classes;

import java.io.Serializable;

public class Comment implements Serializable {
    private String patientName, description;

    public Comment(String patientName, String description) {
        this.patientName = patientName;
        this.description = description;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDescription() {
        return description;
    }
}
