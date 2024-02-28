package com.example.helpgenic.DoctorAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.Comment;
import com.example.helpgenic.R;

import java.util.List;

public class ListViewFeedbackCustomAdapter extends ArrayAdapter<Comment> {

    public ListViewFeedbackCustomAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Comment commentObj = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_feedback_custom_design, parent, false);
        }

        // all views defined in list cell design
        TextView patientName = convertView.findViewById(R.id.pName);
        TextView comment = convertView.findViewById(R.id.comment);


        // setting their data
        patientName.setText(commentObj.getPatientName());
        comment.setText(commentObj.getDescription());

        return convertView;
    }
}
