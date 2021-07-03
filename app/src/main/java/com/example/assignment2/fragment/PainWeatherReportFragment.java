package com.example.assignment2.fragment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.scales.Linear;
import com.example.assignment2.LogIn;
import com.example.assignment2.databinding.PainWeatherLineChartBinding;
import com.example.assignment2.roomdatabase.entity.PainRecord;
import com.example.assignment2.roomdatabase.viewmodel.PainWeatherReportViewModel;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PainWeatherReportFragment extends Fragment {
    // binding of the fragment
    // view model of the fragment
    // date type
    // start date of the fragment
    // end date of the fragment
    private PainWeatherLineChartBinding binding;
    private PainWeatherReportViewModel painWeatherReportViewModel;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private String startDate;
    private String endDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        binding = PainWeatherLineChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        painWeatherReportViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainWeatherReportViewModel.class);
        initDate();
//        execute();
        // pick the start date
        binding.etStartDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                pickDate(binding.etStartDate, "start");
            }

        });
        // pick the end date
        binding.etEndDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                pickDate(binding.etEndDate, "end");
            }
        });

        List<String> list = new ArrayList<>();
        list.add("temperature");
        list.add("humidity");
        list.add("pressure");
        // add the arraylist into the weather spin
        final ArrayAdapter<String> spiner = new ArrayAdapter<String>
                (view.getContext(), android.R.layout.simple_spinner_item, list);
        spiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sWeatherVariable.setAdapter(spiner);

        // execute the confirm button
        binding.bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execute();
                Toast.makeText(getContext(), "confirm", Toast.LENGTH_SHORT).show();
            }
        });
        // execute the correlation button
        binding.bTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTest();
                Toast.makeText(getContext(), "Testing...", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    // get the start date and end date
    private void pickDate(TextView textView, String type) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date selectedDate = new Date((year-1900),month,dayOfMonth);
                textView.setText(sdf.format(selectedDate));

                if (type.equals("start")){
                    startDate = sdf.format(selectedDate);
                } else {
                    endDate = sdf.format(selectedDate);
                }

            }
        } ,year, month, day);
        picker.show();

    }
    // initial the start date and end date
    private void initDate() {
        long currTime = System.currentTimeMillis();

        endDate = sdf.format(currTime);
        startDate = sdf.format(currTime - (10 - 1) * 1000 * 60 * 60 * 24);

        binding.etStartDate.setText(startDate);
        binding.etEndDate.setText(endDate);
    }
    // execute the line chart
    private void draw(List<PainRecord> painRecords) {
        if (painRecords == null || painRecords.isEmpty()) {
            return;
        }
        String weatherVariable = "";

        weatherVariable = binding.sWeatherVariable.getSelectedItem().toString();
        // initial weather is temperature
        if (weatherVariable.equals("")) {
            weatherVariable = "temperature";
        }

        AnyChartView anyChartView = binding.anyChartView;
        // anyChartView.clear();
        anyChartView.setProgressBar(binding.progressBar);

        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.title("Pain and Weather");
        // static pain Y xis and date X xis
        cartesian.yAxis(0).title("Pain level");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        Linear extraYscalesLinear = Linear.instantiate();
        // second y xis weather
        com.anychart.core.axes.Linear extraYScale = cartesian.yAxis(1);
        cartesian.yAxis(1).title(weatherVariable); // dynamic
        cartesian.yAxis(1).orientation("right")
                                .scale(extraYscalesLinear);

        List<DataEntry> data = new ArrayList<>();

        List<DataEntry> data2 = new ArrayList<>();
        // add date in the three type of weather
        for (PainRecord painRecord : painRecords) {
            data.add(new ValueDataEntry(painRecord.currentDate, painRecord.painLevel));

            if ("humidity".equals(weatherVariable)) {
                data2.add(new ValueDataEntry(painRecord.currentDate, painRecord.humidity));
            } else if ("pressure".equals(weatherVariable)) {
                data2.add(new ValueDataEntry(painRecord.currentDate, painRecord.pressure));
            } else {
                // default: temperature
                data2.add(new ValueDataEntry(painRecord.currentDate, painRecord.temperature));
            }
        }
        // left Y xis of pain level
        Line series1 = cartesian.line(data);
        series1.name("Pain level");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        // right Y xis of weather variables
        Line series2 = cartesian.line(data2);
        series2.name(weatherVariable);
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        series2.yScale(extraYscalesLinear);

        // series2.y
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }
    // execute the line chart email, start date and end date
    private void execute() {
        painWeatherReportViewModel.liveDataByEmail(LogIn.email, startDate, endDate);
        painWeatherReportViewModel.getPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                if (painRecords == null || painRecords.isEmpty()) {
                    return;
                }

                draw(painRecords);
            }
        });
    }
    //execute the correlation
    private void executeTest() {
        painWeatherReportViewModel.liveDataByEmail(LogIn.email, startDate, endDate);
        painWeatherReportViewModel.getPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                if (painRecords == null || painRecords.isEmpty()) {
                    return;
                }

                String strCorrelation = "";
                strCorrelation = testCorrelation(painRecords);
                Toast.makeText(getContext(), strCorrelation, Toast.LENGTH_SHORT).show();
            }
        });
    }
    // function of correlation
    private String testCorrelation(List<PainRecord> painRecords) {

        if (painRecords == null || painRecords.isEmpty()) {
            return "Pain Record is null";
        }

        String weatherVariable = "";
        weatherVariable = binding.sWeatherVariable.getSelectedItem().toString();
        if (weatherVariable.equals("")) {
            weatherVariable = "temperature";
        }

        double data[][] = new double[painRecords.size()][2];
        for (int i = 0; i < painRecords.size(); i++) {
            data[i][0] = painRecords.get(i).painLevel;
            if ("humidity".equals(weatherVariable)) {
                data[i][1] = painRecords.get(i).humidity;
            } else if ("pressure".equals(weatherVariable)) {
                data[i][1] = painRecords.get(i).pressure;
            } else {
                data[i][1] = painRecords.get(i).temperature;
            }
        }
        // create a real matrix
        RealMatrix m = MatrixUtils.createRealMatrix(data);
        // measure all correlation test: x-x, x-y, y-x, y-x
        for (int i = 0; i < m.getColumnDimension(); i++)
            for (int j = 0; j < m.getColumnDimension(); j++) {
                PearsonsCorrelation pc = new PearsonsCorrelation();
                double cor = pc.correlation(m.getColumn(i), m.getColumn(j));
            }
        // correlation test (another method): x-y
        PearsonsCorrelation pc = new PearsonsCorrelation(m);
        RealMatrix corM = pc.getCorrelationMatrix();
        // significant test of the correlation coefficient (p-value)
        RealMatrix pM = pc.getCorrelationPValues();
        return("p value:" + pM.getEntry(0, 1)+ "\n" + " correlation: " + corM.getEntry(0, 1));
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}