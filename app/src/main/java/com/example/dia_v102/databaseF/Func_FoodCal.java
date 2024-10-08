package com.example.dia_v102.databaseF;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Func_FoodCal {
    private final DatabaseReference myRef;

    public Func_FoodCal() {
        myRef = FirebaseDatabase.getInstance().getReference("food");
    }

    // 데이터를 저장하는 메서드
    public void saveFoodCal(String userID, String food, double calories, double carbohydrate, double protein, double fat, double cholesterol, double sodium, double sugar, String imgName) {
        FoodCal foodcal = new FoodCal(food, calories, carbohydrate, protein, fat, cholesterol, sodium, sugar, imgName);
        myRef.child(userID).push().setValue(foodcal)
                .addOnSuccessListener(aVoid -> {
                    // 저장 성공
                })
                .addOnFailureListener(e -> {
                    // 저장 실패
                });
    }

    // 데이터 로드 메서드
    public void loadFoodCal(String userID, String date, final OnDataReceivedListener listener) {
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

    // 데이터를 받기 위한 인터페이스
    public interface OnDataReceivedListener {
        void onDataReceived(List<FoodCal> foodCalList);
        void onDataFailed(Exception exception);
    }
}
