package com.example.dia_v102;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dia_v102.database.AppDatabase;
import com.example.dia_v102.database.DatabaseProvider;
import com.example.dia_v102.entities.Food_menu;
import com.example.dia_v102.utils.OnFoodMenuRetrievedListener;
import com.example.dia_v102.utils.imgUtil;

import java.util.List;

import com.example.dia_v102.databaseF.Func_FoodCal;

public class DietOutputNutient extends AppCompatActivity {

    TextView outputmenu1;
    TextView nutList;
    TextView caloriesTextView;
    ProgressBar caloriesProgressBar;

    Func_FoodCal foodcal = new Func_FoodCal();

    String imgUriStr;
    String imgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.diet_output_nutient);

        outputmenu1 = findViewById(R.id.outputmenu1);
        //pieChart = findViewById(R.id.pieChart);
        nutList = findViewById(R.id.nutList);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        caloriesProgressBar = findViewById(R.id.caloriesProgressBar);

        imgUriStr = getIntent().getStringExtra("ImgUriStr");
        Uri imageUri = Uri.parse(imgUriStr);
        Bitmap imgBit = imgUtil.uriToBitmap(this, imageUri);
        imgName = imgUtil.RandomString(12);
        imgUtil.saveImage(this, imgBit, imgName);

        // 앞에서 받아온 메뉴 이름 값 출력
        String outputMenu = getIntent().getStringExtra("outputMenu");
        if (outputMenu != null) {
            outputmenu1.setText(outputMenu);
            updateNutritionalInfo(outputMenu);
        }

        // 시스템 바 인셋 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateNutritionalInfo(String menu) {
        fetchFoodMenu(menu, new OnFoodMenuRetrievedListener() {
            @Override
            public void onFoodMenuRetrieved(Food_menu foodMenu) {
                // 데이터가 검색된 경우 UI 업데이트
                String nutriateText = "음식 이름: " + foodMenu.getFood() +
                        "\n 칼로리: " + foodMenu.getCalories() + " kcal"+
                        "\n 탄수화물: " + foodMenu.getCarbohydrate() + " g" +
                        "\n 단백질: " + foodMenu.getProtein() + " g" +
                        "\n 지방: " + foodMenu.getFat() + " g" +
                        "\n 콜레스테롤: " + foodMenu.getCholesterol() + " mg" +
                        "\n 나트륨: " + foodMenu.getSodium() + " mg" +
                        "\n 설탕 당: " + foodMenu.getSugar() + " g";
                nutList.setText(nutriateText);

                /*프로그래스 바 및 칼로리 텍스트 업데이트
                double totalCalories = totalCalories + foodMenu.getCalories();
                float averageDailyCalories = 2000f;
                caloriesTextView.setText("Calories: " + totalCalories + " / " + averageDailyCalories);
                caloriesProgressBar.setProgress((int) (totalCalories / averageDailyCalories * 100));

                 */
                // saveButton 클릭 시 동작 추가
                Button saveButton = findViewById(R.id.save_diet);
                saveButton.setOnClickListener(v -> {
                    foodcal.saveFoodCal(UserSet.getUserId(), foodMenu.getFood(), foodMenu.getCalories(), foodMenu.getCarbohydrate(), foodMenu.getProtein(), foodMenu.getFat(), foodMenu.getCholesterol(), foodMenu.getSodium(), foodMenu.getSugar(), imgName);
                    Toast.makeText(DietOutputNutient.this, "식단이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DietOutputNutient.this, MainActivity2.class);
                    startActivity(intent);
                });

            }

            @Override
            public void onFoodMenuNotFound() {
                // 데이터가 없을 때
                runOnUiThread(() -> Toast.makeText(DietOutputNutient.this, "해당 메뉴를 찾을 수 없습니다.", Toast.LENGTH_LONG).show());
            }
        });
    }

    public void fetchFoodMenu(String menu, OnFoodMenuRetrievedListener listener) {
        AppDatabase db = DatabaseProvider.getDatabase(this);
        new Thread(() -> {
            // Food_menuDao 객체를 통해 메뉴 정보 가져오기
            List<Food_menu> foodMenuList = db.food_menuDao().getFoodByName(menu);

            // 검색된 결과가 있는지 확인
            if (!foodMenuList.isEmpty()) {
                // UI 스레드에서 실행
                Food_menu food = foodMenuList.get(0);
                runOnUiThread(() -> listener.onFoodMenuRetrieved(food));
            } else {
                // UI 스레드에서 실행
                runOnUiThread(listener::onFoodMenuNotFound);
            }
        }).start();
    }
}