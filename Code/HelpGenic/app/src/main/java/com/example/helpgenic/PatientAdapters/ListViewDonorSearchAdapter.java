package com.example.helpgenic.PatientAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.Classes.Donor;
import com.example.helpgenic.R;

import java.util.List;

public class ListViewDonorSearchAdapter extends ArrayAdapter<Donor> {

    public ListViewDonorSearchAdapter(@NonNull Context context, int resource, @NonNull List<Donor> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Donor donor = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_donor_search_design, parent, false);
        }

        // all views defined in list cell design
        TextView donorName = convertView.findViewById(R.id.name);
        TextView bloodGroup = convertView.findViewById(R.id.bloodgroup);
        TextView phNum = convertView.findViewById(R.id.phNUm);
        TextView address = convertView.findViewById(R.id.address);
        TextView gender = convertView.findViewById(R.id.gender);

        donorName.setText(donor.getName()+" ");
        bloodGroup.setText("("+donor.getBloodGroup()+")");
        phNum.setText(donor.getPhNum());
        address.setText(donor.getAddress());

        if(donor.getGender() == 'M'){
            gender.setText("Male");
        }else{
            gender.setText("Female");
        }


        return convertView;
    }
}
