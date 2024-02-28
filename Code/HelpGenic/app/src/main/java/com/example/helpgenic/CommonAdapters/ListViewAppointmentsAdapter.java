package com.example.helpgenic.CommonAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAppointmentsAdapter extends ArrayAdapter<Appointment> {

    int size = 0;
    public ListViewAppointmentsAdapter(Context context, int resource, ArrayList<Appointment> objects) {
        super(context, resource, objects);
        size = objects.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Appointment item  = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.appointments_list_custom_design, parent, false);
        }


        TextView date = convertView.findViewById(R.id.date);
        date.setText(item.getAppDate().toString());

        TextView appointmentNum = convertView.findViewById(R.id.appointmentNum);
        appointmentNum.setText("Appointment" + (size-position));



        return convertView;
    }

}
