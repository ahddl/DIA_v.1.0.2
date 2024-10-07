package com.example.dia_v102;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.databaseF.InfoBox;

import java.util.List;

public class GlycatedAdapter extends RecyclerView.Adapter<GlycatedAdapter.ViewHolder> {

    private final List<InfoBox> GlycatedList;

    public GlycatedAdapter(List<InfoBox> bloodSugarList) {
        this.GlycatedList = bloodSugarList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_glycated_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoBox glycated_hemo = GlycatedList.get(position);

        holder.recodeTime.setText(glycated_hemo.getTime());
        holder.valueTextView.setText(String.valueOf(glycated_hemo.getValue()));
    }

    @Override
    public int getItemCount() {
        return GlycatedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView valueTextView;
        TextView recodeTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            valueTextView = itemView.findViewById(R.id.glycated_value);
            recodeTime = itemView.findViewById(R.id.recode_time);
        }
    }
}
