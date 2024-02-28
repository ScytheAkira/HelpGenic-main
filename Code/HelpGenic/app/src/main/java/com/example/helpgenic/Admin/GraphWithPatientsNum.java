package com.example.helpgenic.Admin;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.helpgenic.Classes.Admin;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GraphWithPatientsNum extends Fragment {

    private Admin admin;
    DbHandler dbHandler = new DbHandler();
    AnyChartView anyChartView;
    ArrayList<String> docNames = new ArrayList<>();
    ArrayList<Integer> pAttended = new ArrayList<>();


    public GraphWithPatientsNum(Admin admin) {
        // Required empty public constructor
        this.admin = admin;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph_with_patients_num, container, false);
        anyChartView = view.findViewById(R.id.anyChart);
        setupChartView();
        return view;
    }

    private void setupChartView() {
        Pie pie = AnyChart.pie();
        int i =0;
        List<DataEntry> dataEntries = new ArrayList<>();

        dbHandler.connectToDb(getContext());

        ResultSet rs = dbHandler.get_doctors_and_prev_patients(getContext());
        try {
            while (rs.next()) {
                docNames.add(rs.getString("name"));
                pAttended.add(rs.getInt("pAttended"));
                dataEntries.add(new ValueDataEntry(docNames.get(i),pAttended.get(i)));
                i++;
            }
            pie.data(dataEntries);
            pie.title("Patients Attended in Percentage");
            anyChartView.setChart(pie);

            dbHandler.closeConnection();
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}