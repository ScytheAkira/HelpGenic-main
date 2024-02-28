/*------------------------------------------------Fragment-----------------------------------------------*/
package com.example.helpgenic.Patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationPatient #newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationPatient extends Fragment {

    Patient p;

    public NotificationPatient(Patient p) {
        // Required empty public constructor
        this.p = p;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_patient, container, false);
    }
}