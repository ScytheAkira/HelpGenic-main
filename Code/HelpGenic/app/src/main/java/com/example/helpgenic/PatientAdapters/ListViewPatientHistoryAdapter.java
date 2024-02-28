package com.example.helpgenic.PatientAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.R;
import java.util.List;

public class ListViewPatientHistoryAdapter extends ArrayAdapter<com.example.helpgenic.Classes.Appointment> {


    public ListViewPatientHistoryAdapter( Context context, int resource, @NonNull List<com.example.helpgenic.Classes.Appointment> objects) {
        super(context, resource, objects);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        com.example.helpgenic.Classes.Appointment appointment = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_custom_design_for_patient_schedule, parent, false);
        }

        // all views defined in list cell design
        TextView docName = convertView.findViewById(R.id.DocName);
        TextView qualificationPlusProfession = convertView.findViewById(R.id.info);
        TextView appointmentDate = convertView.findViewById(R.id.date);
        TextView appointmentTime = convertView.findViewById(R.id.time);


        // setting their data
        docName.setText("Dr. "+ appointment.getDoc().getName());


        String info = appointment.getDoc().getSpecialization();
        if(appointment.getDoc().isSurgeon()){
            info += ", Surgeon";
        }

        qualificationPlusProfession.setText(info);  // random data
        appointmentDate.setText(appointment.getAppDate().toString());
        String sTime = appointment.getsTime().toString();
        String eTime = appointment.geteTime().toString();
        String timeRange = sTime + " -- " + eTime;
        appointmentTime.setText(timeRange);



        return convertView;
    }
}
