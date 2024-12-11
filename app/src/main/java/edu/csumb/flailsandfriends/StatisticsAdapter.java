package edu.csumb.flailsandfriends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.csumb.flailsandfriends.R;
import edu.csumb.flailsandfriends.entities.BattleRecord;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {

    // List to hold BattleRecord data
    private final List<BattleRecord> battleRecords;

    // Constructor to initialize the data list
    public StatisticsAdapter(List<BattleRecord> battleRecords) {
        this.battleRecords = battleRecords != null ? battleRecords : new ArrayList<>();
    }


    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_item, parent, false);
        return new StatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int position) {
        // Bind data to the view holder
        BattleRecord record = battleRecords.get(position);
        holder.titleTextView.setText(record.getTitle());
        holder.dateTextView.setText(String.valueOf(record.getDate()));
        holder.recordTextView.setText(record.getRecord());
    }

    @Override
    public int getItemCount() {
        // Return the size of the data list
        return battleRecords.size();
    }

    // ViewHolder class to manage views for each item
    public static class StatisticsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, dateTextView, recordTextView;

        public StatisticsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.statistics_title);
            dateTextView = itemView.findViewById(R.id.statistics_date);
            recordTextView = itemView.findViewById(R.id.statistics_record);
        }
    }
}

