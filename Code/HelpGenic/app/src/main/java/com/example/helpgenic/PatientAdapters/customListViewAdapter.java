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
import com.example.helpgenic.R;

import java.util.ArrayList;
import java.util.List;



public class customListViewAdapter extends ArrayAdapter<com.example.helpgenic.Classes.Doctor> {

    private Filter filter;
    private ArrayList<Doctor> objects;

    public customListViewAdapter(Context context, int resource, @NonNull List<Doctor> objects) {
        super(context, resource, objects);
        this.objects = (ArrayList<Doctor>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        com.example.helpgenic.Classes.Doctor doc = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_custom_design, parent, false);
        }
        TextView docName = convertView.findViewById(R.id.DocName);
        TextView qualificationPlusProfession = convertView.findViewById(R.id.info);
        TextView docRating = convertView.findViewById(R.id.rating);

        docName.setText("Dr. "+doc.getName());

        qualificationPlusProfession.setText(doc.getSpecialization());

        docRating.setText(String.valueOf(doc.getRating()));

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<>(objects);
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
                    if (((Doctor)object).getName().toLowerCase().contains(filterSeq))
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
                add((Doctor) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }

}
