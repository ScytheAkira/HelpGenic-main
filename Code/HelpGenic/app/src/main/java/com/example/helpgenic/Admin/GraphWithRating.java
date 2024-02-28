package com.example.helpgenic.Admin;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.helpgenic.Classes.Admin;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class GraphWithRating extends Fragment {


    private Admin admin;
    DbHandler dbHandler = new DbHandler();
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntriesArrayList;
    ArrayList<String> docNames = new ArrayList<>();

    public GraphWithRating(Admin admin) {
        // Required empty public constructor
        this.admin = admin;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph_with_rating, container, false);

        barChart = view.findViewById(R.id.idBarChart);

        // initializing variable for bar chart.
        barChart = view.findViewById(R.id.idBarChart);


        // calling method to get bar entries.
        getBarEntries();
        //labeling xAxis and formatting
        XAxis xAxis = barChart.getXAxis();
        Typeface tf = Typeface.DEFAULT_BOLD;
        xAxis.setTypeface(tf);
        xAxis.setTextSize(15);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        ValueFormatter formatter = new ValueFormatter() {


            @Override
            public String getFormattedValue(float value) {
                return docNames.get((int) value-1);
            }
        };

        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        // creating a new bar data set.
        barDataSet = new BarDataSet(barEntriesArrayList, "Rating Analysis");

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(Color.BLACK);

        // setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);

        return view;
    }

    private void getBarEntries() {
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();
        String docName;
        Float height;
        //get all doctors with their ratings
        dbHandler.connectToDb(getContext());
        ResultSet rs = dbHandler.getAllDocRating(getContext());

        float x = 1;

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        try {
            while (rs.next()) {
                docName = rs.getString("name");
                height = rs.getFloat("rating");
                barEntriesArrayList.add(new BarEntry(x, height));
                docNames.add(docName);
                x++;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        try {
            dbHandler.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
