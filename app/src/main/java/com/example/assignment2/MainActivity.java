package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.assignment2.databinding.ActivityMainBinding;
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.viewmodel.MainViewModel;
import com.example.assignment2.workmanager.PainRecordWorkTool;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth authen;
    private FirebaseAuth.AuthStateListener authenListener;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        authen = FirebaseAuth.getInstance();
        setContentView(view);
        setSupportActionBar(binding.appBar.toolbar);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment,
                R.id.nav_pain_data_entry_fragment,
                R.id.nav_daily_record_fragment,
                R.id.nav_report_fragment,
                R.id.nav_steps_report_fragment,
                R.id.nav_map_fragment,
                R.id.nav_debug_fragment
        )
                //to display the Navigation button as a drawer symbol,not being shown as an Up button
                .setOpenableLayout(binding.drawerLayout)
                .build();
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        //Sets up a NavigationView for use with a NavController.
        NavigationUI.setupWithNavController(binding.navView, navController);
        //Sets up a Toolbar for use with a NavController.
        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController,
                mAppBarConfiguration);


        authenListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (authen.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LogIn.class));
                }
            }
        };

        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);
        mainViewModel.liveDataByCurrentDate();

        mainViewModel.getCurrPainRecord().observe(this, new Observer<PainRecord>() {
            @Override
            public void onChanged(PainRecord painRecord) {
                if (painRecord != null) {
                    LogIn.currPainRecord = painRecord;
                }
            }
        });

        enqueue(); // work manager
    }

    // the data store in the database
    private void enqueue() {
//        int hour = PainRecordWorkTool.EXECUTE_HOUR;
//        int minute = PainRecordWorkTool.EXECUTE_MINUTE;

        // for test
        long laterTime = System.currentTimeMillis() + 1 * 60 * 1000;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(laterTime);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        PainRecordWorkTool painRecordWorkTool = new PainRecordWorkTool(getApplicationContext());
        painRecordWorkTool.enqueue(hour, minute);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authen.addAuthStateListener(authenListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}