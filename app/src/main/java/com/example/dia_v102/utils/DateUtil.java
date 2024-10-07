package com.example.dia_v102.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    // 날짜를 "yyyyMMdd" 형식의 문자열로 변환
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()); // 원하는 형식으로 변경 가능
        return sdf.format(date);
    }

    // "yyyyMMdd" 형식의 문자열을 Date 객체로 변환
    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()); // 문자열 형식과 일치해야 함
        return sdf.parse(dateString);
    }

    // "yyyy-MM-dd" 형식의 문자열을 Date 객체로 변환
    public static Date stringToDate(String dateString, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.parse(dateString);
    }

    public static String HourNMin() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        return String.format(Locale.getDefault(), "%02d시 %02d분", hour, min);
    }
}
