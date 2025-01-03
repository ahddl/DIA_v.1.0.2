package com.example.dia_v102;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;

import com.example.dia_v102.databaseF.InfoBox;

import java.util.Arrays;
import java.util.List;

public class Adapter_bloodSugar extends RecyclerView.Adapter<Adapter_bloodSugar.ViewHolder> {

    private final List<InfoBox> bloodSugarList;

    public Adapter_bloodSugar(List<InfoBox> bloodSugarList) {
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

        holder.recodeTime.setText(bloodSugar.getTime());
        holder.mealTypeTextView.setText(bloodSugar.getTag2());
        holder.valueTextView.setText(String.valueOf(bloodSugar.getValue()));

        // 혈당 값에 따라 점의 색상 설정
        int bloodSugarValue = (int) bloodSugar.getValue();
        int dotColor;

        String mealType = bloodSugar.getTag2(); // mealType 확인
        // mealType을 마지막 글자와 나머지 부분으로 분리
        String color = mealType.substring(mealType.length() - 1);
        if(Arrays.asList("R","G").contains(color)) {
            mealType = mealType.substring(0, mealType.length() - 1);
            holder.mealTypeTextView.setText(mealType);
            if(color.equals("R")){dotColor = Color.RED;}
            else if(color.equals("G")){dotColor = Color.GREEN;}
            else{dotColor= Color.BLACK;}
        }
        else{
            // 색상 결정 로직
            if (Arrays.asList("기상 후(공복)", "자기 전", "기타").contains(mealType)) {
                if (bloodSugarValue >= 80 && bloodSugarValue <= 130) {
                    dotColor = Color.GREEN; // 정상 범위
                } else {
                    dotColor = Color.RED; // 경고 범위
                }
            } else if (Arrays.asList("아침 식전", "점심 식전", "저녁 식전").contains(mealType)) {
                if (bloodSugarValue >= 100 && bloodSugarValue <= 120) {
                    dotColor = Color.GREEN; // 정상 범위
                } else {
                    dotColor = Color.RED; // 경고 범위
                }
            } else { // After meals
                if (bloodSugarValue >= 120 && bloodSugarValue <= 140) {
                    dotColor = Color.GREEN; // 정상 범위
                } else {
                    dotColor = Color.RED; // 경고 범위
                }
            }
        }

        // 점의 색상을 동적으로 설정
        holder.dotView.setBackgroundResource(R.drawable.dot_background);
        GradientDrawable drawable = (GradientDrawable) holder.dotView.getBackground();
        drawable.setColor(dotColor);
    }

    @Override
    public int getItemCount() {return bloodSugarList.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealTypeTextView;
        TextView valueTextView;
        TextView recodeTime;
        View dotView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealTypeTextView = itemView.findViewById(R.id.meal_type);
            valueTextView = itemView.findViewById(R.id.blood_sugar_value);
            recodeTime = itemView.findViewById(R.id.recode_time);
            dotView = itemView.findViewById(R.id.dot_view);
        }
    }
}
