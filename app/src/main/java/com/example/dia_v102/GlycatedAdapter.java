package com.example.dia_v102;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

        // 당화혈색소 값에 따라 dot_view_gly 색상 변경
        double glycatedValue = glycated_hemo.getValue();
        int dotColor;

        if (glycatedValue < 5.7) {
            dotColor = Color.GREEN;
        } else if (glycatedValue >= 5.7 && glycatedValue < 6.5) {
            dotColor = Color.YELLOW;
        } else {
            dotColor = Color.RED;
        }

        // 점의 색상을 동적으로 설정
        holder.dotViewGly.setBackgroundResource(R.drawable.dot_background);
        GradientDrawable drawable = (GradientDrawable) holder.dotViewGly.getBackground();
        drawable.setColor(dotColor);
    }

    @Override
    public int getItemCount() {
        return GlycatedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView valueTextView;
        TextView recodeTime;
        View dotViewGly;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            valueTextView = itemView.findViewById(R.id.glycated_value);
            recodeTime = itemView.findViewById(R.id.recode_time);
            dotViewGly = itemView.findViewById(R.id.dot_view_gly);
        }
    }
}
