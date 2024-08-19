package com.example.dia_v102.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.dia_v102.entities.DietImage;
import com.example.dia_v102.dao.DietImageDao;

@Database(entities = {DietImage.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DietImageDao dietImageDao();
}