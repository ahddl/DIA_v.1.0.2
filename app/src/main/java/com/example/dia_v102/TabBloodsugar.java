package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.databaseF.Func_InfoBox;
import com.example.dia_v102.databaseF.InfoBox;
import com.example.dia_v102.utils.DateUtil;
import com.example.dia_v102.utils.Warning_in_Food;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TabBloodsugar extends Fragment {

    private RecyclerView recyclerView;
    private Adapter_bloodSugar adapter;

    private Func_InfoBox infoBox;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private TextView nowTime;  // nowTime 변수를 클래스 필드로 선언
    private final Handler timeHandler = new Handler(Looper.getMainLooper()); // 시간 업데이트를 위한 Handler

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_tab_bloodsugar 레이아웃 사용 -> view 생성
        View view = inflater.inflate(R.layout.tab_bloodsugar, container, false);
        Func_InfoBox FInfoBox = new Func_InfoBox();

        // UI 요소 참조
        Spinner dropdownMenu = view.findViewById(R.id.dropdown_menu);
        EditText bloodSugarInput = view.findViewById(R.id.blood_sugar);
        Button saveButton = view.findViewById(R.id.save_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /* 날짜 및 시간을 표시할 TextView 참조
        TextView nowTime = view.findViewById(R.id.now_time);

        // 현재 날짜와 시간을 recode_time 설정
        String currentTime = DateUtil.dateToString(new Date()) + " " + HourNMin();
        nowTime.setText(currentTime);*/

        // 날짜 및 시간을 표시할 TextView 참조
        nowTime = view.findViewById(R.id.nowTime);
        updateNowTime();

        // info 버튼 클릭 리스너 추가
        ImageButton infoButton = view.findViewById(R.id.info);
        infoButton.setOnClickListener(v -> showBloodSugarInfoDialog());

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(v -> {
            String tag2 = dropdownMenu.getSelectedItem().toString();
            double sugar = Double.parseDouble(bloodSugarInput.getText().toString());

            String D_color = colorMapping(tag2, sugar);
            if(Arrays.asList("아침 식후", "점심 식후", "저녁 식후").contains(tag2)
                    && (sugar-SetHealth.bloodSugarRecent>SetHealth.getDelta_bloodsugar_std())){
                user_Danger(tag2);
            }

            //저장&정보 수정
            Warning_in_Food.dangerCall(tag2, sugar);
            FInfoBox.saveInfoBox("혈당", tag2+D_color, sugar);
            Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
            SetHealth.setBloodSugarRecent(sugar);

            loadDiabetesData(DateUtil.dateToString(new Date()));
        });

        // 데이터 로드
        loadDiabetesData(DateUtil.dateToString(new Date()));

        // 시간 업데이트 위한 Runnable 설정
        // 현재 시간을 업데이트
        // 1초 후에 다시 실행
        // 시간을 업데이트 할 Runnable
        Runnable timeRunnable = new Runnable() {
            @Override
            public void run() {
                updateNowTime(); // 현재 시간을 업데이트
                timeHandler.postDelayed(this, 1000); // 1초 후에 다시 실행
            }
        };
        timeHandler.post(timeRunnable); // Runnable 시작

        return view;
    }

    // BloodSugarInfoDialog 표시 메서드
    private void showBloodSugarInfoDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.info_bottomsheet, null);
        dialog.setContentView(dialogView);

        ImageView closeButton = dialogView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // 현재 시간을 nowTime TextView에 업데이트 하는 메서드
    private void updateNowTime() {
        String currentTime = DateUtil.dateToString(new Date()) + " " + DateUtil.HourNMin();
        nowTime.setText(currentTime);
    }

    // Firebase 데이터 로드-> RecyclerView에 표시
    private void loadDiabetesData(String date) {
        infoBox = new Func_InfoBox();

        executorService.execute(() -> infoBox.loadInfoBox("혈당", date, new Func_InfoBox.OnDataReceivedListener(){
            @Override
            public void onDataReceived(List<InfoBox> infoBoxList){
                //Log.d("BoxOut", "Success");
                double sumValue = 0.0;

                for (InfoBox infoBox : infoBoxList) {
                    sumValue = sumValue + infoBox.getValue();
                }
                double avgValue = sumValue/infoBoxList.size();
                SetHealth.setBloodSugarAVG(avgValue);
                mainThreadHandler.post(()->{
                    adapter = new Adapter_bloodSugar(infoBoxList);
                    recyclerView.setAdapter(adapter);
                });
            }

            @Override
            public void onDataFailed(Exception exception) {
                Log.d("BoxOut", Objects.requireNonNull(exception.getMessage()));
            }
        }));
    }

    private String colorMapping(String mealType, double bloodSugarValue){
        String dotColor;
        // 색상 결정 로직
        if (Arrays.asList("기상 후(공복)", "자기 전", "기타").contains(mealType)) {
            if (bloodSugarValue >= 80 && bloodSugarValue <= 130) {
                dotColor = "G"; // 정상 범위
            } else {
                dotColor = "R"; // 경고 범위
            }
        } else if (Arrays.asList("아침 식전", "점심 식전", "저녁 식전").contains(mealType)) {
            if (bloodSugarValue >= 100 && bloodSugarValue <= 120) {
                dotColor = "G"; // 정상 범위
            } else {
                dotColor = "R"; // 경고 범위
            }
        } else { // After meals
            if (bloodSugarValue >= 120 && bloodSugarValue <= 140) {
                dotColor = "G"; // 정상 범위
            } else {
                dotColor = "R"; // 경고 범위
            }
        }
        return dotColor;
    }

    private void user_Danger(String tag){
        //Toast.makeText(requireContext(), "위험한 식단 표시", Toast.LENGTH_SHORT).show();
        String searchTag = tag.substring(0, 2);

        Intent intent = new Intent(requireContext(), DangerFood.class);
        intent.putExtra("TAG", searchTag);
        startActivity(intent);
    }
}