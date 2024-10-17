package com.example.dia_v102.databaseF;

public class Report {
    private String food;
    private String tag;
    private double bloodSugarValue;

    public Report(){}

    public Report(String food, String tag, double bloodSugarValue){
        this.food = food;
        this.tag = tag;
        this.bloodSugarValue = bloodSugarValue;
    }

    public String getFood(){return food;}
    public String getTag(){return tag;}
    public double getBloodSugarValue(){return bloodSugarValue;}
}
