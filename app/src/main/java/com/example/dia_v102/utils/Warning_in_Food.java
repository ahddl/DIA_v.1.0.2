package com.example.dia_v102.utils;

import android.util.Log;

import com.example.dia_v102.databaseF.FoodCal;
import com.example.dia_v102.databaseF.Func_FoodCal;
import com.example.dia_v102.databaseF.Func_Report;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Warning_in_Food {
    public static void dangerCall(String tag, double bloodSugarValue) {
        String date = DateUtil.dateToString(new Date());
        Func_FoodCal foodCal = new Func_FoodCal();
        String searchTag = tag.substring(0, 2);

        foodCal.loadFoodCal(date, new Func_FoodCal.OnDataReceivedListener(){
            @Override
            public void onDataReceived(List<FoodCal> foodCalList){
                FoodCal data = null;
                //방법 1. 가장 뒤의 자료에 접근
                // data = foodCalList.get(foodCalList.size() - 1);
                //방법 2. tag 맞는거 가져옴.
                for(FoodCal item : foodCalList){
                    if(item.getTag().equals(searchTag)){data = item;}
                }
                if(data !=null){reportFood(data, bloodSugarValue);}
            }
            @Override
            public void onDataFailed(Exception exception){
                Log.d("foodDB", Objects.requireNonNull(exception.getMessage()));
            }
        });
    }

    private static void reportFood(FoodCal data, double bloodSugarValue) {
        Func_Report report = new Func_Report();
        String foodStr = data.getFood();
        String dateTag = data.getDate() + " " + data.getTag();

        String[] foodList=foodStr.split("/");
        for(String food:foodList){
            report.saveData(food, dateTag, bloodSugarValue);
        }
    }
}
