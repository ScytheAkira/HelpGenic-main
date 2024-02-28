package com.example.helpgenic.Patient;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.PatientAdapters.customListViewAdapter;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.util.ArrayList;


public class HistoryWithDoctors extends Fragment {

    DbHandler db = new DbHandler();
    private ArrayList<Doctor> docList ;
    private ListView doctorsList ;
    Patient p;
    public HistoryWithDoctors(Patient p) {

        this.p = p;
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_history_with_doctors,container,false);

        doctorsList = contentView.findViewById(R.id.AppointmentsListView);

        try {
            // populate the data
            setUpData();
            // set adapter to list view
            customListViewAdapter adapter = new customListViewAdapter(getContext() , R.layout.list_cell_custom_design , docList);
            doctorsList.setAdapter(adapter);

        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        // on click

        doctorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // go to the 'Patient Viewing Doc Profile'
                Doctor doc = (Doctor)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), PatientViewingDocProfile.class);
                intent.putExtra("doctor", doc);
                intent.putExtra("patient" , p);
                startActivity(intent);
            }
        });
        return contentView;
    }

    private void setUpData(){

        if(db.connectToDb(getContext())){

            SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            docList = db.getPreviousDoctorsMet(sh.getInt("Id", 0),getContext());

            try {
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }


}