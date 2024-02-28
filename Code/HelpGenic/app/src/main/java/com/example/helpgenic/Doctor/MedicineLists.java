package com.example.helpgenic.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.MedicineDosage;
import com.example.helpgenic.Classes.Prescription;
import com.example.helpgenic.Classes.ReportsHandler;
import com.example.helpgenic.CommonAdapters.ListViewAppointmentDocsAdapter;
import com.example.helpgenic.CommonAdapters.ListViewMedicinesListAdapter;
import com.example.helpgenic.R;

import java.sql.SQLException;

public class MedicineLists extends AppCompatActivity {

    Button addMedicine;
    Prescription pres;
    ListView medicinesList;
    Button submitBtn;
    EditText daysToFollow;
    int aptId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_lists);

        pres = new Prescription();

        addMedicine = findViewById(R.id.addMedicine);
        medicinesList = findViewById(R.id.medicinesList);
        submitBtn = findViewById(R.id.submitForm);
        daysToFollow = findViewById(R.id.daysToFollow);
        aptId = getIntent().getIntExtra("aptId",0);

        addMedicine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MedicineLists.this , PrescriptionForm.class));
            }

        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isValid = true;
                if(daysToFollow.length() == 0){
                    daysToFollow.setError("Please fill this field");
                    isValid = false;
                }
                if (pres.getMedicines().size() == 0){
                    Toast.makeText(MedicineLists.this, "No prescribed medicine", Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                if(isValid) {

                    pres.setDays(Integer.parseInt(daysToFollow.getText().toString()));


                    ReportsHandler rh = new ReportsHandler();
                    DbHandler db = new DbHandler();

                    db.connectToDb(MedicineLists.this);

                    rh.setDb(db);

                    rh.loadPrescription(aptId, pres, MedicineLists.this);

                    try {
                        db.closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putBoolean("isNeedToUpdate", true);
                    myEdit.apply();


                    finish();
                }

            }
        });


    }

    void setUpData(){
        ListViewMedicinesListAdapter adapter = new ListViewMedicinesListAdapter(this, R.layout.list_cell_medicines_list_custom_design, pres.getMedicines());
        medicinesList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        if(sh.getString("medName",null) != null){

            Toast.makeText(this, "Came", Toast.LENGTH_SHORT).show();
            pres.addMedicine(new MedicineDosage(sh.getString("medName",null), sh.getInt("medDosage",0),sh.getBoolean("morning", false),sh.getBoolean("afternoon", false),sh.getBoolean("evening", false) ));

            setUpData();


        }
        SharedPreferences.Editor myEdit = sh.edit();
        myEdit.remove("medName");
        myEdit.remove("medDosage");
        myEdit.remove("morning");
        myEdit.remove("evening");
        myEdit.remove("afternoon");
        myEdit.apply();

    }
}