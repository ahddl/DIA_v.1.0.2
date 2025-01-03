package com.example.dia_v102;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmStop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알람 중지")
                .setMessage("알람을 중지하시겠습니까?")
                .setPositiveButton("네", (dialog, which) -> {
                    Alarm.stopAlarmSound(this);
                    finish();
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
