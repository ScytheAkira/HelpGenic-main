/*------------------------------------------------Fragment-----------------------------------------------*/
package com.example.helpgenic.Doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.DoctorAdapters.ViewPagerDocPageAdapter;
import com.example.helpgenic.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class HomeDoc extends Fragment {

    TabLayout tab;
    ViewPager2 viewPager;
    Doctor d;


    public HomeDoc(Doctor d) {
        // Required empty public constructor
        this.d = d;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_doc, container, false);
        tab = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.viewPager);
        Toast.makeText(getContext(), d.getSpecialization(), Toast.LENGTH_SHORT).show();

        ViewPagerDocPageAdapter adapter = new ViewPagerDocPageAdapter(getActivity().getSupportFragmentManager(), getLifecycle() ,  d);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tab, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("Appointments");
                }else{
                    tab.setText("Patients");
                }
            }
        }).attach();
        return view;
    }
}