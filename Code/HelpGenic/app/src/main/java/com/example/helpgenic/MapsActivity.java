package com.example.helpgenic;

import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.PhysicalAppointmentSchedule;
import com.example.helpgenic.PatientAdapters.ListViewMapsActivityAdapter;
import com.example.helpgenic.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<PhysicalAppointmentSchedule> details;
    private TextView selectOption;
    Dialog dialog = null;
    EditText editText;
    ListView dialogList;
    ListViewMapsActivityAdapter dialogAdapter;
    Marker name;
    DbHandler db = new DbHandler();


    private void setUpDialogBox(){
        // initialize dialog
        dialog = new Dialog(this);
        // set custom design of dialog
        dialog.setContentView(R.layout.searchable_spinner_custom_design);
        // set custom height
        dialog.getWindow().setLayout(1000,1500);
        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // show
        dialog.show();

        editText = dialog.findViewById(R.id.editText);
        dialogList = dialog.findViewById(R.id.categoryList);
        dialogAdapter = new ListViewMapsActivityAdapter(this , android.R.layout.simple_list_item_1 , details);
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
    }

    private void handleDialogBoxFunctionality(){


        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // set text view as the selected item
                PhysicalAppointmentSchedule detail = (PhysicalAppointmentSchedule)adapterView.getItemAtPosition(i);
                selectOption.setText(detail.getClinicName());
                name.remove();
                name = mMap.addMarker(new MarkerOptions().position(new LatLng(detail.getLattitude(), detail.getLongitude())).title(detail.getDocName()));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(100.0f));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(detail.getLattitude(), detail.getLongitude())));

                // close the dialog box
                dialog.dismiss();
                dialog = null;
            }
        });
    }
    void setUpData(){
        db.connectToDb(MapsActivity.this);
        details = db.getPhysicalSchDetails(this);
        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        selectOption = findViewById(R.id.searchMap);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpData();


        // When user presses the select category Button
        selectOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDialogBox();

                if(dialog != null) {
                    handleDialogBoxFunctionality();
                }

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        name = mMap.addMarker(new MarkerOptions().position(new LatLng(31.4811,74.3034)).title("Fast Nuces Lahore"));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20000.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(31.4811,74.3034)));

    }
}