package com.example.dia_v102.databaseF;

// 사용자 데이터 모델
public class UserInfo {
    public boolean eMailSub;
    public String nick;

    public int height;
    public int weight;
    public int age;
    public String gender;//F/M
    public String type;//1,2,p,o

    // 기본 생성자
    public UserInfo() {}

    // parameter 받는 생성자
    public UserInfo(boolean eMailSub, String nick, int height, int weight, int age, char gender, char type) {
        this.eMailSub = eMailSub;
        this.nick = nick;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = String.valueOf(gender);
        this.type = String.valueOf(type);
    }
    public String getNick() {
        return nick;
    }
}