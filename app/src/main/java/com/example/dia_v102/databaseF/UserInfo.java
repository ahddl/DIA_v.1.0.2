package com.example.dia_v102.databaseF;

// 사용자 데이터 모델
public class UserInfo {
    public String userID;
    public boolean eMailSub;
    public String nick;

    // 기본 생성자
    public UserInfo() {}

    // parameter 받는 생성자
    public UserInfo(String userID, boolean eMailSub, String nick) {
        this.userID = userID;
        this.eMailSub = eMailSub;
        this.nick = nick;
    }
    public String getNick() {
        return nick;
    }
}