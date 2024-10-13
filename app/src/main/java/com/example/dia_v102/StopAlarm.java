package com.example.dia_v102;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class StopAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);

        // 알람을 중지하는 버튼 클릭 리스너
        findViewById(R.id.stopAlarmButton).setOnClickListener(v -> {
            Alarm.stopAlarmSound(this);  // static 메서드 호출
            finish();  // 액티비티 종료
        });
    }
}
