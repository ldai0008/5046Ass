package com.example.assignment2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment2.LogIn;
import com.example.assignment2.databinding.DebugFragmentBinding;
import com.example.assignment2.roomdatabase.dao.PainRecordDao;
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.entity.PainRecordCount;
import com.example.assignment2.roomdatabase.viewmodel.PainRecordCountViewModel;
import com.example.assignment2.roomdatabase.viewmodel.PainRecordViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugFragment extends Fragment {
    // this is test class to add initial data, delete the data and count the data
    private DebugFragmentBinding binding;
    private PainRecordViewModel painRecordViewModel;
    private PainRecordCountViewModel painRecordCountViewModel;
    private LiveData<List<PainRecordCount>> painRecordCounts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DebugFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainRecordViewModel.class);
        painRecordCountViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainRecordCountViewModel.class);

        painRecordCountViewModel.getPainRecordCounts().observe(getViewLifecycleOwner(), new Observer<List<PainRecordCount>>() {
            @Override
            public void onChanged(List<PainRecordCount> painRecordCounts) {
                if (painRecordCounts == null) {
                    return;
                }
            }
        });

        binding.btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                Toast.makeText(view.getContext(), "Init data", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
                Toast.makeText(view.getContext(), "Delete Success!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                painRecordCounts = painRecordViewModel.countLocation();
                Toast.makeText(view.getContext(), "counted!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference ref = database.getReference("pain");
//                DatabaseReference painRecordRef = ref.child("painRecords");
//
//                Map<String, PainRecord> painRecords = new HashMap<>();
//                PainRecord painRecord = new PainRecord(3, "back", "TERRIBLE", 6512, "", 22.72, 97, 1007, LogIn.email);
//                painRecords.put(painRecord.currentDate, painRecord);
//
//                painRecordRef.setValue(painRecords);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                PainRecord painRecord = new PainRecord(3, "back", "TERRIBLE", 6512, "20210513", 22.72, 97, 1007, LogIn.email);
                mDatabase.child("painRecords").child(painRecord.currentDate).setValue(painRecord);

//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("message");
//
//                myRef.setValue("Hello, World!");


                Toast.makeText(view.getContext(), "add data to firebase", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void deleteAll() {
        painRecordViewModel.deleteAll();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        PainRecord painRecord = null;
        List<PainRecord> painRecords = new ArrayList<PainRecord>();

        // set the initial data
        painRecords.add(new PainRecord(3, "back", "TERRIBLE", 6512, "", 22.72, 97, 1007, LogIn.email));
        painRecords.add(new PainRecord(4, "neck", "OKAY", 10201, "", 21.26, 80, 987, LogIn.email));
        painRecords.add(new PainRecord(8, "abdomen", "OKAY", 12000, "", 15.16, 60, 600, LogIn.email));
        painRecords.add(new PainRecord(7, "head", "GOOD", 11000, "", 17.95, 66, 810, LogIn.email));
        painRecords.add(new PainRecord(6, "neck", "OKAY", 7201, "", 18.74, 77, 857, LogIn.email));
        painRecords.add(new PainRecord(5, "elbows", "BAD", 8201, "", 14.61, 60, 654, LogIn.email));
        painRecords.add(new PainRecord(5, "abdomen", "BAD", 8214, "", 15.17, 61, 757, LogIn.email));
        painRecords.add(new PainRecord(4, "back", "OKAY", 6381, "", 18.92, 66, 775, LogIn.email));
        painRecords.add(new PainRecord(3, "neck", "GREAT", 9381, "", 20.41, 70, 948, LogIn.email));
        painRecords.add(new PainRecord(3, "elbows", "BAD", 5851, "", 24.94, 86, 1082, LogIn.email));

        String currentDate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTimeInMillis());
        for (int i = 0; i < painRecords.size(); i++) {
            painRecords.get(i).currentDate = currentDate;
            painRecordViewModel.insertOrUpdate(painRecords.get(i));

            calendar.add(Calendar.DAY_OF_YEAR, -1);
            currentDate =  new SimpleDateFormat("yyyyMMdd").format(calendar.getTimeInMillis());
        }
    }
}
