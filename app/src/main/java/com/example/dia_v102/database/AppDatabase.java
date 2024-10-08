package com.example.dia_v102.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dia_v102.entities.Food_menu;
import com.example.dia_v102.dao.Food_menuDao;

@Database(entities = {Food_menu.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract Food_menuDao food_menuDao();
}