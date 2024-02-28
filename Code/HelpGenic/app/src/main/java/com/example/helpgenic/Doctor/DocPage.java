/*------------------------------------------------Activity-----------------------------------------------*/

package com.example.helpgenic.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DocPage extends AppCompatActivity {

    private BottomNavigationView bnView;

    Doctor d;
    // loads the fragment on tha basis of given 'fragment'
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
        setContentView(R.layout.activity_doc_page);

        d = (Doctor) getIntent().getSerializableExtra("doctor");

        bnView = findViewById(R.id.nav_view);


        bnView.setSelectedItemId(R.id.navigation_home);
        loadFrag(new HomeDoc(d),false);

        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId() ;
                if (id == R.id.navigation_home){
                    loadFrag(new HomeDoc(d),true);
                }else if (id == R.id.navigation_profile){
                    loadFrag(new ProfileDoc(d),true);
                }else{
                    loadFrag(new Feedback(d),true);
                }
                return true;
            }
        });
    }
}