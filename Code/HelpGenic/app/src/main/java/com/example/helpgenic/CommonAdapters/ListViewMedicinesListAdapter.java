package com.example.helpgenic.CommonAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.MedicineDosage;
import com.example.helpgenic.R;

import java.util.ArrayList;

public class ListViewMedicinesListAdapter extends ArrayAdapter<MedicineDosage> {


    public ListViewMedicinesListAdapter(Context context, int resource, ArrayList<MedicineDosage> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MedicineDosage item  = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_medicines_list_custom_design, parent, false);
        }

        try {
            TextView medName = convertView.findViewById(R.id.medicine);
            TextView medDosage = convertView.findViewById(R.id.qty);
            CheckBox m =  convertView.findViewById(R.id.m);
            CheckBox a =  convertView.findViewById(R.id.a);
            CheckBox e =  convertView.findViewById(R.id.e);

            medName.setText(item.getMedicineName());
            medDosage.setText("Qty: "+ Integer.toString(item.getMedicineDosage()));

            if(item.isMorning()){
                m.setChecked(true);
            }
            if(item.isAfternoon()){
                a.setChecked(true);
            }
            if(item.isEvening()){
                e.setChecked(true);
            }
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }









        return convertView;
    }
}
