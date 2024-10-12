package com.example.dia_v102;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.databaseF.FoodCal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.dia_v102.utils.imgUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class DietItemAdapter extends RecyclerView.Adapter<DietItemAdapter.DietItemViewHolder> {

    private final List<FoodCal> foodCals;
    static int i;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(DietItemViewHolder holder, int position) {
        FoodCal foodCal = foodCals.get(position);


        holder.kcalText.setText(foodCal.getCalories()+" kcal");
        holder.timeText.setText(foodCal.getTime());
        holder.meal.setText(foodCal.getTag());

        holder.kcalText.setOnClickListener(v->{
            if(holder.chartHidden.getVisibility() == View.GONE){holder.chartHidden.setVisibility(View.VISIBLE);}
            else {holder.chartHidden.setVisibility(View.GONE);}
        });

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

        // 클릭 시 nutritionInfo를 VISIBLE로 변경
        holder.infoButton.setOnClickListener(v -> {
            if (holder.hiddenView.getVisibility() == View.GONE) {
                holder.hiddenView.setVisibility(View.VISIBLE);
            } else {
                holder.hiddenView.setVisibility(View.GONE);
            }
        });
        String nutriaText = "칼로리: " + String.format(Locale.getDefault(),"%.2f", foodCal.getCalories()) + " kcal" +
                "\n 탄수화물: " + String.format(Locale.getDefault(),"%.2f", foodCal.getCarbohydrate()) + " g" +
                "\n 단백질: " + String.format(Locale.getDefault(),"%.2f", foodCal.getProtein()) + " g" +
                "\n 지방: " + String.format(Locale.getDefault(),"%.2f", foodCal.getFat()) + " g" +
                "\n 콜레스테롤: " + String.format(Locale.getDefault(),"%.2f", foodCal.getCholesterol()) + " mg" +
                "\n 나트륨: " + String.format(Locale.getDefault(),"%.2f", foodCal.getSodium()) + " mg" +
                "\n 설탕 당: " + String.format(Locale.getDefault(),"%.2f", foodCal.getSugar()) + " g";
        holder.nutritionInfo.setText(nutriaText);



        String foodFull = foodCal.getFood();
        String imgFull = foodCal.getImgName();
        String[] foods = foodFull.split("/");
        String[] images = imgFull.split("/");
        i = 0;
        int foodNum = foods.length;
        imgSlider(holder, foods[i], images[i]);
        holder.front.setOnClickListener(v->{
            if(i!=0){
                i-=1;
                imgSlider(holder, foods[i], images[i]);
            }
        });
        holder.back.setOnClickListener(v->{
           if(i!=foodNum-1){
               i+=1;
               imgSlider(holder, foods[i], images[i]);
           }
        });

    }

    private void imgSlider(DietItemViewHolder holder, String foodName, String imgName) {
        Context context = holder.itemView.getContext();

        holder.menuName.setText(foodName);
        imgUtil.setImage(context, holder.photo, imgName);
    }

    @Override
    public int getItemCount() {
        return foodCals.size();
    }

    public static class DietItemViewHolder extends RecyclerView.ViewHolder {
        TextView menuName, nutritionInfo, kcalText, timeText, meal;
        ImageView photo;
        PieChart pieChart;
        LinearLayout infoButton, chartHidden, hiddenView;
        Button front, back;


        public DietItemViewHolder(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.recode_time_diet);
            kcalText = itemView.findViewById(R.id.kcal);
            meal = itemView.findViewById(R.id.mealTag);

            photo = itemView.findViewById(R.id.photo);

            nutritionInfo = itemView.findViewById(R.id.nutrition_info);
            pieChart = itemView.findViewById(R.id.pieChart);
            infoButton = itemView.findViewById(R.id.infoButton);
            chartHidden = itemView.findViewById(R.id.chartHidden);
            hiddenView = itemView.findViewById(R.id.hiddenView);

            menuName = itemView.findViewById(R.id.menu_name);
            front = itemView.findViewById(R.id.frontBtn);
            back = itemView.findViewById(R.id.backBtn);
        }
    }
}
