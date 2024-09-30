package com.example.dia_v102;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.R;
import com.example.dia_v102.databaseF.InfoBox;

import java.util.List;

public class BSAdapter extends RecyclerView.Adapter<BSAdapter.ViewHolder> {

    private final List<InfoBox> bloodSugarList;

    public BSAdapter(List<InfoBox> bloodSugarList) {
        this.bloodSugarList = bloodSugarList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_bloodsugar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoBox bloodSugar = bloodSugarList.get(position);
        holder.mealTypeTextView.setText(bloodSugar.getTag2());
        holder.valueTextView.setText(String.valueOf(bloodSugar.getValue()));
    }

    @Override
    public int getItemCount() {
        return bloodSugarList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealTypeTextView;
        TextView valueTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealTypeTextView = itemView.findViewById(R.id.meal_type);
            valueTextView = itemView.findViewById(R.id.blood_sugar_value);
        }
    }
}
