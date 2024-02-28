package com.example.helpgenic.Patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Donor;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.PatientAdapters.ListViewDonorSearchAdapter;
import com.example.helpgenic.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;


public class DonorSearch extends Fragment {

    AutoCompleteTextView bloodGroups;
    String[] bloodGrps = {"A+" , "A-" , "B+","B-","AB+","AB-","O+","O-" };
    ListView donorsList;
    ArrayList<Donor> donors;
    ListViewDonorSearchAdapter adapter1;


    Patient p;
    public DonorSearch(Patient p) {
        // Required empty public constructor
        this.p = p;
    }
    DbHandler db =  new DbHandler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_donor_search, container, false);
        db.connectToDb(getContext());
        // ======================================== Handling Sort by functionality ==========================================
        bloodGroups = view.findViewById(R.id.bloodGroups);    // 'sort by' spinner option
        donorsList = view.findViewById(R.id.donorsList);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_spinner_dropdown_item , bloodGrps);
        bloodGroups.setAdapter(adapter);

        // when user presses the autocomplete text view 'sort by' option
        bloodGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = adapterView.getItemAtPosition(i).toString();
                donors.removeIf(d -> !Objects.equals(d.getBloodGroup(), s));
                adapter1.notifyDataSetChanged();
            }
        });

        donors = db.getDonorDetails(getContext());
        adapter1 = new ListViewDonorSearchAdapter(getContext(),0,donors);
        donorsList.setAdapter(adapter1);


        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(), "Connection Destroyed", Toast.LENGTH_SHORT).show();
        // as all data filled , so connection closed
        try {
            db.closeConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


}