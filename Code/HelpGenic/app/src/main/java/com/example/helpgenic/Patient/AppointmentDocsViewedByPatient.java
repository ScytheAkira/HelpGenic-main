package com.example.helpgenic.Patient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Document;
import com.example.helpgenic.Classes.Prescription;
import com.example.helpgenic.Classes.ReportsHandler;
import com.example.helpgenic.CommonAdapters.ListViewAppointmentDocsAdapter;
import com.example.helpgenic.CommonAdapters.ListViewAppointmentDocsAdapter2;
import com.example.helpgenic.DisplayImage;
import com.example.helpgenic.DisplayImage2;
import com.example.helpgenic.DisplayPrescription;
import com.example.helpgenic.Doctor.AppointmentDocsViewedByDoc;
import com.example.helpgenic.Doctor.SignUpDoc;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentDocsViewedByPatient extends AppCompatActivity {

    int aptId = 0;
    ArrayList<Prescription> prescriptions = null;
    ReportsHandler rh = new ReportsHandler();
    Button addDocument;

    // Initialize variable
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_docs_viewed_by_patient);


        aptId = getIntent().getIntExtra("aptId",0);

        addDocument = findViewById(R.id.addDocument);

        Toast.makeText(this, Integer.toString(aptId), Toast.LENGTH_SHORT).show();

        DbHandler db = new DbHandler();
        db.connectToDb(AppointmentDocsViewedByPatient.this);
        rh.setDb(db);


        // ======================================= upload document =======================================

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result)
            {
                // Initialize result data
                Intent data = result.getData();
                // check condition
                if (data != null) {

                    Uri selectedImageUri = data.getData();
                    // get bytes data from image and save it to database along with other doctor credentials

                    String encodedImage = rh.loadImage(selectedImageUri , AppointmentDocsViewedByPatient.this);

                    if(!db.isConnectionOpen()){
                        db.connectToDb(AppointmentDocsViewedByPatient.this);
                    }
                    db.loadDocumentToDb(aptId , encodedImage, AppointmentDocsViewedByPatient.this);



                }
            }

        } );


        addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check condition
                if (ActivityCompat.checkSelfPermission(AppointmentDocsViewedByPatient.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    // When permission is not granted
                    // Result permission
                    ActivityCompat.requestPermissions(AppointmentDocsViewedByPatient.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }
                else {
                    // When permission is granted
                    // Create method
                    selectImage();
                }
            }
        });


        // ==============================================================================================================

        ListView listview = findViewById(R.id.listView1);

        ArrayList<Document> documents = rh.getDocuments(aptId,AppointmentDocsViewedByPatient.this);
        ListViewAppointmentDocsAdapter2 adapter = new ListViewAppointmentDocsAdapter2(this, R.layout.list_cell_custom_design_appointment_docs_1, documents);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Document document = (Document)adapterView.getItemAtPosition(i);
                Intent intent =new Intent(AppointmentDocsViewedByPatient.this , DisplayImage2.class);
                intent.putExtra("documentId" , document.getDocumentId());
                startActivity(intent);
            }
        });



        // display prescription and view it
        ListView listview2 = findViewById(R.id.listView2);
        prescriptions = rh.setUpPrescriptions(aptId,AppointmentDocsViewedByPatient.this);

        if(prescriptions != null && prescriptions.size() == 1){
            ListViewAppointmentDocsAdapter adapter2 = new ListViewAppointmentDocsAdapter(this, R.layout.list_cell_custom_design_appointment_docs_2 , prescriptions);
            listview2.setAdapter(adapter2);
        }

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Prescription pres = (Prescription)adapterView.getItemAtPosition(i);

                Intent intent = new Intent(AppointmentDocsViewedByPatient.this , DisplayPrescription.class);
                intent.putExtra("pres", pres);
                startActivity(intent);
            }
        });


        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }



    private void selectImage()
    {
        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // set type
        intent.setType("image/*");
        // Launch intent
        resultLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

        // check condition
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            selectImage();
        }
        else {
            // When permission is denied
            // Display toast
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


}