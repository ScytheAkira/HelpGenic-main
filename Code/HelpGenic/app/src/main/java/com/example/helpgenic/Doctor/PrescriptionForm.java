package com.example.helpgenic.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.ReportsHandler;
import com.example.helpgenic.R;

import java.util.ArrayList;

public class PrescriptionForm extends AppCompatActivity {


    EditText medName, medDosage;
    Button add;
    CheckBox morning;
    CheckBox afternoon;
    CheckBox evening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_form);

        medName = findViewById(R.id.medName);
        medDosage =  findViewById(R.id.med_dosage);
        add = findViewById(R.id.submitMed);
        morning = findViewById(R.id.morningCheck);
        afternoon = findViewById(R.id.afternoonCheck);
        evening = findViewById(R.id.eveningCheck);


        ReportsHandler rh = new ReportsHandler();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<Boolean>  checkedBoxes = rh.checkFields(medName , medDosage , morning,afternoon,evening,PrescriptionForm.this);

                if(checkedBoxes != null){
                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putString("medName" , medName.getText().toString());
                    myEdit.putInt("medDosage",Integer.parseInt(medDosage.getText().toString()));
                    myEdit.putBoolean("morning" , checkedBoxes.get(0));
                    myEdit.putBoolean("afternoon" , checkedBoxes.get(1));
                    myEdit.putBoolean("evening" , checkedBoxes.get(2));
                    myEdit.apply();
                    Toast.makeText(PrescriptionForm.this, sh.getString("medName",null), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }
}