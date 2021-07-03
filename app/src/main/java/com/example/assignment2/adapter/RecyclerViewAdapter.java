package com.example.assignment2.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.databinding.RvLayoutBinding;
import com.example.assignment2.roomdatabase.entity.PainRecord;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter
        <RecyclerViewAdapter.ViewHolder> {
    // The list of pain record
    private List<PainRecord> painRecords;

    /**
     * Constructor for objects of class RecyclerViewAdapter
     * have a non-parameterised (“default”) constructor
     */
    public RecyclerViewAdapter() {

    }
    /**
     * Constructor for objects of class RecyclerViewAdapter
     * have a parameterised constructor
     */
    public RecyclerViewAdapter(List<PainRecord> painRecords) {
        this.painRecords = painRecords;
    }

    public void setData(List<PainRecord> painRecords) {
        this.painRecords = painRecords;
        notifyDataSetChanged();
    }

    // This method creates a new view holder that is constructed with a new View, inflated from a layout
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // binding the rv layout xml
        RvLayoutBinding binding =
                RvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder
                                         viewHolder, int position) {
        if (painRecords == null) {
            return;
        }
        final PainRecord unit = painRecords.get(position);
        viewHolder.binding.tvPainId.setText(String.valueOf(unit.painId));
        viewHolder.binding.tvPainLevel.setText(String.valueOf(unit.painLevel));
        viewHolder.binding.tvLocation.setText(unit.location);
        viewHolder.binding.tvEmail.setText(unit.email);
        viewHolder.binding.tvMoodLevel.setText(unit.moodLevel);
        viewHolder.binding.tvStepTaken.setText(String.valueOf(unit.stepTaken));
        viewHolder.binding.tvTemperature.setText(String.valueOf(unit.temperature));
        viewHolder.binding.tvHumidity.setText(String.valueOf(unit.humidity));
        viewHolder.binding.tvPressure.setText(String.valueOf(unit.pressure));
        viewHolder.binding.tvCurrentDate.setText(String.valueOf(unit.currentDate));


    }
    // get the count of item
    @Override
    public int getItemCount() {
        return painRecords != null ? painRecords.size() : 0;
    }
    // update the result
    public void addUnits(List<PainRecord> results) {
        painRecords = results;
        notifyDataSetChanged();
    }
    // initial the recycler view
    public class ViewHolder extends RecyclerView.ViewHolder {
        private RvLayoutBinding binding;

        public ViewHolder(RvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}