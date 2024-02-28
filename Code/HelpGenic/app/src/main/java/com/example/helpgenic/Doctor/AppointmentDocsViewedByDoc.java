package com.example.helpgenic.Doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Document;
import com.example.helpgenic.Classes.Prescription;
import com.example.helpgenic.Classes.ReportsHandler;
import com.example.helpgenic.CommonAdapters.ListViewAppointmentDocsAdapter;
import com.example.helpgenic.CommonAdapters.ListViewAppointmentDocsAdapter2;
import com.example.helpgenic.DisplayImage;
import com.example.helpgenic.DisplayImage2;
import com.example.helpgenic.DisplayPrescription;
import com.example.helpgenic.Patient.AppointmentDocsViewedByPatient;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.util.ArrayList;


public class AppointmentDocsViewedByDoc extends AppCompatActivity {


    Button addPres;
    int aptId = 0;
    ArrayList<Prescription> prescriptions = null;
    ReportsHandler rh = new ReportsHandler();
    DbHandler db = new DbHandler();
    ListView listview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_docs_viewed_by_doc);


        addPres = findViewById(R.id.addPres);
        aptId = getIntent().getIntExtra("aptId",0);

        Toast.makeText(this, Integer.toString(aptId), Toast.LENGTH_SHORT).show();

        db.connectToDb(AppointmentDocsViewedByDoc.this);
        rh.setDb(db);


        // ========================================== View Document ==========================================
        ListView listview = findViewById(R.id.listView1);

        ArrayList<Document> documents = rh.getDocuments(aptId, AppointmentDocsViewedByDoc.this);
        ListViewAppointmentDocsAdapter2 adapter = new ListViewAppointmentDocsAdapter2(this, R.layout.list_cell_custom_design_appointment_docs_1, documents);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Document document = (Document)adapterView.getItemAtPosition(i);
                Intent intent =new Intent(AppointmentDocsViewedByDoc.this , DisplayImage2.class);
                intent.putExtra("documentId" , document.getDocumentId());
                startActivity(intent);
            }
        });

        // =====================================================================================================


        listview2 = findViewById(R.id.listView2);
        prescriptions = rh.setUpPrescriptions(aptId,AppointmentDocsViewedByDoc.this);

        if(prescriptions != null && prescriptions.size() == 1){
            ListViewAppointmentDocsAdapter adapter2 = new ListViewAppointmentDocsAdapter(this, R.layout.list_cell_custom_design_appointment_docs_2 , prescriptions);
            listview2.setAdapter(adapter2);
        }

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Prescription pres = (Prescription)adapterView.getItemAtPosition(i);

                Intent intent = new Intent(AppointmentDocsViewedByDoc.this , DisplayPrescription.class);
                intent.putExtra("pres", pres);
                startActivity(intent);
            }
        });


        addPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(prescriptions == null){
                    Intent intent = new Intent(AppointmentDocsViewedByDoc.this , MedicineLists.class);
                    intent.putExtra("aptId", aptId);
                    startActivity(intent);
                }else{
                    Toast.makeText(AppointmentDocsViewedByDoc.this, "Already Added", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        if(sh.getBoolean("isNeedToUpdate", false)){

            prescriptions = rh.setUpPrescriptions(aptId,AppointmentDocsViewedByDoc.this);

            ListViewAppointmentDocsAdapter adapter2 = new ListViewAppointmentDocsAdapter(this, R.layout.list_cell_custom_design_appointment_docs_2 , prescriptions);
            listview2.setAdapter(adapter2);

            SharedPreferences.Editor myEdit = sh.edit();
            myEdit.remove("isNeedToUpdate");
            myEdit.apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}