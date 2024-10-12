package com.example.dia_v102;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.databaseF.FoodCal;
import com.example.dia_v102.databaseF.Func_FoodCal;
import com.example.dia_v102.utils.DateUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

public class FragmentDiet extends Fragment {

    private RecyclerView recyclerView;
    private DietItemAdapter adapter;
    private TextView dateText;
    private String selectedDate;

    private Func_FoodCal foodCal;

    // ExecutorService: 백그라운드 작업을 처리하는 스레드 풀
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    // Handler: 메인 스레드에서 UI 업데이트 처리
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

         /* 로딩화면: 사진 입력 후 모델 분석 때 시간 걸릴 때 사용
        Button btnLoad = view.findViewById(R.id.btnload);
        customProgressDialog = new ProgressDialog(getContext());
        if (customProgressDialog.getWindow() != null) {
            customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        btnLoad.setOnClickListener(v -> customProgressDialog.show());*/

        dateText = view.findViewById(R.id.diet_date);
        selectedDate = DateUtil.dateToString(new Date());  // 현재 날짜를 기본 값으로 설정
        setUnderlineText(dateText, selectedDate);  // 텍스트 뷰에 날짜 설정 및 밑줄 추가

        ImageView datePickerButton = view.findViewById(R.id.diet_date_btn);
        datePickerButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, month, dayOfMonth) -> {
                selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                setUnderlineText(dateText, selectedDate);  // 선택한 날짜를 텍스트 뷰에 표시
                loadDietData();  // 선택한 날짜로 데이터 로딩
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 처음 화면에 현재 날짜 데이터 로드
        loadDietData();

        return view;

    }

    // 텍스트 뷰에 밑줄을 추가, 텍스트 설정 함수
    private void setUnderlineText(TextView textView, String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    // 백그라운드에서 데이터를 로드 & UI 업데이트
    private void loadDietData() {
        String date = selectedDate;
        foodCal = new Func_FoodCal();
        String userID = FirebaseAuth.getInstance().getUid();
        executorService.execute(() -> {
            foodCal.loadFoodCal(userID, date, new Func_FoodCal.OnDataReceivedListener(){
                @Override
                public void onDataReceived(List<FoodCal> foodCalList){
                    // 메인 스레드에서 UI 업데이트
                    mainThreadHandler.post(() -> {
                        adapter = new DietItemAdapter(foodCalList);
                        recyclerView.setAdapter(adapter);  // 어댑터 데이터 설정
                    });
                }
                @Override
                public void onDataFailed(Exception exception){
                    Log.d("foodDB", Objects.requireNonNull(exception.getMessage()));
                }
            });
            // 백그라운드 스레드에서 DB에서 데이터 로드
            //List<DietItem> dietItems = db.dietImageDao().getItemsByDate(date);


        });
    }

}
