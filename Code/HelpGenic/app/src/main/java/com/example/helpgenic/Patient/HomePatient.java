/*------------------------------------------------Fragment-----------------------------------------------*/

package com.example.helpgenic.Patient;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.GuestUser;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.MapsActivity;
import com.example.helpgenic.PatientAdapters.customListViewAdapter;
import com.example.helpgenic.R;
import com.example.helpgenic.login;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.StringTokenizer;


public class HomePatient extends Fragment  {

    Patient p;

    public HomePatient(Patient p) {
        // Required empty public constructor
        this.p =  p;
    }

    ArrayList<Doctor> docList ;
    // String[] items = {"Rating" , "Fee" , "PatientsAttended" };
    String[] categories = {"Dermatologist" , "Allergist" , "Neurologist" , "Pathologist" , "Urologist" , "Anesthesiologist" , "Ophthalmologist"};


    ListView doctorsList; // list of Doctors
    EditText sView;
    //AutoCompleteTextView tView;
    Button selectOption, findOnMapsButton;
    Dialog dialog;
    TextView patientWelcome;
    customListViewAdapter adapter1;

    // Helper Functions
    private ArrayList<Doctor> getNewListFromCategory(String category){
        ArrayList<Doctor> temp =  new ArrayList<>();
        for(int i=0 ; i < docList.size() ; i++){

            StringTokenizer sp = new StringTokenizer(docList.get(i).getSpecialization().toLowerCase(),",");
            String str = sp.nextToken();

            if(str.equals(category.toLowerCase())){
                System.out.println(str+"  "+category.toLowerCase());
                temp.add(docList.get(i));
            }
        }

        System.out.println(temp.size());
        return temp;
    }

    @SuppressLint("SetTextI18n")
    private void setUpData(){

        DbHandler db = new DbHandler();
        if(db.connectToDb(getContext())){

            patientWelcome.setText("Welcome "+ p.getName());
            docList = db.getListOfDoctors(getContext());

            try {
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
    private void setUpDialogBox(){
        // initialize dialog
        dialog = new Dialog(getContext());
        // set custom design of dialog
        dialog.setContentView(R.layout.searchable_spinner_custom_design);
        // set custom height
        dialog.getWindow().setLayout(1000,1500);
        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // show
        dialog.show();
    }
    private void handleDialogBoxFunctionality(){

        EditText editText = dialog.findViewById(R.id.editText);
        ListView dialogList = dialog.findViewById(R.id.categoryList);
        ArrayAdapter<String> dialogAdapter = new ArrayAdapter<>(getContext() , android.R.layout.simple_list_item_1 , categories);
        dialogList.setAdapter(dialogAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // leave it
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filter list
                dialogAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // leave it
            }
        });

        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // set text view as the selected item
                selectOption.setText(dialogAdapter.getItem(i));

                ArrayList<Doctor> tempLst = getNewListFromCategory(dialogAdapter.getItem(i));

                docList.clear();
                docList.addAll(tempLst);
                adapter1.notifyDataSetChanged();

                // close the dialog box
                dialog.dismiss();
            }
        });
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Toast.makeText(getContext(), p.getMail(), Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.fragment_home_patient, container, false); // return view

        //================================== Select category functionality =======================

        selectOption = view.findViewById(R.id.selectCategory); // select category option
        patientWelcome = view.findViewById(R.id.patientWelcome);
        doctorsList = view.findViewById(R.id.DocList);
        findOnMapsButton = view.findViewById(R.id.findOnMaps);

        // When user presses the select category Button
        selectOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDialogBox();
                handleDialogBoxFunctionality();


            }
        });
        // ==================================================================================



        // ========================================= Handling Search View click =========================================
        sView = view.findViewById(R.id.searchView);       // search view at the top
        // ==============================================================================================================



        // ======================================== Handling Sort by functionality ==========================================

//        tView = view.findViewById(R.id.searchOptions);    // 'sort by' spinner option
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_spinner_dropdown_item , items);
//        tView.setAdapter(adapter);
//
//        // when user presses the autocomplete text view 'sort by' option
//        tView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String s = adapterView.getItemAtPosition(i).toString();
//                if(s.equalsIgnoreCase( "rating")){
//                    sortListByRating();
//                    adapter1.notifyDataSetChanged();
//                }
//            }
//        });

        // ==================================================================================



        // =======================================Populating the doctors list ===========================================

        try {
            // populate the data
             setUpData();
             if(docList != null){
                 // set adapter to list view
                 adapter1 = new customListViewAdapter(getContext() , R.layout.list_cell_custom_design , docList);
                 doctorsList.setAdapter(adapter1);
             }


        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        // ============== to listen as the text is getting changed in edit text to filter the list  ===========================

        sView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // leave it
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filter list
                adapter1.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // leave it
            }
        });


        // ====================================== Listening to click on doctor list row ===========================

        doctorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // go to the 'Patient Viewing Doc Profile'
                Doctor doc = (Doctor)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), PatientViewingDocProfile.class);
                intent.putExtra("doctor", doc);
                intent.putExtra("patient" , p);
                startActivity(intent);
            }
        });

        findOnMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext() , MapsActivity.class));
            }
        });
        return view;
    }

//    public void sortListByRating(){
//
//         docList.sort(Doctor.ratingDescending);
//    }

//    public void sortListByFee(){
//
//        docList.sort(Doctor.ratingDescending);
//    }
}