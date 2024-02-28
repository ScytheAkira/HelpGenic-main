package com.example.helpgenic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.ReportsHandler;

import java.sql.SQLException;

public class DisplayImage extends AppCompatActivity {

    int documentId = 0, docId  = 0;
    ImageView iv;
    Button reject , verify;
    DbHandler db = new DbHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        reject=(Button) findViewById(R.id.reject);
        verify=(Button) findViewById(R.id.accept);

        documentId = getIntent().getIntExtra("documentId" ,0);
        docId = getIntent().getIntExtra("docId" ,0);

        iv = findViewById(R.id.document);


        ReportsHandler rh = new ReportsHandler();
        db.connectToDb(DisplayImage.this);

        if(db.isConnectionOpen()){

            if(documentId != 0){
                String bytesData = db.getPatientDocumentsFromDb(documentId, this);
                rh.loadImageFromByteData(iv,bytesData);
            }else if(docId != 0){
                String byteData = db.loadDoctorDegreeImageFromDb(docId,DisplayImage.this);
                rh.loadImageFromByteData(iv,byteData);
            }

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println("The id is " + docId);
                    db.setVerified(DisplayImage.this,docId);
                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putBoolean("NeedToUpdate", true);
                    myEdit.apply();

                    finish(); // return back
                }
           });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("The id is " + docId);
                    db.removeDoctor(DisplayImage.this, docId);

                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putBoolean("NeedToUpdate", true);
                    myEdit.apply();

                    finish(); // return back
                }
            });

        }else{
            Toast.makeText(this, "Connection Error!", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
        if(db.isConnectionOpen()){
            try {
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}