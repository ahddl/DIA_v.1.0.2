package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DietUserInput extends AppCompatActivity {
    Button save;
    EditText foodName, foodCalories, foodCarbohydrate, foodProtein, foodFat, foodCholesterol, foodSodium, foodSugar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_user_food);

        save = findViewById(R.id.saveBtn);

        foodName = findViewById(R.id.food_name);
        foodCalories = findViewById(R.id.food_calories);
        foodCarbohydrate = findViewById(R.id.food_carbohydrate);
        foodProtein = findViewById(R.id.food_protein);
        foodFat = findViewById(R.id.food_fat);
        foodCholesterol = findViewById(R.id.food_cholesterol);
        foodSodium = findViewById(R.id.food_sodium);
        foodSugar = findViewById(R.id.food_sugar);

        save.setOnClickListener(v -> {
            // 입력된 값들을 가져옴
            String name = foodName.getText().toString();
            String calories = foodCalories.getText().toString();
            String carbohydrate = foodCarbohydrate.getText().toString();
            String protein = foodProtein.getText().toString();
            String fat = foodFat.getText().toString();
            String cholesterol = foodCholesterol.getText().toString();
            String sodium = foodSodium.getText().toString();
            String sugar = foodSugar.getText().toString();

            // 모든 입력값 확인
            if (name.isEmpty() || calories.isEmpty() || carbohydrate.isEmpty() || protein.isEmpty() ||
                    fat.isEmpty() || cholesterol.isEmpty() || sodium.isEmpty() || sugar.isEmpty()) {
                // 비어있다면 경고 메시지
                Toast.makeText(DietUserInput.this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 입력값 모두 존재 => /로 연결된 문자열로 만든다
                String foodInfoString = name + "/" + calories + "/" + carbohydrate + "/" + protein + "/" +
                        fat + "/" + cholesterol + "/" + sodium + "/" + sugar;
                Log.d("whyNull", foodInfoString);
                // 인텐트로 데이터 전달
                Intent intent = new Intent(DietUserInput.this, DietOutputNutrient.class);
                intent.putExtra("userInput", foodInfoString);
                startActivity(intent);
            }
        });
    }
}
