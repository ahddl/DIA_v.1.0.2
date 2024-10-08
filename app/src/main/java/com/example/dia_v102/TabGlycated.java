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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TabGlycated extends Fragment {

    private RecyclerView recyclerView;
    private GlycatedAdapter adapter;

    private Func_InfoBox infoBox;
    private final FirebaseUser CurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userID;
    private TextView nowTime;  // nowTime 변수를 클래스 필드로 선언

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_tab_glycated 레이아웃을 사용하여 view를 생성
        View view = inflater.inflate(R.layout.tab_glycated, container, false);
        Func_InfoBox FinfoBox = new Func_InfoBox();

        // UI 요소 참조
        EditText GlycatedInput = view.findViewById(R.id.glycated);
        Button saveButton = view.findViewById(R.id.save_button_gly);
        recyclerView = view.findViewById(R.id.recycler_view_gly);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 날짜 및 시간을 표시할 TextView 참조
        nowTime = view.findViewById(R.id.nowTime);
        updateNowTime();

        // info 버튼 클릭 리스너 추가
        ImageButton infoButton = view.findViewById(R.id.info_gly);
        infoButton.setOnClickListener(v -> showGlycatedInfoDialog());

        // 저장 버튼 클릭 리스너
        saveButton.setOnClickListener(v -> {
            //String tag2 = dropdownMenu.getSelectedItem().toString();
            double glycated = Double.parseDouble(GlycatedInput.getText().toString());
            FinfoBox.saveInfoBox(CurrUser.getUid(), "당화혈색소", null, glycated);
            Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
            loadGlycatedData(DateUtil.dateToString(new Date()));
        });

        // 데이터 로드
        loadGlycatedData(DateUtil.dateToString(new Date()));

        return view;
    }

    // showGlycatedInfoDialog 표시 메서드
    private void showGlycatedInfoDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.info_bottomsheet_gly, null);
        dialog.setContentView(dialogView);

        ImageView closeButton = dialogView.findViewById(R.id.close_button_gly);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // 현재 시간을 nowTime TextView에 업데이트 하는 메서드
    private void updateNowTime() {
        String currentTime = DateUtil.dateToString(new Date()) + " " + DateUtil.HourNMin();
        nowTime.setText(currentTime);
    }

    private void loadGlycatedData(String date) {
        infoBox = new Func_InfoBox();
        userID = CurrUser.getUid();

        executorService.execute(() -> infoBox.loadInfoBox(userID, "당화혈색소", date, new Func_InfoBox.OnDataReceivedListener(){
            @Override
            public void onDataReceived(List<InfoBox> infoBoxList){
                Log.d("BoxOut", "Success");
                double sumValue = 0.0;

                for (InfoBox infoBox : infoBoxList) {
                    sumValue = sumValue + infoBox.getValue();
                }
                double avgValue = sumValue/infoBoxList.size();
                HealthSet.setHbA1cAVG(avgValue);
                mainThreadHandler.post(()->{
                    adapter = new GlycatedAdapter(infoBoxList);
                    recyclerView.setAdapter(adapter);

                });
            }

            @Override
            public void onDataFailed(Exception exception) {
                Log.d("BoxOut", Objects.requireNonNull(exception.getMessage()));
            }
        }));

    }
}