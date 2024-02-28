package com.example.helpgenic.DoctorAdapters;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Doctor.PatientsAttended;
import com.example.helpgenic.Doctor.PatientsRemaining;

public class ViewPagerDocPageAdapter extends FragmentStateAdapter {


    Doctor d;
    public ViewPagerDocPageAdapter( FragmentManager fragmentManager, Lifecycle lifecycle, Doctor d) {
        super(fragmentManager, lifecycle);
        this.d = d;
    }

    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new PatientsRemaining(d);
        }else{
            return new PatientsAttended(d);
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
