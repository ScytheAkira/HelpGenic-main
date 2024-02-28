package com.example.helpgenic.CommonAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.Classes.Document;
import com.example.helpgenic.Classes.Prescription;
import com.example.helpgenic.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAppointmentDocsAdapter2 extends ArrayAdapter<Document> {


    public ListViewAppointmentDocsAdapter2(@NonNull Context context, int resource, @NonNull ArrayList<Document> objects) {

        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Document item  = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_custom_design_appointment_docs_1, parent, false);
        }


        TextView fileName = convertView.findViewById(R.id.fileName);
        fileName.setText("Document " + Integer.toString(position+1));



        return convertView;
    }
}
