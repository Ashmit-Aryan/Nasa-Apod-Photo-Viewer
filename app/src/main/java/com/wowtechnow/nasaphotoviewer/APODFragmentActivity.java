package com.wowtechnow.nasaphotoviewer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wowtechnow.nasaphotoviewer.Fragment.AboutFragment;
import com.wowtechnow.nasaphotoviewer.Fragment.ImageFragment;
import com.wowtechnow.nasaphotoviewer.Fragment.SearchFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class APODFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod_fragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        BottomNavigationView bnv = findViewById(R.id.bottom_navigation_home);
        bnv.setOnNavigationItemSelectedListener(mOnNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ImageFragment()).commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragement = null;
            switch (item.getItemId()){
                case R.id.ImageFragment:
                    selectedFragement = new ImageFragment();
                    break;
                case R.id.SearchFragment:
                    selectedFragement = new SearchFragment();
                    break;
                case R.id.AboutFragment:
                    selectedFragement = new AboutFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragement).commit();
            return true;
        }
    };


}