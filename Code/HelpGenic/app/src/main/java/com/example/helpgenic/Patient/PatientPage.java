/*------------------------------------------------Activity-----------------------------------------------*/

package com.example.helpgenic.Patient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.helpgenic.Classes.Admin;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class PatientPage extends AppCompatActivity {

    //private ActivityPatientPageBinding binding;
    private BottomNavigationView bnView;
    Patient p;

    private void loadFrag(Fragment fragment, boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!flag){
            ft.add(R.id.container , fragment);
        }else{
            ft.replace(R.id.container , fragment);
        }
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_page);

        bnView = findViewById(R.id.nav_view);
        p = (Patient) getIntent().getSerializableExtra("patient");

//        System.out.println("--------"+p.getName());
        // Toast.makeText(this, p.getMail(), Toast.LENGTH_SHORT).show();

        bnView.setSelectedItemId(R.id.navigation_home);
        loadFrag(new HomePatient(p),false);

        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId() ;
                if (id == R.id.navigation_home){
                    loadFrag(new HomePatient(p),true);
                }else if (id == R.id.navigation_profile){
                    loadFrag(new ProfilePatient(p),true);
                }else if (id == R.id.navigation_notification){
                    loadFrag(new NotificationPatient(p),true);
                }else if(id == R.id.navigation_searchDonor){
                    loadFrag(new DonorSearch(p),true);
                }else{
                    loadFrag(new PatientHistory(p),true);
                }
                return true;
            }
        });



    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        SharedPreferences.Editor myEdit = sh.edit();
//
//
//        if(Objects.equals(sh.getInt("isNeedToUpdateUPFrag",0),1)){
//            Toast.makeText(this, "Yes Came", Toast.LENGTH_SHORT).show();
//            loadFrag(new PatientHistory(p),true);
//        }
//        myEdit.remove("isNeedToUpdateUPFrag");
//        myEdit.apply();
//
//    }
}