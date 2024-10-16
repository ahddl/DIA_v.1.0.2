package com.example.dia_v102;

import android.app.Application;

import com.example.dia_v102.databaseF.Func_UserInfo;
import com.example.dia_v102.databaseF.UserInfo;

public class SetUser extends Application {
    private static String userID;
    private static String nickname;
    private static boolean eSub;
    private static String email;
    private static String pw;
    static int height;
    static int weight;
    static int age;
    static char gender;//F/M
    static char type;//1,2,p,o

    public static String getEmail(){return email;}
    public static void setEmail(String email){
        SetUser.email = email;}
    public static String getPW(){return pw;}
    public static void setPW(String pw){
        SetUser.pw = pw;}
    public static void loginComplete(String UserID) {
        SetUser.userID = UserID;
        SetUser.email = ""; SetUser.pw = "";
        UserInfo userinfo = new UserInfo(eSub, nickname, height, weight, age, gender, type);
        Func_UserInfo.saveUserInfo(userID, userinfo);
    }


    public static String getUserId() {
        return userID;
    }
    public static void setUserId(String userId) {
        SetUser.userID = userId;
    }
    public static String getNickname() {
        return nickname;
    }
    public static void setNickname(String nickname) {
        SetUser.nickname = nickname;
    }

    public static boolean getESub(){return eSub;}
    public static void setESub(boolean eSub){
        SetUser.eSub = eSub;}
    public static char getType() {return type;}
    public static String getTypeStr() {
        if(type == '1' || type == '2'){return type+"형";}
        else if(type=='p'){return "임신성 당뇨";}
        else if(type=='o'){return "기타";}
        else return "";
    }
    public static void setType(char type){
        SetUser.type = type;}
    public static char getGender() {return gender;}
    public static void setGender(char gender){
        SetUser.gender = gender;}
    public static int getHeight() {return height;}
    public static void setHeight(int height){
        SetUser.height = height;}
    public static int getAge() {return age;}
    public static void setAge(int age){
        SetUser.age = age;}
    public static int getWeight() {return weight;}
    public static void setWeight(int weight){
        SetUser.weight = weight;}

    public static void logOut(){
        SetUser.userID = "";
        SetUser.nickname = "";
        SetUser.age = -1;
        SetUser.height = -1;
        SetUser.weight = -1;
        SetUser.eSub = false;
        SetUser.gender = '\0';
        SetUser.type = '\0';
    }
    public static void saveUserSet(){
        UserInfo userinfo = new UserInfo(eSub, nickname, height, weight, age, gender, type);
        Func_UserInfo.saveUserInfo(userID, userinfo);
    }

}
