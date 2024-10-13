package com.example.dia_v102;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 알람 사운드 재생
        Alarm.playAlarmSound(context);

        // 알람 중지 화면을 띄우기 위한 인텐트 실행
        Intent stopAlarmIntent = new Intent(context, StopAlarm.class);
        stopAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // 새로운 Task로 실행
        context.startActivity(stopAlarmIntent);  // 중지 화면(Activity) 실행
    }
}
