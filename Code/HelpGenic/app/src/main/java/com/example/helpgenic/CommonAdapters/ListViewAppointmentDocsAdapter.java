package com.example.helpgenic.CommonAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.Classes.Prescription;
import com.example.helpgenic.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAppointmentDocsAdapter extends ArrayAdapter<Prescription> {


    private final int resource;
    public ListViewAppointmentDocsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Prescription> objects) {

        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Prescription item  = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }


        TextView fileName = convertView.findViewById(R.id.fileName);
        fileName.setText("Prescription");



        return convertView;
    }
}
