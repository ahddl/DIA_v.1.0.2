package com.example.dia_v102.utils;

public class Message {
    public static final int USER_TYPE = 0;
    public static final int BOT_TYPE = 1;

    private final String text;
    private final int type;

    public Message(String text, int type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }
}
