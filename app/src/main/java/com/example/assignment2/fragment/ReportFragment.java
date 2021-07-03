package com.example.assignment2.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.assignment2.databinding.ReportFragmentBinding;
import com.example.assignment2.roomdatabase.entity.PainRecordCount;
import com.example.assignment2.roomdatabase.viewmodel.PainRecordCountViewModel;
import com.example.assignment2.roomdatabase.viewmodel.PainRecordViewModel;
import com.example.assignment2.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;


public class ReportFragment extends Fragment {
    private SharedViewModel model;
    private ReportFragmentBinding binding;
    private PainRecordCountViewModel painRecordCountViewModel;
    private LiveData<List<PainRecordCount>> painRecordCounts;
    /**
     * Constructor for objects of class ReportFragment
     * have a non-parameterised (“default”) constructor
     */
    public ReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // Inflate the View for this fragment
        binding = ReportFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        painRecordCountViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainRecordCountViewModel.class);

        painRecordCountViewModel.getPainRecordCounts().observe(getViewLifecycleOwner(), new Observer<List<PainRecordCount>>() {
            // execute the draw
            @Override
            public void onChanged(List<PainRecordCount> painRecordCounts) {
                if (painRecordCounts == null) {
                    return;
                }
                draw(painRecordCounts);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void draw(List<PainRecordCount> painRecordCounts) {
        if (painRecordCounts == null || painRecordCounts.isEmpty()) {
            return;
        }

        AnyChartView anyChartView = binding.anyChartView;
        anyChartView.setProgressBar(binding.progressBar);
        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });
        List<DataEntry> data = new ArrayList<>();

        for (PainRecordCount temp: painRecordCounts) {
            data.add(new ValueDataEntry(temp.location, temp.countNumber));
        }

        pie.data(data);
        pie.title("pain location");
        pie.labels().position("outside");
        pie.legend().title().enabled(false);
        pie.legend().title()
                .text("")
                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        anyChartView.setChart(pie);
    }

}