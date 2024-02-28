package com.example.helpgenic.Patient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.util.Objects;


public class GiveFeedback extends AppCompatActivity {

    EditText comment;
    RatingBar rb;
    TextView tv;
    Button submitBtn, cancelButton;
    float rating = 0.0f ;
    int patientId = 0;
    int doctorId  = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);

        doctorId = getIntent().getIntExtra("docId",0);
        SharedPreferences shrd =  this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shrd.edit();
        patientId  = shrd.getInt("Id",0);
        doctorId  = shrd.getInt("dId",0);
        myEdit.remove("dId");
        myEdit.apply();

        System.out.println(doctorId);
        System.out.println(patientId);

        Toast.makeText(this, Integer.toString(doctorId) + " " +Integer.toString(patientId) , Toast.LENGTH_SHORT).show();

        comment=findViewById(R.id.editTextTextMultiLine);
        rb=findViewById(R.id.ratingBar);
        tv=findViewById(R.id.textView13);
        submitBtn=findViewById(R.id.button10);
        cancelButton = findViewById(R.id.cancel_Btn);



        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                tv.setText("Rating: " +v);
                rating = v;

            }
        } );


        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Objects.equals(validate() , 1))
                {
                    DbHandler dbHandler = new DbHandler();
                    dbHandler.connectToDb(GiveFeedback.this);

                    if(comment.length() != 0){
                        dbHandler.InsertComment(patientId, doctorId,comment.getText().toString(),GiveFeedback.this);
                    }
                    if(rating != 0){
                        dbHandler.updateRating( doctorId ,  rating , GiveFeedback.this);
                    }
                    finish();

                    try {
                        dbHandler.closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(GiveFeedback.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private int validate()
    {


        if(comment.length() != 0 || rating != 0.0){
            return 1;
        }

        return 0;

    }




}

