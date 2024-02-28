package com.example.helpgenic.CommonAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.Classes.VirtualAppointmentSchedule;
import com.example.helpgenic.R;

import java.util.ArrayList;


public class ListViewVirtualScheduleDisplayAdapter extends ArrayAdapter<VirtualAppointmentSchedule> {
    private final int resource;
    public ListViewVirtualScheduleDisplayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<VirtualAppointmentSchedule> objects) {

        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        VirtualAppointmentSchedule item  = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }


        TextView time = convertView.findViewById(R.id.time);
        TextView day = convertView.findViewById(R.id.day);

        String sTime = item.getsTime().toString();
        String eTime = item.geteTime().toString();
        String timeRange = sTime + " -- " + eTime;

        time.setText(timeRange);
        String week = item.getDay();
        day.setText(week);


        return convertView;
    }
}
