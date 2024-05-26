package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutputNutient extends AppCompatActivity {

    TextView outputmenu1;
    PieChart pieChart;
    TextView nutList;
    TextView caloriesTextView;
    ProgressBar caloriesProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_output_nutient);

        outputmenu1 = findViewById(R.id.outputmenu1);
        pieChart = findViewById(R.id.pieChart);
        nutList = findViewById(R.id.nutList);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        caloriesProgressBar = findViewById(R.id.caloriesProgressBar);

        //앞에서 받아온 메뉴 이름 값 출력
        String outputMenu = getIntent().getStringExtra("outputMenu");
        if (outputMenu != null) {
            outputmenu1.setText(outputMenu);
        }

        //값 지정(임의)
        float totalCalories = 762.58f;
        float averageDailyCalories = 2000f;

        //프로그레스 바
        caloriesTextView.setText("Calories: " + totalCalories + " / " + averageDailyCalories);
        caloriesProgressBar.setProgress((int) totalCalories);


        //백분율로 표시하여 파이 자료의 전체 합이 100%가 되로록 함

        //데이터 설정; 임시로 달걀찜 값 넣어둠
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(15.58f, "탄수화물(g)"));
        entries.add(new PieEntry(46.12f, "단백질(g)"));
        entries.add(new PieEntry(57.1f, "지방(g)"));
        entries.add(new PieEntry(1378.77f, "콜레스테롤(mg)"));
        entries.add(new PieEntry(570.5f, "나트륨(mg)"));

        //데이터 정보
        PieDataSet dataSet = new PieDataSet(entries, "  {영양소 정보}");

        //각 섹션 색상 정함
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow1));
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow2));
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow3));
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow4));
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow5));
        dataSet.setColors(colors);

        //파이차트 안에 텍스트 크기
        dataSet.setValueTextSize(16f);

        //데이터 설정값 입력
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh the chart

        pieChart.getDescription().setEnabled(false); //차트 설명 비활성화
        pieChart.setCenterText("영양소"); //중간에 텍스트
        pieChart.setDrawEntryLabels(false);  //

        //텍스트 출력
        String nutritionalInfo = "탄수화물: 15.58g\n"
                + "단백질: 46.12g\n"
                + "지방: 57.1g\n"
                + "콜레스테롤: 1378.77mg\n"
                + "나트륨: 570.5mg";
        nutList.setText(nutritionalInfo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}