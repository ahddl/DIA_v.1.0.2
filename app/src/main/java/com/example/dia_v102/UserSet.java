package com.example.dia_v102;

import android.app.Application;

public class UserSet extends Application {
    private static String userID;
    private static String nickname;

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
}
