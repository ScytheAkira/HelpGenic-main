/*------------------------------------------------Fragment-----------------------------------------------*/
package com.example.helpgenic.Doctor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.DoctorAdapters.ListViewPatientAttendedAdapter;
import com.example.helpgenic.DoctorAdapters.ListViewPatientsRemainingAdapter;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.util.ArrayList;


public class PatientsAttended extends Fragment {

    private ArrayList<Patient> prevPatients = new ArrayList<>();
    Doctor d;
    EditText sView;

    public PatientsAttended(Doctor d) {
        // Required empty public constructor
        this.d = d;
    }


    private void setUpData() {

        DbHandler db = new DbHandler();
        db.connectToDb(getContext());

        prevPatients = db.getPreviousPatientsAttended(d.getId() , getContext());

        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patients_attended, container, false);

        sView = view.findViewById(R.id.searchView);
        // set adapter to list view
        ListView appointmentListRef = view.findViewById(R.id.patients);
        // populate the data
        setUpData();

        ListViewPatientAttendedAdapter adapter = new ListViewPatientAttendedAdapter(getContext() , R.layout.list_cell_custom_design_patients_attended, prevPatients);
        appointmentListRef.setAdapter(adapter);


        // go to patient profile on row click
        appointmentListRef.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Patient p = (Patient) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(new Intent(getContext(), DocViewingPatientProfile.class));
                intent.putExtra("patientID" , p.getId());
                intent.putExtra("docID" ,d.getId());
                startActivity(intent);
            }
        });



        sView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // leave it
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filter list
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // leave it
            }
        });



        // Inflate the layout for this fragment
        return view;
    }
}