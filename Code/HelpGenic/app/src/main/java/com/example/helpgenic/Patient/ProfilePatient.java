/*------------------------------------------------Fragment-----------------------------------------------*/
package com.example.helpgenic.Patient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.Doctor.DocPage;
import com.example.helpgenic.R;
import com.example.helpgenic.login;

public class ProfilePatient extends Fragment {

    Patient p;

    TextView pName,phNum,email , bloodGroup , dob,id;

    public ProfilePatient(Patient p) {
        // Required empty public constructor
        this.p = p;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_patient, container, false);

        pName = view.findViewById(R.id.pName);
        phNum = view.findViewById(R.id.phNum);
        email = view.findViewById(R.id.email);
        bloodGroup = view.findViewById(R.id.bloodGroup);
        dob = view.findViewById(R.id.dob);
        id = view.findViewById(R.id.patientID);

        pName.setText(p.getName());
        phNum.setText(p.getPhNum());
        email.setText(p.getMail());
        bloodGroup.setText(p.getBloodGroup());
        id.setText("ID: "+ Integer.toString(p.getId()));
        //System.out.println(p.getDob());
        dob.setText(p.getDob().toString());

        Button logOut = view.findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                myEdit.clear();

                myEdit.apply();
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);

                getActivity().finish();
            }
        });
        return view;

    }
}