package com.example.helpgenic.Patient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.PatientAdapters.ViewPagerPatientHistoryAdapter;
import com.example.helpgenic.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class PatientHistory extends Fragment {

    TabLayout tab;
    ViewPager2 viewPager;


    Patient p;
    public PatientHistory(Patient p) {
        // Required empty public constructor
        this.p = p;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_history, container, false);

        tab = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.viewPager);

        ViewPagerPatientHistoryAdapter adapter = new ViewPagerPatientHistoryAdapter(getActivity().getSupportFragmentManager(), getLifecycle(),  p );
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tab, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("Upcoming Appointments");
                }else{
                    tab.setText("History");
                }
            }
        }).attach();

        return view;
    }
}