package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

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

        // 앞에서 받아온 메뉴 이름 값 출력
        String outputMenu = getIntent().getStringExtra("outputMenu");
        if (outputMenu != null) {
            outputmenu1.setText(outputMenu);
            updateNutritionalInfo(outputMenu);
        }

        // 시스템 바 인셋 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateNutritionalInfo(String menu) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        String nutritionalInfo = "";
        float totalCalories = 0.0f;

        switch (menu) {
            case "고등어구이":
                addEntry(entries, 0.7f, "탄수화물(g)");
                addEntry(entries, 35.18f, "단백질(g)");
                addEntry(entries, 25.19f, "지방(g)");
                addEntry(entries, 120f, "콜레스테롤(mg)");
                addEntry(entries, 822f, "나트륨(mg)");

                nutritionalInfo = "탄수화물: 0.7g\n"
                        + "단백질: 35.18g\n"
                        + "지방: 25.19g\n"
                        + "콜레스테롤: 120mg\n"
                        + "나트륨: 822mg";

                totalCalories = 379.0f;
                break;

            case "오리구이":
                addEntry(entries, 0f, "탄수화물(g)");
                addEntry(entries, 29.68f, "단백질(g)");
                addEntry(entries, 37.19f, "지방(g)");
                addEntry(entries, 127.0f, "콜레스테롤(mg)");
                addEntry(entries, 381.0f, "나트륨(mg)");

                nutritionalInfo = "탄수화물: 0.0g\n"
                        + "단백질: 29.68g\n"
                        + "지방: 37.19g\n"
                        + "콜레스테롤: 127.0mg\n"
                        + "나트륨: 381.0mg";

                totalCalories = 462.0f;
                break;

            case "갈비구이":
                addEntry(entries, 4.24f, "탄수화물(g)");
                addEntry(entries, 45.08f, "단백질(g)");
                addEntry(entries, 52.3f, "지방(g)");
                addEntry(entries, 162.0f, "콜레스테롤(mg)");
                addEntry(entries, 919.0f, "나트륨(mg)");

                nutritionalInfo = "탄수화물: 4.24g\n"
                        + "단백질: 45.08g\n"
                        + "지방: 52.3g\n"
                        + "콜레스테롤: 162.0mg\n"
                        + "나트륨: 919.0mg";

                totalCalories = 646.0f;
                break;

            // 다른 메뉴도 여기에 추가

            default:
                Toast.makeText(this, "영양 정보를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                return;
        }

        nutList.setText(nutritionalInfo);

        // 프로그래스 바 업데이트
        float averageDailyCalories = 2000f;
        caloriesTextView.setText("Calories: " + totalCalories + " / " + averageDailyCalories);
        caloriesProgressBar.setProgress((int) totalCalories);

        // 파이 차트 데이터 설정
        PieDataSet dataSet = new PieDataSet(entries, "  {영양소 정보}");

        // 각 섹션 색상 설정
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow1));
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow2));
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow3));
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow4));
        colors.add(ContextCompat.getColor(this, R.color.pastel_rainbow5));
        dataSet.setColors(colors);

        // 파이차트 텍스트 크기 설정
        dataSet.setValueTextSize(16f);

        // 데이터 설정값 입력
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // 차트 새로고침

        pieChart.getDescription().setEnabled(false); // 차트 설명 비활성화
        pieChart.setCenterText("영양소"); // 중간 텍스트
        pieChart.setDrawEntryLabels(false);  // 항목 레이블 그리기 비활성화
    }

    private void addEntry(ArrayList<PieEntry> entries, float value, String label) {
        if (value > 0) {
            entries.add(new PieEntry(value, label));
        }
    }
}