package com.example.dia_v102;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.dia_v102.databaseF.Func_InfoBox;

public class GraphDiabetes extends Fragment {
    private BarChart barChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.graph_diabetes, container, false);
        barChart = view.findViewById(R.id.barChart);
        loadData();
        return view;
        
    }

    private void loadData() {
        Func_InfoBox loadBox = new Func_InfoBox();
        String tag1 = "혈당";
        loadBox.loadInfoBox_date(tag1, new Func_InfoBox.OnData_DateReceivedListener() {
            @Override
            public void onData_DateReceived(Map<String, Double> dateSumMap) {
                drawBarChart(dateSumMap);
            }

            @Override
            public void onDataFailed(Exception e) {
                Log.d("FirebaseError", Objects.requireNonNull(e.getMessage()));
            }
        });
    }

    private void drawBarChart(Map<String, Double> dateSumMap) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> dateLabels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Double> entry : dateSumMap.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue().floatValue())); // 막대 그래프 Entry에 값 추가
            dateLabels.add(entry.getKey().substring(5)); // 날짜 레이블 추가
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Blood Sugar by Date");
        dataSet.setColors(Color.RED); // 막대 그래프 색상 설정
        dataSet.setValueTextSize(12f);

        // X축 설정
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f); // X축 값 간격 설정
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels)); // X축에 날짜 표시

        // BarChart 데이터 설정
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // 막대 너비 설정

        barChart.setData(barData);
        barChart.setFitBars(true); // 그래프 막대가 차트에 맞도록 설정
        barChart.invalidate(); // 차트 새로고침

        // 처음에 최대 7개의 항목만 표시되게 설정
        barChart.setVisibleXRangeMaximum(7);

        // 가장 최근 데이터로 화면을 이동 (마지막 7개를 보여줌)
        if (dateSumMap.size() > 7) {
            barChart.moveViewToX(dateSumMap.size() - 7);
        }
    }
}