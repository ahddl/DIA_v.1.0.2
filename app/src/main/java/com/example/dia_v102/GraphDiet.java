package com.example.dia_v102;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.dia_v102.databaseF.FoodCal;
import com.example.dia_v102.databaseF.Func_FoodCal;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GraphDiet extends Fragment {
    private BarChart barChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.graph_diet, container, false);
        barChart = view.findViewById(R.id.barChart);
        loadData();
        return view;
    }

    private void loadData() {
        Func_FoodCal foodCal = new Func_FoodCal();
        foodCal.loadFoodCal_Graph(new Func_FoodCal.OnDataReceivedListener(){
            @Override
            public void onDataReceived(List<FoodCal> foodCalList){
                dataProcessing(foodCalList);
            }
            @Override
            public void onDataFailed(Exception exception){
                Log.d("foodDB", Objects.requireNonNull(exception.getMessage()));
            }
        });

    }

    private void dataProcessing(List<FoodCal> foodCalList){
        List<FoodCal> newList = new ArrayList<>();
        int head = 0;
        for(FoodCal item : foodCalList){
            if(newList.isEmpty()){newList.add(item);}
            else {
                if (newList.get(head).getDate().equals(item.getDate())) {
                    FoodCal fixData = newList.get(head);
                    fixData.setCarbohydrate(fixData.getCarbohydrate() + item.getCarbohydrate());
                    fixData.setProtein(fixData.getProtein() + item.getProtein());
                    fixData.setFat(fixData.getFat() + item.getFat());
                    newList.set(head, fixData);
                } else {
                    newList.add(item);
                    head++;
                }
            }
        }
        drawBarChart(newList);
    }

    private void drawBarChart(List<FoodCal> foodCalList) {
        List<BarEntry> carbEntries = new ArrayList<>();
        List<BarEntry> proteinEntries = new ArrayList<>();
        List<BarEntry> fatEntries = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        int index = 0;
        for (FoodCal item : foodCalList) {
            Log.d("graphDiet", item.getDate());
            dates.add(item.getDate().substring(5)); // 날짜 형식
            // 막대의 인덱스 설정
            carbEntries.add(new BarEntry(index, (float) item.getCarbohydrate()));
            proteinEntries.add(new BarEntry(index + 0.1f, (float) item.getProtein()));
            fatEntries.add(new BarEntry(index + 0.2f, (float) item.getFat()));
            index++; // 인덱스 증가
        }

        BarDataSet carbohydratesDataSet = new BarDataSet(carbEntries, "Carbohydrates");
        carbohydratesDataSet.setColor(Color.BLUE);
        carbohydratesDataSet.setValueTextSize(12f);

        BarDataSet proteinDataSet = new BarDataSet(proteinEntries, "Protein");
        proteinDataSet.setColor(Color.GREEN);
        proteinDataSet.setValueTextSize(12f);

        BarDataSet fatDataSet = new BarDataSet(fatEntries, "Fat");
        fatDataSet.setColor(Color.RED);
        fatDataSet.setValueTextSize(12f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates)); // X축에 날짜 표시
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        BarData barData = new BarData(carbohydratesDataSet, proteinDataSet, fatDataSet);
        barData.setBarWidth(0.3f); // 막대의 너비 설정
        barChart.setData(barData);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate(); // 그래프 갱신

        barChart.setVisibleXRangeMaximum(7); // 표시할 최대 날짜 수 설정
    }
}