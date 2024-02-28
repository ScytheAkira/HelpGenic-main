package com.example.helpgenic.Patient;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.BookingManager;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.Classes.PhysicalAppointmentSchedule;
import com.example.helpgenic.Classes.ReportsHandler;
import com.example.helpgenic.Classes.VirtualAppointmentSchedule;
import com.example.helpgenic.CommonAdapters.ExpandableListViewAdapter;
import com.example.helpgenic.CommonAdapters.ListViewAppointmentsAdapter;
import com.example.helpgenic.CommonAdapters.ListViewVirtualScheduleDisplayAdapter;
import com.example.helpgenic.Doctor.AppointmentDocsViewedByDoc;
import com.example.helpgenic.Doctor.DocViewingPatientProfile;
import com.example.helpgenic.DoctorAdapters.MyExpandableListAdapter;
import com.example.helpgenic.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientViewingDocProfile extends AppCompatActivity {


    ListView appointments;
    ListView virtualapps;

    TextView fee , docName , docEmail ,docSpeciality;
    public static boolean flag = false;
    Doctor d;
    Patient p;

    List<String> groupList,childList;
    Map<String, List<String>> map;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    DbHandler db = new DbHandler();
    ArrayList<String[]> clinicInfo =new ArrayList<>();
    ArrayList<String> allClinicNames= new ArrayList<>();
    ReportsHandler rh = new ReportsHandler();

    private void setupPhysicalScheduleData(){
        // setting physical schedule data
        groupList = new ArrayList<>();
        addClinicNames(d.getId());
        setUpDataForEachClinic(d.getId());

        //--------------------- ------- seting adapter for physical schedule list ------------------------------

        expandableListAdapter = new MyExpandableListAdapter(PatientViewingDocProfile.this,groupList,map,db);
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_viewing_doc_profile);

        appointments=findViewById(R.id.prior_appointments);
        expandableListView= (ExpandableListView)findViewById(R.id.psExpandable);

        d = (Doctor) getIntent().getSerializableExtra("doctor");
        p = (Patient) getIntent().getSerializableExtra("patient");


        Toast.makeText(this, d.getName(), Toast.LENGTH_SHORT).show();




        // ============================================ Get Appointment =========================================


        if(db.connectToDb(this)){

            BookingManager bm = new BookingManager();
            bm.setDb(db);

            //getting doc info

            // // populating doc Info

            docName = findViewById(R.id.docName);
            docName.setText("Dr. "+d.getName());

            docEmail = findViewById(R.id.docEmail);
            docEmail.setText(d.getMail());

            docSpeciality = findViewById(R.id.docSpeciality);
            docSpeciality.setText(d.getSpecialization());

            fee = findViewById(R.id.fee);


            // getting doc id  : 1st msg call
            int docId = d.getId();

            // getting docInfo
            // // getting vSch details
            ArrayList<VirtualAppointmentSchedule> vSchList = bm.getDoctorVirtualSchedule(docId , this);
            d.setVSch(vSchList);


            // setting fee
            if(vSchList.size() != 0){
                fee.setText("FEE: "+Float.toString(vSchList.get(0).getFee()) + " /-");
            }else{
                fee.setText("Boom! No fee");
            }



            // setting up the list
            virtualapps=findViewById(R.id.virtual_app_list);
            ListViewVirtualScheduleDisplayAdapter adapter2 = new ListViewVirtualScheduleDisplayAdapter(this,R.layout.list_cell_custom_design_patient_views_doc_prof,vSchList);
            virtualapps.setAdapter(adapter2);


            setupPhysicalScheduleData();



            // listen to click on virtual schedule row
            virtualapps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(PatientViewingDocProfile.this, DisplayingSlots.class);

                    VirtualAppointmentSchedule vs = (VirtualAppointmentSchedule)adapterView.getItemAtPosition(i);
                    intent.putExtra("vs" ,  vs);
                    intent.putExtra("doc" , d);


                    startActivity(intent);
                }
            });


            rh.setDb(db);
            rh.displayPreviousAppointments(docId , p.getId() , PatientViewingDocProfile.this, appointments);

            appointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Appointment apt = (Appointment)adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(PatientViewingDocProfile.this, AppointmentDocsViewedByPatient.class);
                    intent.putExtra("aptId" , apt.getAppId());
                    startActivity(intent);
                }
            });




        }

            // ===================================================================================================

    }

    //function to set data for physical schedule
    public void setUpDataForEachClinic(int docId){

        ResultSet resultSet = db.getAllPhyAppointments(PatientViewingDocProfile.this,docId);
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
            Toast.makeText(PatientViewingDocProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
        ResultSet resultSet = db.getAllClinicNames(PatientViewingDocProfile.this,docId) ;
        try{
            while(resultSet.next()){
                clinicName=resultSet.getString("clinicname");
                groupList.add(clinicName);
                allClinicNames.add(clinicName);
            }
        }
        catch (Exception e){
            Toast.makeText(PatientViewingDocProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(PatientViewingDocProfile.this, "Connection Destroyed", Toast.LENGTH_SHORT).show();
        // as all data filled , so connection closed
        try {
            db.closeConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}