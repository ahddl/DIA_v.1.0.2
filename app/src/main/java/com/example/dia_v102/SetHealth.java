package com.example.dia_v102;

import android.app.Application;
import java.util.ArrayList;
import java.util.List;

public class SetHealth extends Application {

    static double delta_bloodsugar_std=20;
    static double bloodSugarAVG;
    static double bloodSugarRecent;
    static double HbA1cAVG;
    private static final List<BloodSugarChangeListener> listeners = new ArrayList<>();
    private static final List<HbA1CChangeListener> listeners2 = new ArrayList<>();

    // 평균 혈당량 입력
    public static double getBloodSugarAVG() {
        return bloodSugarAVG;
    }
    public static double getBloodSugarRecent(){return bloodSugarRecent;}
    public static double getHbA1cAVG(){return HbA1cAVG;}

    // 평균 혈당량 설정 및 리스너 호출
    public static void setBloodSugarAVG(double bloodSugarAVG) {
        SetHealth.bloodSugarAVG = bloodSugarAVG;
        notifyListeners(); // 리스너-변경 사항 알림
    }

    public static void setHbA1cAVG(double HbA1cAVG){
        SetHealth.HbA1cAVG = HbA1cAVG;
        notifyListeners();
    }
    public static void setBloodSugarRecent(double bloodSugarRecent){
        SetHealth.bloodSugarRecent=bloodSugarRecent;}


    // 리스너 등록
    public static void addBloodSugarChangeListener(BloodSugarChangeListener listener) {
        listeners.add(listener);
    }
    public static void addHbA1cChangeListener(HbA1CChangeListener listener) {
        listeners2.add(listener);
    }

    // 리스너 제거
    public static void removeBloodSugarChangeListener(BloodSugarChangeListener listener) {
        listeners.remove(listener);
    }
    public static void removeHbA1CChangeListener(HbA1CChangeListener listener){
        listeners2.remove(listener);
    }

    public static void setDelta_bloodsugar_std(double val){
        delta_bloodsugar_std = val;
    }
    public static double getDelta_bloodsugar_std(){
        return  delta_bloodsugar_std;
    }

    // 알림-모든 리스너
    private static void notifyListeners() {
        for (BloodSugarChangeListener listener : listeners) {
            listener.onBloodSugarChanged();
        }
        for (HbA1CChangeListener listener : listeners2){
            listener.onHbA1CChanged();
        }
    }

    // 혈당 변화 리스너 인터페이스
    public interface BloodSugarChangeListener {
        void onBloodSugarChanged();
    }
    public interface HbA1CChangeListener{
        void onHbA1CChanged();
    }
}
