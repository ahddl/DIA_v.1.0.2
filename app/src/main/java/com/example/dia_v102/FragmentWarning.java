package com.example.dia_v102;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dia_v102.databaseF.Func_Report;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FragmentWarning extends Fragment {

    private TableLayout tableLayout;
    private Func_Report funcReport;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_warning, container,false);
        tableLayout = view.findViewById(R.id.tableLayout);
        funcReport = new Func_Report();
        loadData();
        return view;
    }

    private void loadData() {
        funcReport.loadData(new Func_Report.OnDataReceivedListener() {
            @Override
            public void onDataReceived(Map<String, List<Double>> foodValueMap) {
                for (Map.Entry<String, List<Double>> entry : foodValueMap.entrySet()) {
                    String food = entry.getKey();
                    List<Double> values = entry.getValue();

                    // 평균, 최소, 최대 값 계산
                    double average = calculateAverage(values);
                    double min = Collections.min(values);
                    double max = Collections.max(values);

                    // 새로운 테이블 행을 추가
                    addTableRow(food, average, min, max);
                }
            }

            @Override
            public void onDataFailed(Exception exception) {
                // 에러 처리
                Toast.makeText(getContext(), "데이터를 불러오는 중 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTableRow(String food, double average, double min, double max) {
        TableRow row = new TableRow(getContext());

        TextView foodTextView = new TextView(getContext());
        foodTextView.setText(food);
        foodTextView.setPadding(8, 8, 8, 8);

        TextView averageTextView = new TextView(getContext());
        averageTextView.setText(String.format(Locale.getDefault(), "%.2f", average));
        averageTextView.setPadding(8, 8, 8, 8);

        TextView minTextView = new TextView(getContext());
        minTextView.setText(String.format(Locale.getDefault(), "%.2f", min));
        minTextView.setPadding(8, 8, 8, 8);

        TextView maxTextView = new TextView(getContext());
        maxTextView.setText(String.format(Locale.getDefault(), "%.2f", max));
        maxTextView.setPadding(8, 8, 8, 8);

        // 각 textView 행에 추가
        row.addView(foodTextView);
        row.addView(averageTextView);
        row.addView(minTextView);
        row.addView(maxTextView);

        // 테이블 레이아웃- 행을 추가
        tableLayout.addView(row);
    }

    private double calculateAverage(List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }
}
