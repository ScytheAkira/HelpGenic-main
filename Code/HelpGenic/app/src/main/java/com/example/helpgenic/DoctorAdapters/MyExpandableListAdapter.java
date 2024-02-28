package com.example.helpgenic.DoctorAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.MapsActivity2;
import com.example.helpgenic.R;
import com.google.android.gms.common.data.DataBuffer;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String, List<String>> map;
    private List<String> groupList;
    DbHandler db = new DbHandler();

    public MyExpandableListAdapter(Context context, List<String> groupList, Map<String,List<String>> map, DbHandler db) {
        this.context=context;
        this.map=map;
        this.groupList=groupList;
        this.db = db;
    }

    @Override
    public int getGroupCount() {
        return map.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return map.get(groupList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return map.get(groupList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        String clinicName = getGroup(i).toString();

        if(view == null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_listview_group_item_patient_views_doc_profile,null);
        }

        TextView item = view.findViewById(R.id.group_item);
        ImageView location = view.findViewById((R.id.location));
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Double> coordinates ;
                coordinates= db.getLattsAndLongs(groupList.get(i),context);
                //Toast.makeText(context, "Location Clicked", Toast.LENGTH_SHORT).show();

                if(coordinates.size() != 0){
                    Intent intent = new Intent(context, MapsActivity2.class);
                    intent.putExtra("latts",coordinates.get(0));
                    intent.putExtra("longs",coordinates.get(1));
                    intent.putExtra("clinicName",groupList.get(i));
                    System.out.println(coordinates.get(0));
                    System.out.println(coordinates.get(1));
                    context.startActivity(intent);

                }


            }
        });
        item.setTypeface(null, Typeface.BOLD);
        item.setText(clinicName);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String info = getChild(i,i1).toString();
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_listview_child_item_patient_views_doc_prof,null);
        }

        TextView item = view.findViewById(R.id.child_item);
        item.setText(info);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
