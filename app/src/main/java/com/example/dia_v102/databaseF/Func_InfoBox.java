package com.example.dia_v102.databaseF;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Func_InfoBox {
    private final DatabaseReference myRef;
    protected String userID_;

    public Func_InfoBox(){
        myRef = FirebaseDatabase.getInstance().getReference("box");
        userID_ =  Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public void saveInfoBox(String tag1, String tag2, double value){
        InfoBox infobox = new InfoBox(tag1, tag2, value);
        myRef.child(userID_).push().setValue(infobox)
            .addOnSuccessListener(avoid -> Log.d("Firebase", "Box data saved successfully."))
            .addOnFailureListener(e -> Log.d("Firebase", "Failed to save user data.", e));
    }

    public void loadInfoBox(String tag1, String date, final Func_InfoBox.OnDataReceivedListener listener) {
        myRef.child(userID_).orderByChild("date").equalTo(date)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<InfoBox> infoBoxList = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            InfoBox infoBox  = snapshot.getValue(InfoBox.class);
                            if (infoBox != null && infoBox.getTag1().equals(tag1)) {
                                infoBoxList.add(infoBox);
                            }
                        }
                        // HH-MM 기준 정렬
                        infoBoxList.sort(Comparator.comparing(InfoBox::getTime));

                        // 데이터 로드 성공을 알리기 위해 listener 호출
                        if (listener != null) {
                            listener.onDataReceived(infoBoxList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 데이터 로드 실패 시 처리
                        if (listener != null) {
                            listener.onDataFailed(error.toException());
                        }
                    }
                });
    }
    public void loadInfoBox_date(String tag1, final Func_InfoBox.OnData_DateReceivedListener listener) {
        myRef.child(userID_).orderByChild("date")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Double> dateSumMap = new TreeMap<>();  // 날짜별 합산 저장
                        Map<String, Integer> dateCountMap = new TreeMap<>();  // 날짜별 데이터 개수 저장

                        // Firebase에서 날짜별로 데이터 합산
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String tag = snapshot.child("tag1").getValue(String.class);
                            String date = snapshot.child("date").getValue(String.class);  // 날짜 필드
                            Double value = snapshot.child("value").getValue(Double.class); // 값 필드

                            if(tag != null && tag.equals(tag1)) {
                                if (date != null && value != null) {
                                    // 날짜별로 값을 합산하고 개수를 증가시킴
                                    //noinspection DataFlowIssue
                                    dateSumMap.put(date, dateSumMap.getOrDefault(date, 0.0) + value);
                                    //noinspection DataFlowIssue
                                    dateCountMap.put(date, dateCountMap.getOrDefault(date, 0) + 1);
                                }
                            }
                        }

                        // 날짜별 평균을 계산
                        Map<String, Double> dateAverageMap = new TreeMap<>();
                        for (String date : dateSumMap.keySet()) {
                            try {
                                //noinspection DataFlowIssue
                                double sum = dateSumMap.get(date);
                                //noinspection DataFlowIssue
                                int count = dateCountMap.get(date);
                                double average = sum / count;  // 평균 계산
                                dateAverageMap.put(date, average);
                            }catch(NullPointerException error){
                                Log.d("Graph", Objects.requireNonNull(error.getMessage()));
                            }

                        }

                        if(listener != null){
                            listener.onData_DateReceived(dateAverageMap);  // 날짜별 평균 전달
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 데이터 로드 실패 시 처리
                        if (listener != null) {
                            listener.onDataFailed(error.toException());
                        }
                    }
                });
    }



    // 데이터를 받기 위한 인터페이스
    public interface OnDataReceivedListener {
        void onDataReceived(List<InfoBox> infoBoxList);
        void onDataFailed(Exception exception);
    }

    public interface OnData_DateReceivedListener{
        void onData_DateReceived(Map<String, Double> dateSumMap);
        void onDataFailed(Exception exception);
    }

}
