package com.example.dia_v102.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CameraGalleryPicker {

    private File file;
    private Uri uri;
    private final Context context;

    // 런처를 전달받기 위한 변수
    private final ActivityResultLauncher<Intent> takePictureLauncher;
    private final ActivityResultLauncher<Intent> pickGalleryLauncher;

    public CameraGalleryPicker(Context context,
                               ActivityResultLauncher<Intent> takePictureLauncher,
                               ActivityResultLauncher<Intent> pickGalleryLauncher) {
        this.context = context;
        this.takePictureLauncher = takePictureLauncher;
        this.pickGalleryLauncher = pickGalleryLauncher;
    }

    // 이미지 소스 선택 다이얼로그
    public void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("이미지 소스 선택")
                .setItems(new String[]{"카메라", "갤러리"}, (dialog, which) -> {
                    if (which == 0) {
                        takePicture(); // 카메라 열기
                    } else {
                        openGallery(); // 갤러리 열기
                    }
                });
        builder.create().show();
    }

    // 카메라 열기
    private void takePicture() {
        try {
            file = createFile();
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
        } catch (IOException e) {
            Log.d("CameraGalleryPicker", Objects.requireNonNull(e.getMessage()));
        }

        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        takePictureLauncher.launch(intent); // 카메라 런처 호출
    }

    // 갤러리 열기
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickGalleryLauncher.launch(intent); // 갤러리 런처 호출
    }

    // 파일 생성
    private File createFile() {
        String filename = "capture.jpg";
        File outFile = new File(context.getFilesDir(), filename);
        Log.d("CameraGalleryPicker", "파일 경로 : " + outFile.getAbsolutePath());
        return outFile;
    }

    // Uri 리턴
    public Uri getUri() {
        return uri;
    }
}
