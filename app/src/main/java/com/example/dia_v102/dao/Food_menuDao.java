package com.example.dia_v102.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dia_v102.entities.Food_menu;

import java.util.List;
@Dao
public interface Food_menuDao {
    @Insert
    void insert(Food_menu foodMenu);
    @Insert
    void insertAll(List<Food_menu> foodMenus);
    @Update
    void update(Food_menu foodMenu);

    @Query("SELECT * FROM Food_menu")
    List<Food_menu> getAll();


    // 특정 음식 이름을 기반으로 데이터 조회
    @Query("SELECT * FROM Food_menu WHERE food = :foodName")
    List<Food_menu> getFoodByName(String foodName);
    // 필요한 다른 데이터베이스 작업도 정의할 수 있습니다.
}
