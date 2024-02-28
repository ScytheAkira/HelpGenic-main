package com.example.helpgenic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.helpgenic.Classes.Prescription;
import com.example.helpgenic.Classes.ReportsHandler;

public class DisplayPrescription extends AppCompatActivity {

    ListView medList;
    TextView daysToFollow;
    Prescription pres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_prescriptiom);

        medList = findViewById(R.id.medList);
        daysToFollow = findViewById(R.id.days);

        pres = (Prescription) getIntent().getSerializableExtra("pres");

        ReportsHandler rh = new ReportsHandler();
        rh.displayPrescription(medList , pres  ,daysToFollow , this);




    }
}