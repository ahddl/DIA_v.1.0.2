package com.example.dia_v102.databaseF;

import java.util.Date;

public class InfoBox {
    //private String userID;
    private String date;
    private String time;
    private String tag1;
    private String tag2;
    private double value;
    // parameter 받는 생성자
    public InfoBox() {}
    public InfoBox(String date, String time, String tag1, String tag2, double value) {
        this.date = date;
        this.time = time;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.value = value;
    }
    // 기본 생성자


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }
    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}