package com.example.dia_v102;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckMenu extends AppCompatActivity {

    ImageView imageView1;
    Button outputOk;
    Button outputNo;
    TextView outputmenu;

    TextView dateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_menu);

        imageView1 = findViewById(R.id.imageView1);
        outputOk = findViewById(R.id.outputOk);
        outputNo = findViewById(R.id.outputNo);
        outputmenu = findViewById(R.id.outputmenu);
        dateTime = findViewById(R.id.dateTime);

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView1.setImageURI(imageUri);
        }

        //날짜 및 시간 출력
        setDateTime();

        //사진을 통해 인식한 음식 이름 출력하기

        outputOk.setOnClickListener(v -> {
            String outputMenu = outputmenu.getText().toString();
            Intent intent = new Intent(CheckMenu.this, OutputNutient.class);
            intent.putExtra("outputMenu", outputMenu);
            startActivity(intent);
        });

        outputNo.setOnClickListener(v -> {
            Intent intent2 = new Intent(CheckMenu.this, Diet.class);
            intent2.putExtra("showImageSourceDialog", true);
            startActivity(intent2);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시:mm분", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        dateTime.setText(currentDateTime);
    }
}