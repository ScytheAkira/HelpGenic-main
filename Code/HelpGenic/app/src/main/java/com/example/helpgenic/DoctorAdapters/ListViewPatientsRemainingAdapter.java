package com.example.helpgenic.DoctorAdapters;

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

import java.util.List;


public class ListViewPatientsRemainingAdapter extends ArrayAdapter<Appointment> {

    public ListViewPatientsRemainingAdapter(Context context, int resource, @NonNull List<Appointment> objects) {
        super(context, resource, objects);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Appointment appointment = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_custom_design_patients_remaining, parent, false);
        }

        // all views defined in list cell design
        TextView patientName = convertView.findViewById(R.id.patientNaam);
        TextView id = convertView.findViewById(R.id.id);
        TextView appointmentDate = convertView.findViewById(R.id.date);
        TextView appointmentTime = convertView.findViewById(R.id.time);


        // setting their data
        patientName.setText(appointment.getP().getName());

        id.setText(String.valueOf(appointment.getP().getId()));  // random data
        appointmentDate.setText(appointment.getAppDate().toString());
        String time = appointment.getsTime().toString();
        time += " -- ";
        time += appointment.geteTime().toString();
        appointmentTime.setText(time);



        return convertView;
    }

}
