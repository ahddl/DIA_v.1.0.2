package com.example.dia_v102;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dia_v102.databaseF.Func_UserInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.File;
import java.io.IOException;


public class MainActivity2 extends AppCompatActivity {

    /*//FragmentDiet.java로 이동 필요
    private FirebaseAuth mAuth;
    private String userNick;

    TextView nickView;*/

    /*navigation 하단 탭 생성(4개)*/

    BottomNavigationView bottom_navigation;

    FragmentDiet diet;
    FragmentDiabetes diabetes;
    FragmentChatbot chatbot;
    FragmentGraph graph;

    //카메라 및 갤러리 열기 (음식인식 위한)
    File file;
    Uri uri;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickGalleryLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottom_navigation = findViewById(R.id.bottom_navigation);

        diet = new FragmentDiet();
        diabetes = new FragmentDiabetes();
        chatbot = new FragmentChatbot();
        graph = new FragmentGraph();

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, diet).commit();

        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.tabdiet) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, diet).commit();
                    return true;
                } else if (itemId == R.id.tabdiabetes) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, diabetes).commit();
                    return true;
                } else if (itemId == R.id.tabchatbot) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, chatbot).commit();
                    return true;
                } else if (itemId == R.id.tabgraph) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, graph).commit();
                    return true;
                } else {
                    return false;
                }


            }
        });

    // ActivityResultLauncher 초기화
    takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == RESULT_OK) {
            startMainActivity2(uri);
        } else {
            Toast.makeText(this, "사진을 찍지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }
        );

    pickGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri selectedImageUri = result.getData().getData();
            if (selectedImageUri != null) {
                startMainActivity2(selectedImageUri);
            }
        } else {
            Toast.makeText(this, "사진을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }
        );

    FloatingActionButton enterDietButton = findViewById(R.id.enterDiet);
        enterDietButton.setOnClickListener(v -> showImageSourceDialog());

    // 이미지 소스 대화상자 표시 처리
        if (getIntent().getBooleanExtra("showImageSourceDialog", false)) {
        showImageSourceDialog();
    }
}


    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지 소스 선택")
                .setItems(new String[]{"카메라", "갤러리"}, (dialog, which) -> {
                    if (which == 0) {
                        takePicture();
                    } else {
                        openGallery();
                    }
                });
        builder.create().show();
    }

    private void takePicture() {
        try {
            file = createFile();
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        takePictureLauncher.launch(intent);
    }

    private File createFile() {
        String filename = "capture.jpg";
        File outFile = new File(getFilesDir(), filename);
        Log.d("Main", "파일 경로 : " + outFile.getAbsolutePath());
        return outFile;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickGalleryLauncher.launch(intent);
    }

    private void startMainActivity2(Uri imageUri) {
        Intent intent2 = new Intent(this, DietCheckMenu.class);
        intent2.putExtra("imageUri", imageUri.toString());
        startActivity(intent2);
    }


}