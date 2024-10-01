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

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag2 = dropdownMenu.getSelectedItem().toString();
                double sugar = Double.parseDouble(bloodSugarInput.getText().toString());
                FinfoBox.saveInfoBox(CurrUser.getUid(), null, HourNMin(), "혈당", tag2, sugar);
                Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                loadDiabeteData(DateUtil.dateToString(new Date()));
            }
        });

        // 데이터 로드
        loadDiabeteData(DateUtil.dateToString(new Date()));

        return view;
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

        return String.format(Locale.getDefault(), "%02d-%02d", hour, min);
    }
}