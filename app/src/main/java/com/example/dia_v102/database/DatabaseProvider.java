package com.example.dia_v102.database;

import android.content.Context;
import androidx.room.Room;

//import com.example.dia_v102.dao.DietImageDao;
import com.example.dia_v102.dao.Food_menuDao; // 추가된 부분
//import com.example.dia_v102.entities.DietImage;
import com.example.dia_v102.entities.Food_menu; // 추가된 부분

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseProvider {
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration() // Optional: If you need to handle migrations
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    // CSV 파일을 파싱하여 데이터를 DB에 삽입하는 메서드
    public static void parseCsvAndInsertToDB(Context context, int csvResourceId) {
        AppDatabase db = getDatabase(context);
        Food_menuDao foodMenuDao = db.food_menuDao();

        try {
            InputStream inputStream = context.getResources().openRawResource(csvResourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // Skip header line
            reader.readLine();

            List<Food_menu> foodMenuList = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                Food_menu foodMenu = new Food_menu();
                foodMenu.setFood(tokens[0]);
                foodMenu.setCalories(Double.parseDouble(tokens[1]));
                foodMenu.setCarbohydrate(Double.parseDouble(tokens[2]));
                foodMenu.setProtein(Double.parseDouble(tokens[3]));
                foodMenu.setFat(Double.parseDouble(tokens[4]));
                foodMenu.setCholesterol(Double.parseDouble(tokens[5]));
                foodMenu.setSodium(Double.parseDouble(tokens[6]));
                foodMenu.setQuantity(Integer.parseInt(tokens[7]));

                foodMenuList.add(foodMenu);
            }
            reader.close();

            // 데이터베이스에 삽입
            if (!foodMenuList.isEmpty()) {
                foodMenuDao.insertAll(foodMenuList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
