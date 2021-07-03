package com.example.assignment2.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment2.LogIn;
import com.example.assignment2.TimeReceiver;
import com.example.assignment2.databinding.PainDataEntryFragmentBinding;
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.viewmodel.PainRecordViewModel;
import com.example.assignment2.weather.Main;
import com.google.android.material.slider.Slider;
import com.hsalf.smileyrating.SmileyRating;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;


public class PainDataEntryFragment extends Fragment {
    // the binding of the fragment
    // view model of the fragment
    // the step goal input
    // the step taken input
    // save and edit buttons
    // pain location of the fragment
    // pain level of the fragment
    // smile level of smile rating
    private PainDataEntryFragmentBinding addBinding;
    private PainRecordViewModel painRecordViewModel;
    private EditText stepGoal;
    private EditText stepTaken;
    private Button save, edit;
    private String location;
    private int painLevel;
    private SmileyRating smileLevel;

    /**
     * Constructor for objects of class PainDataEntryFragment
     * have a non-parameterised (“default”) constructor
     */
    public PainDataEntryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addBinding = com.example.assignment2.databinding.PainDataEntryFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainRecordViewModel.class);
        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {

                String allPainRecord = "";
                // for loop to get the records id, pain level, mood level from the record to check the value is valid
                for (PainRecord temp : painRecords) {
                    allPainRecord = temp.painId + " " + temp.painLevel + " " + temp.moodLevel + System.getProperty("line.separator") + allPainRecord;
                }
                addBinding.textviewContent.setText(allPainRecord);
            }
        });
        // initial the binding data
        stepGoal = addBinding.etStepGoal;
        stepTaken = addBinding.etStepTaken;
        save = addBinding.bSave;
        edit = addBinding.bEdit;
        smileLevel = addBinding.moodLevel;
        // get the slider value of the pain level
        addBinding.slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                painLevel = (int) value;

            }
        });

        // list of pain location
        List<String> list = new ArrayList<>();
        list.add("back");
        list.add("neck");
        list.add("head");
        list.add("knees");
        list.add("hips");
        list.add("abdomen");
        list.add("elbows");
        list.add("shoulders");
        list.add("shins");
        list.add("jaw");
        list.add("facial");
        // add the location into spin
        final ArrayAdapter<String> spiner = new ArrayAdapter<String>
                (view.getContext(), android.R.layout.simple_spinner_item, list);
        spiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addBinding.spinLocation.setAdapter(spiner);
        addBinding.spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String locationSele = parent.getItemAtPosition(position).toString();
                if (locationSele != null) {
                    location = locationSele;
                } else {

                }
            }
            // if nothing selected in spin
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set the mood level title
        smileLevel.setTitle(SmileyRating.Type.TERRIBLE, "Very low");
        smileLevel.setTitle(SmileyRating.Type.BAD, "Low");
        smileLevel.setTitle(SmileyRating.Type.OKAY, "Average");
        smileLevel.setTitle(SmileyRating.Type.GOOD, "Good");
        smileLevel.setTitle(SmileyRating.Type.GREAT, "Very good");

        // execute the save bottom and alarm
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // check the empty value
                if (addBinding.moodLevel.getSelectedSmiley().toString().equals("") || addBinding.etStepTaken.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(), "Please check your input!", Toast.LENGTH_SHORT).show();
                    return;
                }

                save();
                setTime();

//                String moodLevel = addBinding.moodLevel.getSelectedSmiley().name();
                Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();
            }


        });
        // execute the edit bottom
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit();

                Toast.makeText(view.getContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        });
        // execute the delete bottom
        addBinding.btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
                Toast.makeText(view.getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
    // get the value of the data, save them and can not modify them
    private void save() {
        String data = new Date().toString();
        Main main = HomeFragment.weatherMain;
        int iPainLevel = (int) addBinding.slider.getValue();
        String strLocation = addBinding.spinLocation.getSelectedItem().toString();
        String strMoodLevel = addBinding.moodLevel.getSelectedSmiley().toString();
        int iStepTaken = Integer.parseInt(addBinding.etStepTaken.getText().toString());
        String strCurrentDate = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());

        addBinding.slider.setEnabled(false);
        stepGoal.setEnabled(false);
        stepTaken.setEnabled(false);
        addBinding.moodLevel.disallowSelection(true);
        addBinding.spinLocation.setEnabled(false);

        PainRecord painRecord = new PainRecord(iPainLevel, strLocation, strMoodLevel, iStepTaken, strCurrentDate
                , main.temp, main.humidity, main.pressure, LogIn.email);
        painRecordViewModel.insertOrUpdate(painRecord);
        LogIn.currPainRecord = painRecord;
    }
    // edit the values of data
    private void edit() {
        addBinding.slider.setEnabled(true);
        stepGoal.setEnabled(true);
        stepTaken.setEnabled(true);
        addBinding.moodLevel.disallowSelection(false);
        addBinding.spinLocation.setEnabled(true);

    }
    // delete all the data
    private void deleteAll() {
        painRecordViewModel.deleteAll();
    }
    // get the alarm time minus 2 mins
    private long getMillsTime(int hour, int minute) {
        long currMillsTime = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        // minus 2 minutes
        calendar.add(Calendar.MINUTE, -2);

        currMillsTime = calendar.getTimeInMillis();
        return currMillsTime;
    }
    // execute the alarm manager
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setTime() {
        //set the time picker
        int hour = addBinding.timePicker.getHour();
        int minute = addBinding.timePicker.getMinute();

        Intent intent = new Intent(getActivity().getApplication(), TimeReceiver.class);
        intent.setAction("com.example.assignment2.TimeReceiver");

        PendingIntent pt = PendingIntent.getBroadcast(getActivity().getApplication(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, getMillsTime(hour, minute), AlarmManager.INTERVAL_DAY, pt);
    }
}
