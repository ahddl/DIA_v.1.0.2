package com.example.dia_v102.databaseF;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.dia_v102.NicknameCallback;

// Firebase Realtime Database 상호 작용 클래스
public class Func_UserInfo {
    private final DatabaseReference myRef;

    // 생성자: Firebase Database 참조 초기화
    public Func_UserInfo() {
        myRef = FirebaseDatabase.getInstance().getReference("users");
    }

    // 사용자 정보 저장
    public void saveUserInfo(String userID, boolean eMailSub, String nick) {
        UserInfo userInfo = new UserInfo(userID, eMailSub, nick);
        myRef.child(userID).setValue(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "User data saved successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firebase", "Failed to save user data.", e);
                    }
                });
    }

    // 사용자 데이터 읽기
    // 닉네임을 반환하는 함수
    public void getNick(String userID, final NicknameCallback nicknameCallback) {
        myRef.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
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
            }
        });
    }


}
