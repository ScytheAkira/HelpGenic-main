package com.example.helpgenic.DoctorAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.PatientAdapters.customListViewAdapter;
import com.example.helpgenic.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewPatientAttendedAdapter extends ArrayAdapter<Patient> {

    ArrayList<Patient> objects;
    Filter filter;
    public ListViewPatientAttendedAdapter(Context context, int resource, @NonNull ArrayList<Patient> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Patient patient = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_custom_design_patients_attended, parent, false);
        }

        // all views defined in list cell design
        TextView patientName = convertView.findViewById(R.id.patientNaam);
        TextView id = convertView.findViewById(R.id.id);


        // setting their data
        patientName.setText(patient.getName());

        id.setText(String.valueOf(patient.getId()));  // random data



        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new ListViewPatientAttendedAdapter.AppFilter<>(objects);
        return filter;

    }

    private class AppFilter<T> extends Filter {

        private ArrayList<T> sourceObjects;

        public AppFilter(List<T> objects) {
            sourceObjects = new ArrayList<T>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {

            String filterSeq = chars.toString().toLowerCase();

            FilterResults result = new FilterResults();

            if (filterSeq != null && filterSeq.length() > 0) {

                ArrayList<T> filter = new ArrayList<T>();

                for (T object : sourceObjects) {
                    // the filtering itself:
                    if (((Patient)object).getId() == Integer.parseInt((String) chars))
                        filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((Patient) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }

}
