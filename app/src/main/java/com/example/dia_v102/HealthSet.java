package com.example.dia_v102;

import android.app.Application;
import java.util.ArrayList;
import java.util.List;

public class HealthSet extends Application {
    static double bloodSugarAVG;
    static double bloodSugarRecent;
    private static final List<BloodSugarChangeListener> listeners = new ArrayList<>();

    // 평균 혈당량 입력
    public static double getBloodSugarAVG() {
        return bloodSugarAVG;
    }
    public static double getBloodSugarRecent(){return bloodSugarRecent;}

    // 평균 혈당량 설정 및 리스너 호출
    public static void setBloodSugarAVG(double bloodSugarAVG) {
        HealthSet.bloodSugarAVG = bloodSugarAVG;
        notifyListeners(); // 리스너-변경 사항 알림
    }
    public static void setBloodSugarRecent(double bloodSugarRecent){HealthSet.bloodSugarRecent=bloodSugarRecent;}

    // 리스너 등록
    public static void addBloodSugarChangeListener(BloodSugarChangeListener listener) {
        listeners.add(listener);
    }

    // 리스너 제거
    public static void removeBloodSugarChangeListener(BloodSugarChangeListener listener) {
        listeners.remove(listener);
    }

    // 알림-모든 리스너
    private static void notifyListeners() {
        for (BloodSugarChangeListener listener : listeners) {
            listener.onBloodSugarChanged();
        }
    }

    // 혈당 변화 리스너 인터페이스
    public interface BloodSugarChangeListener {
        void onBloodSugarChanged();
    }
}
