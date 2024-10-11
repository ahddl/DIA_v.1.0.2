package com.example.dia_v102.databaseF;

import com.example.dia_v102.utils.DateUtil;

import java.util.Date;

public class FoodCal {
    private String date;
    private String time;

    //형태에 따라 빠질 수도 있을듯.
    private String food;
    private String tag;
    private double calories;
    private double carbohydrate;
    private double protein;
    private double fat;
    private double cholesterol;
    private double sodium;
    private double sugar;
    private String imgName;

    public FoodCal(){}

    public FoodCal(String food, String tag, double calories, double carbohydrate, double protein, double fat, double cholesterol, double sodium, double sugar, String imgName){
        date = DateUtil.dateToString(new Date());
        time = DateUtil.HourNMin();
        this.food = food;
        this.tag = tag;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.sugar = sugar;
        this.imgName =imgName;
    }
    // Getter/Setter 메서드
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime(){return time;}
    public void setTime(String time){this.time = time;}

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getTag() {return tag;}
    public void setTag(){this.tag = tag;}

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getSugar(){return sugar;}
    public void setSugar(double sugar){this.sugar = sugar;}

    public String getImgName(){return imgName;}
    void setImgName(String imgName){this.imgName = imgName;}
}
