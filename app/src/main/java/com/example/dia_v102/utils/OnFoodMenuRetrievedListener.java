package com.example.dia_v102.utils;

import com.example.dia_v102.entities.Food_menu;

public interface OnFoodMenuRetrievedListener {
    void onFoodMenuRetrieved(Food_menu foodMenu);
    void onFoodMenuNotFound();
}