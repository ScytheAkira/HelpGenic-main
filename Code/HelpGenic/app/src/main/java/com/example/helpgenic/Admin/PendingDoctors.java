package com.example.helpgenic.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helpgenic.AdminAdapters.CustomAdapterVerifyDoc;
import com.example.helpgenic.Classes.Admin;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.DisplayImage;
import com.example.helpgenic.R;
import com.example.helpgenic.login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class PendingDoctors extends Fragment {

    Button logOutBtn;

    ArrayList<Doctor> docList = null;

    ListView doctorsList;
    DbHandler dbHandler = new DbHandler();
    CustomAdapterVerifyDoc adapter1 = null;

    // Helper Functions
    private ArrayList<Doctor> setUpData(){

        ArrayList<Doctor> docList =new ArrayList<>();
        int id=0;
        String name,specialization;
        ResultSet rs = dbHandler.getUnVerifiedDocs(getContext()); // get all unverified doctors

        try {
            while (rs.next()) {
                id=rs.getInt("uid");
                name=rs.getString("name");
                specialization=rs.getString("specialization");
                docList.add(new Doctor(name,specialization,id));
            }
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return docList;
    }

    public PendingDoctors(Admin admin) {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_doctors, container, false);;

        if(!dbHandler.isConnectionOpen()){
            dbHandler.connectToDb(getContext());
        }


        logOutBtn = view.findViewById(R.id.logOutBtn);
        logOutBtn = view.findViewById(R.id.logOutBtn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                myEdit.clear();

                myEdit.apply();
                Intent intent = new Intent(getContext() ,login.class);
                startActivity(intent);

                getActivity().finish();
            }
        });


        doctorsList =view.findViewById(R.id.verifyList);

        docList = setUpData();
        adapter1 = new CustomAdapterVerifyDoc(getContext() , R.layout.list_cell_custom_verify_doctors_design , docList);
        doctorsList.setAdapter(adapter1);

        doctorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Doctor d = (Doctor) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext() , DisplayImage.class);
                intent.putExtra("docId" , d.getId());
                startActivity(intent);
            }
        });






        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


        SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();

        if(sh.getBoolean("NeedToUpdate", false)){
            // setting physical schedule data

            Toast.makeText(getContext(), "Resumed", Toast.LENGTH_SHORT).show();
            docList = setUpData();
            adapter1 = new CustomAdapterVerifyDoc(getContext() , R.layout.list_cell_custom_verify_doctors_design , docList);
            doctorsList.setAdapter(adapter1);
        }
        myEdit.remove("NeedToUpdate");
        myEdit.apply();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(dbHandler.isConnectionOpen()){
            try {
                dbHandler.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}