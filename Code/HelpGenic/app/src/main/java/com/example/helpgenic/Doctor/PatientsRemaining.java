/*------------------------------------------------Fragment-----------------------------------------------*/
package com.example.helpgenic.Doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.DoctorAdapters.ListViewPatientsRemainingAdapter;
import com.example.helpgenic.Patient.JoinMeeting;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.util.ArrayList;


public class PatientsRemaining extends Fragment {

    private ArrayList<Appointment> appointments = null;
    Doctor d;
    Button callPatient;

    public PatientsRemaining(Doctor d) {
        // Required empty public constructor
        this.d = d;
    }

    private void setUpData() {
        DbHandler db = new DbHandler();
        db.connectToDb(getContext());

        appointments = db.getUpcommingAppointmentsForDoctor(d.getId() , getContext());

        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patients_remaining, container, false);

        callPatient = view.findViewById(R.id.callPatient);
        // populate the data
        setUpData();
        // set adapter to list view
        ListView appointmentListRef = view.findViewById(R.id.patientsRem);
        ListViewPatientsRemainingAdapter adapter = new ListViewPatientsRemainingAdapter(getContext() , R.layout.list_cell_custom_design_patients_remaining, appointments);
        appointmentListRef.setAdapter(adapter);



        appointmentListRef.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Appointment p = (Appointment) adapterView.getItemAtPosition(i);


                //System.out.println(p);

                Intent intent = new Intent(new Intent(getContext(), DocViewingPatientProfile.class));
                intent.putExtra("patientID" , p.getP().getId());
                intent.putExtra("docID" ,d.getId());
                startActivity(intent);
            }
        });


        callPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), JoinMeeting.class));
            }
        });

        return view;
    }


}