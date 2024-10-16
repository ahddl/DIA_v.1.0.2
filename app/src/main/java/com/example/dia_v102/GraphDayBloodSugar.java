package com.example.dia_v102;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.dia_v102.databaseF.Func_InfoBox;
import com.example.dia_v102.databaseF.InfoBox;
import com.example.dia_v102.utils.DateUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GraphDayBloodSugar extends Fragment {
    private TextView dateText;
    private String selectedDate;
    private LineChart lineChart;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.graph_day_bloodsugar, container, false);
        lineChart = view.findViewById(R.id.lineChart);

        dateText = view.findViewById(R.id.diet_date);
        selectedDate = DateUtil.dateToString(new Date());  // 현재 날짜를 기본 값으로 설정
        setUnderlineText(dateText, selectedDate);  // 텍스트 뷰에 날짜 설정 및 밑줄 추가

        ImageView datePickerButton = view.findViewById(R.id.diet_date_btn);
        datePickerButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, month, dayOfMonth) -> {
                selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                setUnderlineText(dateText, selectedDate);  // 선택한 날짜를 텍스트 뷰에 표시
                loadData();  // 선택한 날짜로 데이터 로딩
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
        loadData();

        return view;
    }

    private void loadData() {
        String date = selectedDate;
        Func_InfoBox infoBox = new Func_InfoBox();
        executorService.execute(() -> infoBox.loadInfoBox("혈당", date, new Func_InfoBox.OnDataReceivedListener(){
            @Override
            public void onDataReceived(List<InfoBox> infoBoxList){
                setupLineChart(infoBoxList);
                addTable(infoBoxList);
            }

            @Override
            public void onDataFailed(Exception exception) {
                Log.d("BoxOut", Objects.requireNonNull(exception.getMessage()));
            }
        }));
    }

    private void addTable(List<InfoBox> infoBoxList) {
        TableLayout dataTable = requireView().findViewById(R.id.dataTable);
        // 이전의 모든 행을 지웁니다.
        dataTable.removeViewsInLayout(1, dataTable.getChildCount() - 1); // 첫 번째 행(헤더)을 제외한 모든 행 제거

        for(InfoBox item : infoBoxList){
            // 테이블에 데이터 추가
            TableRow tableRow = new TableRow(requireContext());
            TextView timeTextView = new TextView(requireContext());
            timeTextView.setText(item.getTime()); // HH:MM 형식의 시간
            timeTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

            TextView tagTextView = new TextView(requireContext());
            tagTextView.setText(item.getTag2());
            tagTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

            TextView valueTextView = new TextView(requireContext());
            valueTextView.setText(String.valueOf(item.getValue())); // Value
            valueTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

            tableRow.addView(timeTextView);
            tableRow.addView(tagTextView);
            tableRow.addView(valueTextView);

            dataTable.addView(tableRow); // 테이블에 행 추가
        }

    }

    // 텍스트 뷰에 밑줄을 추가, 텍스트 설정 함수
    private void setUnderlineText(TextView textView, String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    // 클래스의 리스트를 받아와서 꺾은선 그래프를 만드는 메서드
    private void setupLineChart(List<InfoBox> classList) {
        ArrayList<Entry> entries = new ArrayList<>();

        // time과 value 값을 꺼내서 Entry 리스트에 추가
        for (InfoBox item : classList) {
            float timeFloat = convertTimeToFloat(item.getTime());
            float value = (float)item.getValue(); // Y축 값
            entries.add(new Entry(timeFloat, value));
        }

        // 데이터셋 생성
        LineDataSet dataSet = new LineDataSet(entries, "시간 당 혈당 변화");
        dataSet.setColor(Color.BLUE); // 선 색상 설정
        dataSet.setValueTextColor(Color.BLACK); // 값 텍스트 색상 설정
        dataSet.setLineWidth(3f);//선 두께
        dataSet.setCircleRadius(4f);//점 크기


        // LineData 객체 생성
        LineData lineData = new LineData(dataSet);
        lineData.setValueTextSize(12f);

        // LineChart에 데이터 설정
        lineChart.setData(lineData);

        // X축 설정 (시간 값)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(5f); // 예: X축의 최소값을 0으로 설정 (필요시 변경)
        xAxis.setAxisMaximum(24f); // 예: X축의 최대값을 24로 설정 (필요시 변경)
        xAxis.setGranularity(1f); // X축 간격 설정
        xAxis.setAvoidFirstLastClipping(true);


        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int hour = (int)value;
                int min = (int)((value-hour) * 60);
                return String.format(Locale.getDefault(),"%02d:%02d", hour, min); // float 값에 맞는 원래 시간 레이블 반환
            }
        });
        xAxis.setEnabled(true);

        YAxis yAxis = lineChart.getAxisLeft();  // 왼쪽 Y축 가져오기
        yAxis.setAxisMinimum(50f);  // Y축 최소값 설정
        yAxis.setAxisMaximum(200f); // Y축 최대값 설정

        lineChart.getAxisRight().setEnabled(false); // 오른쪽 Y축 비활성화 (선택 사항)

        // 그래프 새로고침
        lineChart.invalidate();
    }
    private float convertTimeToFloat(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (float) (hours + (minutes/60.0));
    }
}
