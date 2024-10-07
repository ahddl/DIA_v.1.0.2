package com.example.dia_v102.databaseF;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.dia_v102.utils.NicknameCallback;
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
                        Log.d("Firebase", "User data is null.");
                        listener.onFailure(new Exception("User data is null."));
                    }
                } else {
                    Log.d("Firebase", "User not found.");
                    listener.onFailure(new Exception("User not found."));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Failed to load user data.", error.toException());
                listener.onFailure(error.toException());
            }

        });
    }

    // 데이터 로드 완료 시 콜백을 위한 리스너 인터페이스
    public interface DataLoadListener {
        void onDataLoaded(UserInfo userInfo);
        void onFailure(Exception e);
    }

    // 사용자 데이터 읽기
    // 닉네임을 반환하는 함수
    public void getNick(String userID, final NicknameCallback nicknameCallback) {
        myRef.child(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    // UserInfo 객체로 변환하고 닉네임 가져오기
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    if (userInfo != null) {
                        nicknameCallback.onCallback(userInfo.getNick());
                    } else {
                        Log.d("Firebase", "User data is null");
                        nicknameCallback.onCallback(null);
                    }
                } else {
                    Log.d("Firebase", "User not found");
                    nicknameCallback.onCallback(null);
                }
            } else {
                Log.d("Firebase", "Failed to retrieve user data", task.getException());
                nicknameCallback.onCallback(null);
            }
        });
    }


}
