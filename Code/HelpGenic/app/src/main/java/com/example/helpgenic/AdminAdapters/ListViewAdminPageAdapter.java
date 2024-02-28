package com.example.helpgenic.AdminAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.R;
import com.example.helpgenic.Classes.Doctor;

import java.util.List;

public class ListViewAdminPageAdapter extends ArrayAdapter<Doctor> {
    public ListViewAdminPageAdapter(Context context, int resource, @NonNull List<Doctor> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Doctor doc = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.list_cell_admin_page_design, parent, false);
        }
        TextView docName = convertView.findViewById(R.id.DocName);
        TextView qualificationPlusProfession = convertView.findViewById(R.id.info);


        docName.setText(doc.getName());

        String info = doc.getSpecialization();

        qualificationPlusProfession.setText(info);


        return convertView;
    }
}
