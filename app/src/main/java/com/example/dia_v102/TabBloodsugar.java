package com.example.dia_v102;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TabBloodsugar extends Fragment {

    private RecyclerView recyclerView;
    private BSAdapter adapter;

    private Func_InfoBox infoBox;
    private final FirebaseUser CurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userID;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private TextView nowTime;  // nowTime 변수를 클래스의 필드로 선언
    private final Handler timeHandler = new Handler(Looper.getMainLooper()); // 시간 업데이트를 위한 Handler
    private Runnable timeRunnable; // 시간을 업데이트할 Runnable

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_tab_bloodsugar 레이아웃을 사용하여 view를 생성
        View view = inflater.inflate(R.layout.tab_bloodsugar, container, false);
        Func_InfoBox FinfoBox = new Func_InfoBox();

        // UI 요소 참조
        Spinner dropdownMenu = view.findViewById(R.id.dropdown_menu);
        EditText bloodSugarInput = view.findViewById(R.id.blood_sugar);
        Button saveButton = view.findViewById(R.id.save_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /*// 날짜 및 시간을 표시할 TextView 참조
        TextView nowTime = view.findViewById(R.id.nowtime);

        // 현재 날짜와 시간을 recode_time에 설정
        String currentTime = DateUtil.dateToString(new Date()) + " " + HourNMin();
        nowTime.setText(currentTime);*/

        // 날짜 및 시간을 표시할 TextView 참조
        nowTime = view.findViewById(R.id.nowtime);

        // 현재 날짜와 시간을 nowTime에 설정
        updateNowTime();

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag2 = dropdownMenu.getSelectedItem().toString();
                double sugar = Double.parseDouble(bloodSugarInput.getText().toString());
                String currentTime = DateUtil.dateToString(new Date()) + " " + HourNMin();
                FinfoBox.saveInfoBox(CurrUser.getUid(), null, currentTime, "혈당", tag2, sugar);
                Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                loadDiabeteData(DateUtil.dateToString(new Date()));
            }
        });

        // 데이터 로드
        loadDiabeteData(DateUtil.dateToString(new Date()));

        // 시간 업데이트를 위한 Runnable 설정
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                updateNowTime(); // 현재 시간을 업데이트
                timeHandler.postDelayed(this, 1000); // 1초 후에 다시 실행
            }
        };
        timeHandler.post(timeRunnable); // Runnable 시작

        return view;
    }

    // 현재 시간을 nowTime TextView에 업데이트하는 메서드
    private void updateNowTime() {
        String currentTime = DateUtil.dateToString(new Date()) + " " + HourNMin();
        nowTime.setText(currentTime);
    }

    // Firestore에서 데이터를 로드하여 RecyclerView에 표시
    private void loadDiabeteData(String date) {
        infoBox = new Func_InfoBox();
        userID = CurrUser.getUid();

        executorService.execute(() -> {

            infoBox.loadInfoBox(userID, "혈당", date, new Func_InfoBox.OnDataReceivedListener(){
                @Override
                public void onDataReceived(List<InfoBox> infoBoxList){
                    Log.d("BoxOut", "Success");

                    for (InfoBox infoBox : infoBoxList) {
                        Log.d("BoxOut", "InfoBox Data: " + infoBox.getTime()); // infoBox의 toString() 메서드를 사용하여 데이터를 출력
                    }
                    mainThreadHandler.post(()->{
                        adapter = new BSAdapter(infoBoxList);
                        recyclerView.setAdapter(adapter);

                    });
                }

                @Override
                public void onDataFailed(Exception exception) {
                    Log.d("BoxOut", exception.getMessage());
                }
            });
        });

    }

    private String HourNMin() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        return String.format(Locale.getDefault(), "%02d시 %02d분", hour, min);
    }
}