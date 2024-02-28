package com.example.helpgenic.PatientAdapters;

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
import com.example.helpgenic.Classes.PhysicalAppointmentSchedule;


import java.util.ArrayList;
import java.util.List;

public class ListViewMapsActivityAdapter extends ArrayAdapter<PhysicalAppointmentSchedule> {

    private Filter filter;
    private ArrayList<PhysicalAppointmentSchedule> objects;
    public ListViewMapsActivityAdapter(Context context, int resource, @NonNull List<PhysicalAppointmentSchedule> objects) {
        super(context, resource, objects);
        this.objects = (ArrayList<PhysicalAppointmentSchedule>) objects;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        PhysicalAppointmentSchedule detail = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // all views defined in list cell design
        TextView locationName = convertView.findViewById(android.R.id.text1);
        locationName.setText(detail.getClinicName());


        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new ListViewMapsActivityAdapter.AppFilter<>(objects);
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
                    if (((PhysicalAppointmentSchedule)object).getClinicName().toLowerCase().contains(filterSeq))
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
                add((PhysicalAppointmentSchedule) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }
}
