package com.example.dia_v102.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class FileUtil {

    // assets 디렉토리에서 파일을 읽는 메서드
    public static String readFromAssets(Context context, String fileName) {
        StringBuilder result = new StringBuilder();
        try {
            // assets에서 파일을 가져옴
            InputStream is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            Log.d("FileUtil", Objects.requireNonNull(e.getMessage()));
        }
        return result.toString();
    }
}
