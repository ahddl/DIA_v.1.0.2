package com.example.dia_v102.databaseF;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Func_FoodCal {
    private final DatabaseReference myRef;
    protected String userID;

    public Func_FoodCal() {
        myRef = FirebaseDatabase.getInstance().getReference("food");
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    // 데이터 저장 메서드
    public void saveFoodCal(String food, String tag, double calories, double carbohydrate, double protein, double fat, double cholesterol, double sodium, double sugar, String imgName) {
        FoodCal foodcal = new FoodCal(food, tag, calories, carbohydrate, protein, fat, cholesterol, sodium, sugar, imgName);
        myRef.child(userID).push().setValue(foodcal)
                .addOnSuccessListener(aVoid -> {
                    // 저장 성공
                })
                .addOnFailureListener(e -> {
                    // 저장 실패
                });
    }

    // 데이터 로드 메서드
    public void loadFoodCal(String date, final OnDataReceivedListener listener) {
        myRef.child(userID).orderByChild("date").equalTo(date)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<FoodCal> foodCalList = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FoodCal foodcal = snapshot.getValue(FoodCal.class);
                            if (foodcal != null) {
                                foodCalList.add(foodcal);
                            }
                        }

                        // 데이터 로드 성공을 알리기 위해 listener 호출
                        if (listener != null) {
                            listener.onDataReceived(foodCalList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 데이터 로드 실패 시 처리
                        if (listener != null) {
                            listener.onDataFailed(databaseError.toException());
                        }
                    }
                });
    }

    public void loadFoodCal_Graph(final OnDataReceivedListener listener) {
        myRef.child(userID).orderByChild("date")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<FoodCal> foodCalList = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FoodCal foodcal = snapshot.getValue(FoodCal.class);
                            if (foodcal != null) {
                                foodCalList.add(foodcal);
                            }
                        }

                        // 데이터 로드 성공을 알리기 위해 listener 호출
                        if (listener != null) {
                            listener.onDataReceived(foodCalList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 데이터 로드 실패 시 처리
                        if (listener != null) {
                            listener.onDataFailed(databaseError.toException());
                        }
                    }
                });
    }

    // 데이터 receive 인터페이스
    public interface OnDataReceivedListener {
        void onDataReceived(List<FoodCal> foodCalList);
        void onDataFailed(Exception exception);
    }
}
