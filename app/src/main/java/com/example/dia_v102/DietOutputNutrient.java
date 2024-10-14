package com.example.dia_v102;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dia_v102.database.AppDatabase;
import com.example.dia_v102.database.DatabaseProvider;
import com.example.dia_v102.entities.Food_menu;
import com.example.dia_v102.utils.CameraGalleryPicker;
import com.example.dia_v102.utils.imgUtil;

import java.util.List;

import com.example.dia_v102.databaseF.Func_FoodCal;

public class DietOutputNutrient extends AppCompatActivity {

    TextView outputMenu1, nutList;
    Func_FoodCal foodCal = new Func_FoodCal();
    String imgUriStr, imgName, userInput;
    Spinner dropdownMenu;
    static Food_menu sumMenus = new Food_menu();
    static String imgNameStr="";
    int quantity;

    //카메라 관련
    CameraGalleryPicker imgPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.diet_output_nutient);

        outputMenu1 = findViewById(R.id.outputmenu1);
        nutList = findViewById(R.id.nutList);

        dropdownMenu = findViewById(R.id.dropdown_menu);

        cameraInput();

        imgUriStr = getIntent().getStringExtra("ImgUriStr");
        if (imgUriStr != null){
            Uri imageUri = Uri.parse(imgUriStr);
            Bitmap imgBit = imgUtil.uriToBitmap(this, imageUri);
            imgName = imgUtil.RandomString(8);
            imgUtil.saveImage(this, imgBit, imgName);
        }

        quantity = getIntent().getIntExtra("quantity", 1);
        // 앞에서 받아온 메뉴 이름 값 출력
        String outputMenu = getIntent().getStringExtra("outputMenu");
        if (outputMenu != null) {
            outputMenu1.setText(outputMenu);
            updateNutritionalInfo(outputMenu);
        }

        userInput = getIntent().getStringExtra("userInput");
        if(userInput != null){
            String[] menuInfo = userInput.split("/");
            Food_menu menu = new Food_menu();
            menu.addInfo(menuInfo[0], Double.parseDouble(menuInfo[1]), Double.parseDouble(menuInfo[2]), Double.parseDouble(menuInfo[3]), Double.parseDouble(menuInfo[4]), Double.parseDouble(menuInfo[5]), Double.parseDouble(menuInfo[6]), Double.parseDouble(menuInfo[7]));
            imgName="_";
            updateUI(menu);
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
            public void onFoodMenuRetrieved(Food_menu food) {
                updateUI(food);
                // 검색된 경우 UI 업데이트
            }

            @Override
            public void onFoodMenuNotFound() {
                runOnUiThread(() -> Toast.makeText(DietOutputNutrient.this, "해당 메뉴를 찾을 수 없습니다.", Toast.LENGTH_LONG).show());
            }
        });
    }

    public void updateUI(Food_menu foodMenu){


        String nutriaText = "음식 이름: " + foodMenu.getFood() +
                "\n 칼로리: " + foodMenu.getCalories()*quantity + " kcal"+
                "\n 탄수화물: " + foodMenu.getCarbohydrate()*quantity + " g" +
                "\n 단백질: " + foodMenu.getProtein()*quantity + " g" +
                "\n 지방: " + foodMenu.getFat()*quantity + " g" +
                "\n 콜레스테롤: " + foodMenu.getCholesterol()*quantity + " mg" +
                "\n 나트륨: " + foodMenu.getSodium()*quantity + " mg" +
                "\n 설탕 당: " + foodMenu.getSugar()*quantity + " g";
        nutList.setText(nutriaText);

                /*프로그래스 바 및 칼로리 텍스트 업데이트
                double totalCalories = totalCalories + foodMenu.getCalories();
                float averageDailyCalories = 2000f;
                caloriesTextView.setText("Calories: " + totalCalories + " / " + averageDailyCalories);
                caloriesProgressBar.setProgress((int) (totalCalories / averageDailyCalories * 100));

                 */
        // saveButton 클릭 시 동작 추가
        Button addButton = findViewById(R.id.re_input);
        addButton.setOnClickListener(v -> {
            sumMenus.addInfo(foodMenu.getFood(), foodMenu.getCalories()*quantity, foodMenu.getCarbohydrate()*quantity, foodMenu.getProtein()*quantity, foodMenu.getFat()*quantity, foodMenu.getCholesterol()*quantity, foodMenu.getSodium()*quantity, foodMenu.getSugar()*quantity);
            imgNameStr += imgNameStr.isEmpty()?(imgName):"/"+(imgName);
            imgPicker.showImageSourceDialog();
        });

        Button showSave = findViewById(R.id.select_tag);
        LinearLayout hiddenSave = findViewById(R.id.hiddenSave);
        showSave.setOnClickListener(v->{
            if(hiddenSave.getVisibility() == View.GONE){hiddenSave.setVisibility(View.VISIBLE);}
            else {hiddenSave.setVisibility(View.GONE);}
        });

        Button saveButton = findViewById(R.id.exit_save);
        saveButton.setOnClickListener(v -> {
            sumMenus.addInfo(foodMenu.getFood(), foodMenu.getCalories()*quantity, foodMenu.getCarbohydrate()*quantity, foodMenu.getProtein()*quantity, foodMenu.getFat()*quantity, foodMenu.getCholesterol()*quantity, foodMenu.getSodium()*quantity, foodMenu.getSugar()*quantity);
            imgNameStr += imgNameStr.isEmpty()?(imgName):"/"+(imgName);
            String tag = dropdownMenu.getSelectedItem().toString();
            foodCal.saveFoodCal(sumMenus.getFood(), tag, sumMenus.getCalories(), sumMenus.getCarbohydrate(), sumMenus.getProtein(), sumMenus.getFat(), sumMenus.getCholesterol(), sumMenus.getSodium(), sumMenus.getSugar(), imgNameStr);
            //리셋.
            sumMenus = new Food_menu();
            imgNameStr = "";

            Toast.makeText(DietOutputNutrient.this, "식단이 저장되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DietOutputNutrient.this, MainActivity2.class);
            startActivity(intent);
        });
    }

    public void fetchFoodMenu(String menu, OnFoodMenuRetrievedListener listener) {
        AppDatabase db = DatabaseProvider.getDatabase(this);
        new Thread(() -> {
            // Food_menuDao 객체를 통해 메뉴 정보 가져오기
            List<Food_menu> foodMenuList = db.food_menuDao().getFoodByName(menu);

            // 검색된 결과가 있는지 확인
            if (!foodMenuList.isEmpty()) {
                Food_menu food = foodMenuList.get(0);
                runOnUiThread(() -> listener.onFoodMenuRetrieved(food));
            } else {
                runOnUiThread(listener::onFoodMenuNotFound);
            }
        }).start();
    }

    public interface OnFoodMenuRetrievedListener {
        void onFoodMenuRetrieved(Food_menu foodMenu);
        void onFoodMenuNotFound();
    }

    private void cameraInput() {
        // ActivityResultLauncher 초기화
        ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri uri = imgPicker.getUri();
                        startAct(uri);
                    } else {
                        Toast.makeText(this, "사진을 촬영하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        ActivityResultLauncher<Intent> pickGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            startAct(selectedImageUri);
                        }
                    } else {
                        Toast.makeText(this, "사진을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imgPicker = new CameraGalleryPicker(this, takePictureLauncher, pickGalleryLauncher);
    }

    private void startAct(Uri imageUri) {
        Intent intent = new Intent(this, DietCheckMenu.class);
        intent.putExtra("imageUri", imageUri.toString());
        startActivity(intent);
    }
}