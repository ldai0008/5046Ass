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
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.entity.PainRecordCount;
import com.example.assignment2.roomdatabase.viewmodel.PainRecordCountViewModel;

import java.util.ArrayList;
import java.util.List;

public class StepsReportFragment extends Fragment {
    // binding of the fragment
    // view model of the fragment
    private ReportFragmentBinding binding;
    private PainRecordCountViewModel painRecordCountViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ReportFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        painRecordCountViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())
                .create(PainRecordCountViewModel.class);

        painRecordCountViewModel.getPainRecord().observe(getViewLifecycleOwner(), new Observer<PainRecord>() {
            @Override
            public void onChanged(PainRecord painRecord) {
                if (painRecord == null) {
                    return;
                }
                draw(painRecord);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    // draw pie chart function
    public void draw(PainRecord painRecord) {
        if (painRecord == null) {
            return;
        }

        AnyChartView anyChartView = binding.anyChartView;
        anyChartView.setProgressBar(binding.progressBar);
        Pie pie = AnyChart.pie();
        pie.innerRadius("60%");// the circle in centrel
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("goal", 10000));
        data.add(new ValueDataEntry("taken", painRecord.stepTaken));

        pie.data(data);
        pie.title("Steps report");
        pie.labels().position("outside");
        pie.legend().title().enabled(false);
        pie.legend().title()
                .text("");

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        anyChartView.setChart(pie);
    }
}
