package com.example.dia_v102;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dia_v102.databaseF.FoodCal;
import com.example.dia_v102.databaseF.Func_FoodCal;
import com.example.dia_v102.utils.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;


public class DangerFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger);
        String date = DateUtil.dateToString(new Date());
        Func_FoodCal foodCal = new Func_FoodCal();
        TextView foodText = findViewById(R.id.food_lists);
        String searchTag = getIntent().getStringExtra("TAG");

        foodCal.loadFoodCal(date, new Func_FoodCal.OnDataReceivedListener(){
            @Override
            public void onDataReceived(List<FoodCal> foodCalList){
                FoodCal data;
                //방법 1. 가장 뒤의 자료에 접근
                data = foodCalList.get(foodCalList.size() - 1);
                //방법 2. tag 맞는거 가져옴.
                for(FoodCal item : foodCalList){
                    if(item.getTag().equals(searchTag)){data = item;}
                }
                String[] foods = data.getFood().split("/");
                StringBuilder ViewFood = new StringBuilder();
                for(String food : foods){
                    ViewFood.append(food).append("\n");
                }
                foodText.setText(ViewFood);
            }
            @Override
            public void onDataFailed(Exception exception){
                Log.d("foodDB", Objects.requireNonNull(exception.getMessage()));
            }
        });
    }



}