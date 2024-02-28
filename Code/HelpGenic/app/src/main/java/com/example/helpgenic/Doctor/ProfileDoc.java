/*------------------------------------------------Fragment-----------------------------------------------*/

package com.example.helpgenic.Doctor;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.BookingManager;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.VirtualAppointmentSchedule;
import com.example.helpgenic.CommonAdapters.ListViewVirtualScheduleDisplayAdapter;
import com.example.helpgenic.DoctorAdapters.MyExpandableListAdapter;
import com.example.helpgenic.R;
import com.example.helpgenic.login;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileDoc extends Fragment {

    Doctor d;
    Button changeFee,addVirtualSchedule,addPhysicalSchedule;
    Dialog dialog;
    DbHandler db =new DbHandler();
    TextView fee , docName , docEmail ,docSpeciality;
    ListView virtualapps;
    ListViewVirtualScheduleDisplayAdapter adapter2;
    ArrayList<VirtualAppointmentSchedule> vSchList;
    ArrayList<String[]> clinicInfo =new ArrayList<>();
    ArrayList<String> allClinicNames= new ArrayList<>();
    View view;
    float feeAmount = 0.0f;

    List<String> groupList,childList;
    Map<String, List<String>> map;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    public ProfileDoc( Doctor d) {
        // Required empty public constructor
        this.d = d;
    }
    private void setUpDialogBox(){
        // initialize dialog
        dialog = new Dialog(getContext());
        // set custom design of dialog
        dialog.setContentView(R.layout.fee_input);
        // set custom height
        dialog.getWindow().setLayout(1000,800);
        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // show
        dialog.show();
    }

    private void handleDialogBoxFunctionality() {

        EditText feeInput =  dialog.findViewById(R.id.feeInput);
        Button feeSubmit = dialog.findViewById(R.id.feeSubmit);

        feeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), feeInput.getText().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                fee.setText("FEE: "+Integer.parseInt(feeInput.getText().toString()) + " /-");

                feeAmount = Float.parseFloat(feeInput.getText().toString());

                if(!db.isConnectionOpen()){
                    db.connectToDb(getContext());
                }
                db.updateFee(d.getId(),Integer.parseInt(feeInput.getText().toString()),getContext());


            }
        });
    }

    private void setUpData(View view){

        // setting db connection
        if(!db.isConnectionOpen()) {
            db.connectToDb(getContext());
        }

        BookingManager bm = new BookingManager();
        bm.setDb(db);

        //getting doc info

        // // populating doc Info

        docName = view.findViewById(R.id.name);
        docName.setText(d.getName());

        docEmail = view.findViewById(R.id.email);
        docEmail.setText(d.getMail());

        docSpeciality = view.findViewById(R.id.speciality);
        docSpeciality.setText(d.getSpecialization());

        fee = view.findViewById(R.id.fee);


        // getting doc id  : 1st msg call
        int docId = d.getId();

        // getting docInfo
        // // getting vSch details
        vSchList = bm.getDoctorVirtualSchedule(docId , getContext());
        d.setVSch(vSchList);



        // setting fee
        if(vSchList.size() != 0){
            feeAmount = vSchList.get(0).getFee();
            fee.setText("FEE: "+Float.toString(vSchList.get(0).getFee()) + " /-");
        }else{
            fee.setText("Boom! No fee");
        }



        // setting up the list
        virtualapps=view.findViewById(R.id.virtual_app_list);
        adapter2 = new ListViewVirtualScheduleDisplayAdapter(getContext(),R.layout.list_cell_custom_design_patient_views_doc_prof,vSchList);
        virtualapps.setAdapter(adapter2);

    }

    private void setupPhysicalScheduleData(){
        // setting physical schedule data

        // setting db connection
        if(!db.isConnectionOpen()) {
            db.connectToDb(getContext());
        }
        groupList = new ArrayList<>();
        addClinicNames(d.getId());
        setUpDataForEachClinic(d.getId());

        //--------------------- ------- seting adapter for physical schedule list ------------------------------

        expandableListAdapter = new MyExpandableListAdapter(getContext(),groupList,map, db);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition=-1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i!= lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition=i;

            }
        });

        // -----------------------------------------------------------------------------------------------------

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Toast.makeText(getContext(), Integer.toString(d.getId()), Toast.LENGTH_SHORT).show();

        view = inflater.inflate(R.layout.fragment_profile_doc, container, false);

        addPhysicalSchedule=(Button)view.findViewById(R.id.book_physical_appointment);
        addVirtualSchedule =(Button)view.findViewById(R.id.book_virtual_appointment);
        expandableListView= (ExpandableListView)view.findViewById(R.id.psExpandable);

        // setting db connection
        if(!db.isConnectionOpen()) {
            db.connectToDb(getContext());
        }


        setUpData(view);  // setting virtual schedule data


        // setting physical schedule data

        setupPhysicalScheduleData();


        Button logOut = view.findViewById(R.id.dLogOut);
        changeFee = view.findViewById(R.id.changeFee);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                myEdit.clear();
                myEdit.apply();

                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);

                getActivity().finish();
            }
        });

        changeFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDialogBox();
                handleDialogBoxFunctionality();
            }
        });


        //================================== update VSchedule =========================================
        virtualapps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VirtualAppointmentSchedule sch = (VirtualAppointmentSchedule) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), UpdateVirtualSchedule.class);
                intent.putExtra("vSch" ,sch);

                ArrayList<CharSequence> days = getUsedDays();
                days.remove(sch.getDay());

                intent.putExtra("days" ,days);
                startActivity(intent);
            }
        });

        //================================== remove service =========================================
        virtualapps.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                VirtualAppointmentSchedule item = (VirtualAppointmentSchedule)adapterView.getItemAtPosition(i);
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this service?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                vSchList.remove(item);
                                adapter2.notifyDataSetChanged();
                                //Toast.makeText(getContext(),Integer.toString(item.getId()), Toast.LENGTH_SHORT).show();

                                db.removeAppSchedule(item.getId(),getContext());
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
        //================================== add virtual schedule ================================
        addVirtualSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddVirtualSchedule.class);
                intent.putExtra("docId" ,d.getId());
                intent.putExtra("fee" ,feeAmount);

                ArrayList<CharSequence> days = getUsedDays();

                intent.putExtra("days" ,days);
                startActivity(intent);
            }
        });
        // ================================== add physical schedule ================================
        // Click listener to button 'addSchedule for physicalSchedule'

        addPhysicalSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), com.example.helpgenic.Doctor.AddPhysicalSchedule.class);
                intent.putExtra("docId" ,d.getId());

                startActivity(intent);
            }
        });



        return view;
    }

    //function to set data for physical schedule
    public void setUpDataForEachClinic(int docId){

        ResultSet resultSet = db.getAllPhyAppointments(getContext(),docId);
        try {
            while (resultSet.next()) {

                String day = resultSet.getString("day");
                Time sTime = resultSet.getTime("stime");
                Time eTime = resultSet.getTime("etime");
                String AssPhone = resultSet.getString("assistantPhoneNum");
                String fullSchedule = "   Time :      " + sTime.toString() + "  to  " + eTime.toString() + "  " + day; // concatenate data
                String[] allinOne={fullSchedule, "Assistant Contact :      "+AssPhone};
                clinicInfo.add(allinOne);
            }
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        map = new HashMap<String,List<String>>();

        int i =0;
        System.out.println(clinicInfo.size());
        for(String group: groupList){
            if(group.equals(allClinicNames.get(i)))
                loadChild(clinicInfo.get(i));
            map.put(group,childList);
            i++;
        }
    }

    private void loadChild(String[] info) {
        childList = new ArrayList<>();
        for(String x:info){
            childList.add(x);
        }
    }

    //function to set up groupItems in Expandable List;
    // // replace with current logged in docId

    public void addClinicNames(int docId){

        String clinicName;
        groupList=new ArrayList<>();
        System.out.println(docId);
        ResultSet resultSet = db.getAllClinicNames(getContext(),docId) ;
        try{
            while(resultSet.next()){
                clinicName=resultSet.getString("clinicname");
                groupList.add(clinicName);
                allClinicNames.add(clinicName);
            }
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {

        super.onResume();


        SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();

        if(sh.getBoolean("pNeedToUpdate", false)){
            // setting physical schedule data

            Toast.makeText(getContext(), "Resumed", Toast.LENGTH_SHORT).show();
            setupPhysicalScheduleData();

        }
        else if (sh.getBoolean("vNeedToUpdate", false)) {

            Toast.makeText(getContext(), "Resumed", Toast.LENGTH_SHORT).show();
            setUpData(view);  // setting virtual schedule data

        }
        myEdit.remove("pNeedToUpdate");
        myEdit.remove("vNeedToUpdate");
        myEdit.apply();


    }
    public ArrayList<CharSequence> getUsedDays(){
        ArrayList<CharSequence> days = new ArrayList<>();

        for(VirtualAppointmentSchedule v: vSchList){
            days.add(v.getDay());
        }
        return days;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(), "Connection Destroyed", Toast.LENGTH_SHORT).show();
        // as all data filled , so connection closed
        try {
            db.closeConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}