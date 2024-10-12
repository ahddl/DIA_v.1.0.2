package com.example.dia_v102.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.dia_v102.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;

public class imgUtil {
    public static void saveImage(Context context, Bitmap bitmap, String imageName) {
        FileOutputStream fos = null;
        try {
            // 내부 저장소에 파일 경로 설정
            fos = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            // 비트맵을 JPEG 형식으로 저장
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            Log.d("imgSave", Objects.requireNonNull(e.getMessage()));
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Log.d("imgSave", Objects.requireNonNull(e.getMessage()));
            }
        }
    }
    public static Bitmap loadImage(Context context, String imageName) {
        FileInputStream fis = null;
        try {
            // 내부 저장소에서 파일 경로 설정
            fis = context.openFileInput(imageName);
            // 파일에서 비트맵으로 변환
            return BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            Log.d("imgLoad", Objects.requireNonNull(e.getMessage()));
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                Log.d("imgLoad", Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    public static void setImage(Context context, ImageView imageView, String imageName) {
        Bitmap bitmap = loadImage(context, imageName);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            // 이미지가 없을 경우에 대한 처리
            imageView.setImageResource(R.drawable.placeholder); // 대체 이미지 설정
        }
    }

    public static String RandomString(int len){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < len; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomString.toString();
    }

    public static Bitmap uriToBitmap(Context context, Uri uri) {
        String TAG = "uriToBit";
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null) {
                Log.d(TAG, "Bitmap loaded successfully");
                return Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            } else {
                Log.d(TAG, "Bitmap is null");
                return null;
            }
        } catch (IOException e) {
            Log.d("url2Bit", Objects.requireNonNull(e.getMessage()));
            Log.e(TAG, "Error converting URI to Bitmap", e);
            return null;
        }
    }
}
