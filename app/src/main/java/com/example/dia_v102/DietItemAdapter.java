package com.example.dia_v102;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.databaseF.FoodCal;

import java.util.ArrayList;
import java.util.List;

import com.example.dia_v102.utils.imgUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class DietItemAdapter extends RecyclerView.Adapter<DietItemAdapter.DietItemViewHolder> {

    private final List<FoodCal> foodCals;

    public DietItemAdapter(List<FoodCal> foodCal) {
        this.foodCals = foodCal;
    }

    @NonNull
    @Override
    public DietItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diet_item, parent, false);
        return new DietItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DietItemViewHolder holder, int position) {
        FoodCal foodCal = foodCals.get(position);
        holder.menuName.setText(foodCal.getFood());

        String nutriaText = "칼로리: " + foodCal.getCalories() + " kcal"+
                "\n 탄수화물: " + foodCal.getCarbohydrate() + " g" +
                "\n 단백질: " + foodCal.getProtein() + " g"+
                "\n 지방: " + foodCal.getFat() + "g"+
                "\n 콜레스테롤: " + foodCal.getCholesterol() + " mg"+
                "\n 나트륨: " + foodCal.getSodium() + " mg"+
                "\n 설탕 당: " + foodCal.getSugar() + " g";
        holder.nutritionInfo.setText(nutriaText);

        // 사진 URI를 ImageView에 set
        Context context = holder.itemView.getContext();
        String imgName = foodCal.getImgName();
        imgUtil.setImage(context, holder.photo, imgName);

        // PieChart 설정
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float)foodCal.getCarbohydrate(), "탄수화물"));
        entries.add(new PieEntry((float)foodCal.getProtein(), "단백질"));
        entries.add(new PieEntry((float)foodCal.getFat(), "지방"));

        PieDataSet dataSet = new PieDataSet(entries, "영양소 구성");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        holder.pieChart.setHoleRadius(35f);
        holder.pieChart.setTransparentCircleRadius(40f);

        holder.pieChart.setData(data);
        holder.pieChart.invalidate(); // refresh

        // 파이차트 클릭 시 nutritionInfo의 visibility를 VISIBLE로 변경
        holder.infoButton.setOnClickListener(v -> {
            if (holder.nutritionInfo.getVisibility() == View.GONE) {
                holder.nutritionInfo.setVisibility(View.VISIBLE);
            } else {
                holder.nutritionInfo.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodCals.size();
    }

    public static class DietItemViewHolder extends RecyclerView.ViewHolder {
        TextView menuName;
        TextView nutritionInfo;
        ImageView photo;
        PieChart pieChart;
        LinearLayout infoButton;


        public DietItemViewHolder(View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_name);
            nutritionInfo = itemView.findViewById(R.id.nutrition_info);
            photo = itemView.findViewById(R.id.photo);
            pieChart = itemView.findViewById(R.id.pieChart);
            infoButton = itemView.findViewById(R.id.infoButton);
        }
    }
}
