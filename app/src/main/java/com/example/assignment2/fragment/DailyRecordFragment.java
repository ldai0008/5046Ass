package com.example.assignment2.fragment;

import android.app.job.JobServiceEngine;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.assignment2.adapter.RecyclerViewAdapter;
import com.example.assignment2.databinding.DailyRecordFragmentBinding;
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.viewmodel.PainRecordViewModel;
import com.example.assignment2.viewmodel.SharedViewModel;
import com.hsalf.smileyrating.SmileyRating;

import java.util.List;


public class DailyRecordFragment extends Fragment {
    // The binding of the fragment
    // The view model of the fragment
    // The adapter from RecyclerViewAdapter class
    // the layout manager of recycle view
    private DailyRecordFragmentBinding binding;
    private PainRecordViewModel painRecordViewModel;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Constructor for objects of class DailyRecordFragment
     * have a non-parameterised (“default”) constructor
     */
    public DailyRecordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment using the binding
        binding = DailyRecordFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        painRecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainRecordViewModel.class);
        List<PainRecord> painRecords = painRecordViewModel.getAllPainRecords().getValue();
        adapter = new RecyclerViewAdapter();

        binding.painRecordList.addItemDecoration(new
                DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        binding.painRecordList.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(getContext());
        binding.painRecordList.setLayoutManager(layoutManager);
        // get data from recycle view adapter class
        painRecordViewModel.getEmailPainRecords().observe(getViewLifecycleOwner(), myPainRecords -> adapter.setData(myPainRecords));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
