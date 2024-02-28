package com.example.helpgenic.PatientAdapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.helpgenic.Classes.Slot;
import com.example.helpgenic.R;


import java.util.List;

public class ListViewDsiplayingSlotsAdapter  extends ArrayAdapter<Slot> {

    Context c;
    public ListViewDsiplayingSlotsAdapter(@NonNull Context context, int resource, @NonNull List<Slot> objects) {
        super(context, resource, objects);
        c = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Slot slot = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_custom_design_patient_views_doc_prof, parent, false);
        }




        TextView availableSlot = convertView.findViewById(R.id.time);

        TextView dayName = convertView.findViewById(R.id.day);

        String sTime = slot.sTime.toString();
        String eTime = slot.eTime.toString();
        String timeRange = sTime + " -- " + eTime;

        availableSlot.setText(timeRange);
        dayName.setText(slot.day);


        return convertView;

    }

}