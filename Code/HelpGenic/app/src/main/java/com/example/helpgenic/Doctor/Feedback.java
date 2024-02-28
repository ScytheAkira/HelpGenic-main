/*------------------------------------------------Fragment-----------------------------------------------*/

package com.example.helpgenic.Doctor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.DoctorAdapters.ListViewFeedbackCustomAdapter;
import com.example.helpgenic.R;

import java.sql.SQLException;

public class Feedback extends Fragment {

    Doctor d;
    RatingBar rb ;
    ListView commentsList;

    public Feedback(Doctor d) {
        // Required empty public constructor
        this.d = d;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_doc, container, false);
        rb = view.findViewById(R.id.ratingBar2);
        commentsList = view.findViewById(R.id.commentsList);

        SharedPreferences shrd =  getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        float rating = shrd.getFloat("rating" , 0.0f);
        rb.setRating(rating);


        DbHandler db = new DbHandler();
        db.connectToDb(getContext());


        Doctor docWithComments = db.getDoctorComments(d.getId(), getContext());

        if(docWithComments.getComments() != null){
            System.out.println(docWithComments.getComments().size());
            ListViewFeedbackCustomAdapter adapter = new ListViewFeedbackCustomAdapter(getContext(),0,docWithComments.getComments());
            commentsList.setAdapter(adapter);
        }


        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return view;
    }


}