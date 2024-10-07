package com.example.dia_v102.databaseF;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Func_InfoBox {
    private final DatabaseReference myRef;

    public Func_InfoBox(){myRef = FirebaseDatabase.getInstance().getReference("box");}

    public void saveInfoBox(String userID, String tag1, String tag2, double value){
        InfoBox infobox = new InfoBox(tag1, tag2, value);
        myRef.child(userID).push().setValue(infobox)
            .addOnSuccessListener(avoid -> Log.d("Firebase", "Box data saved successfully."))
            .addOnFailureListener(e -> Log.d("Firebase", "Failed to save user data.", e));
    }

    public void loadInfoBox(String userID, String tag1, String date, final Func_InfoBox.OnDataReceivedListener listener) {
        myRef.child(userID).orderByChild("date").equalTo(date)
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
        void onDataReceived(List<InfoBox> infoBoxList);
        void onDataFailed(Exception exception);
    }

}
