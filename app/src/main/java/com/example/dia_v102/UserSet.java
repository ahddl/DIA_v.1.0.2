package com.example.dia_v102;

import android.app.Application;

import com.example.dia_v102.databaseF.Func_UserInfo;
import com.example.dia_v102.databaseF.UserInfo;

public class UserSet extends Application {
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
    public static void setEmail(String email){UserSet.email = email;}
    public static String getPW(){return pw;}
    public static void setPW(String pw){UserSet.pw = pw;}
    public static void loginComplete() {UserSet.email = ""; UserSet.pw = "";}


    public static String getUserId() {
        return userID;
    }
    public static void setUserId(String userId) {
        UserSet.userID = userId;
    }
    public static String getNickname() {
        return nickname;
    }
    public static void setNickname(String nickname) {
        UserSet.nickname = nickname;
    }

    public static boolean getESub(){return eSub;}
    public static void setESub(boolean eSub){UserSet.eSub = eSub;}
    public static char getType() {return type;}
    public static String getTypeStr() {
        if(type == '1' || type == '2'){return type+"형";}
        else if(type=='p'){return "임신성 당뇨";}
        else if(type=='o'){return "기타";}
        else return "";
    }
    public static void setType(char type){UserSet.type = type;}
    public static char getGender() {return gender;}
    public static void setGender(char gender){UserSet.gender = gender;}
    public static int getHeight() {return height;}
    public static void setHeight(int height){UserSet.height = height;}
    public static int getAge() {return age;}
    public static void setAge(int age){UserSet.age = age;}
    public static int getWeight() {return weight;}
    public static void setWeight(int weight){UserSet.weight = weight;}

    public static void logOut(){
        UserSet.userID = "";
        UserSet.nickname = "";
        UserSet.age = -1;
        UserSet.height = -1;
        UserSet.weight = -1;
        UserSet.eSub = false;
        UserSet.gender = '\0';
        UserSet.type = '\0';
    }

    public static void saveUserSet(){
        UserInfo userinfo = new UserInfo(eSub, nickname, height, weight, age, gender, type);
        Func_UserInfo.saveUserInfo(userID, userinfo);
    }
}
