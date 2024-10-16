package com.example.dia_v102;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dia_v102.utils.AlarmReceiver;

import java.util.Calendar;

public class Alarm extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static MediaPlayer mediaPlayer;  // static 필드로 선언
    private static final String ALARM_FLAG = "isAlarmSet";  // 플래그 키

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("AlarmPrefs", MODE_PRIVATE);

        // 권한 요청을 액티비티에 들어오자마자 수행
        requestAlarmPermission();

        // 혈당 알람 버튼 클릭 시 다이얼로그 호출
        findViewById(R.id.btnBloodSugarAlarm).setOnClickListener(v -> showAlarmConfirmationDialog());

    }

    // 권한 요청 함수
    private void requestAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                // 권한 요청
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    // 알람 설정 동의를 위한 다이얼로그 호출
    private void showAlarmConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("혈당 알람 설정")
                .setMessage("혈당 알람을 설정하시겠습니까?")
                .setPositiveButton("확인", (dialog, which) -> {
                    saveAlarmAgreement(true);  // 동의 저장
                    Toast.makeText(Alarm.this, "혈당 알람에 동의하셨습니다.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("취소", (dialog, which) -> {
                    saveAlarmAgreement(false);  // 동의 거부 저장
                    Toast.makeText(Alarm.this, "혈당 알람을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    // SharedPreferences에 혈당 알람 동의 여부 저장
    private void saveAlarmAgreement(boolean isAgreed) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isBloodSugarAlarmAgreed", isAgreed);
        editor.apply();
    }

    // 알람을 설정하는 함수 (DietCheckMenu에서 호출됨)
    public static void setBloodSugarAlarm(Context context, int secondsLater) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isAlarmSet = sharedPreferences.getBoolean(ALARM_FLAG, false);  // 플래그 확인

        if (isAlarmSet) {
            Toast.makeText(context, "알람이 이미 설정되어 있습니다.", Toast.LENGTH_SHORT).show();
            return;  // 알람이 이미 설정되어 있으면 종료
        }

        try {
            // 알람 발생 시 실행할 인텐트 설정 (AlarmReceiver로 전송)
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // 현재 시간으로부터 secondsLater 후에 알람 설정
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, secondsLater);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            // 알람이 등록되면 플래그를 true로 설정
            editor.putBoolean(ALARM_FLAG, true);
            editor.apply();

            Toast.makeText(context, secondsLater + "초 후 알람이 설정되었습니다.", Toast.LENGTH_SHORT).show();

        } catch (SecurityException e) {
            Toast.makeText(context, "알람 설정 권한이 필요합니다.", Toast.LENGTH_LONG).show();
        }
    }

    // 알람 사운드를 재생하는 함수 (AlarmReceiver에서 호출될 예정)
    public static void playAlarmSound(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);  // raw 폴더의 alarm_sound.mp3 재생
        mediaPlayer.start();
    }

    // 알람 사운드를 중지하는 함수
    public static void stopAlarmSound(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 플래그를 false로 설정하여 새로운 알람을 설정할 수 있게 함
        editor.putBoolean(ALARM_FLAG, false);
        editor.apply();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
