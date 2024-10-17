package com.example.dia_v102.databaseF;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Func_Report {
    private final DatabaseReference myRef;
    protected String userID;

    public Func_Report() {
        myRef = FirebaseDatabase.getInstance().getReference("danger");
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public void saveData(String food, String tag, double value){
        Report report = new Report(food, tag, value);
        myRef.child(userID).push().setValue(report)
                .addOnSuccessListener(aVoid -> {
                    // 저장 성공
                })
                .addOnFailureListener(e -> {
                    // 저장 실패
                });
    }

    public void loadData(OnDataReceivedListener listener) {
        myRef.child(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    Map<String, List<Double>> foodValueMap = new HashMap<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Report report = snapshot.getValue(Report.class);
                        if (report != null) {
                            List<Double> values = foodValueMap.getOrDefault(report.getFood(), new ArrayList<>());
                            assert values != null;
                            values.add(report.getBloodSugarValue());
                            foodValueMap.put(report.getFood(), values);
                        }
                    }
                    listener.onDataReceived(foodValueMap);
                } else {
                    listener.onDataFailed(new Exception("No data available"));
                }
            } else {
                listener.onDataFailed(task.getException());
            }
        });
    }

    public interface OnDataReceivedListener {
        void onDataReceived(Map<String, List<Double>> foodValueMap);
        void onDataFailed(Exception exception);
    }
}
