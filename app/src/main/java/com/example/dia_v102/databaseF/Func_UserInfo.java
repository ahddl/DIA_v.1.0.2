package com.example.dia_v102.databaseF;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

// Firebase Realtime Database 상호 작용 클래스
public class Func_UserInfo {
    private static DatabaseReference myRef;

    // 생성자: Firebase Database 참조 초기화
    public Func_UserInfo() {
        myRef = FirebaseDatabase.getInstance().getReference("users");
    }

    // 사용자 정보 저장
    public static void saveUserInfo(String userID, UserInfo userInfo) {
        myRef.child(userID).setValue(userInfo)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "User data saved successfully."))
                .addOnFailureListener(e -> Log.d("Firebase", "Failed to save user data.", e));
    }
    // 사용자 정보 로드
    public void loadData(String userID, final DataLoadListener listener) {
        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    if (userInfo != null) {
                        listener.onDataLoaded(userInfo); // 데이터 로드 성공
                        Log.d("Firebase", "User data loaded successfully.");
                    } else {
                        listener.onFailure(new Exception("User data is null."));
                        Log.d("Firebase", "User data is null.");
                    }
                } else {
                    listener.onFailure(new Exception("User not found."));
                    Log.d("Firebase", "User not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure(error.toException());
                Log.d("Firebase", "Failed to load user data.", error.toException());
            }

        });
    }

    // 데이터 로드 완료 시 콜백을 위한 리스너 인터페이스
    public interface DataLoadListener {
        void onDataLoaded(UserInfo userInfo);
        void onFailure(Exception e);
    }
}
