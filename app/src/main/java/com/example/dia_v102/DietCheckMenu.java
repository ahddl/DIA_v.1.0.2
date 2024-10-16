package com.example.dia_v102;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.dia_v102.utils.imgUtil;


public class DietCheckMenu extends AppCompatActivity {

    private static final String TAG = "CheckMenu";
    ImageView imageView1;
    Button outputOk, outputNo;
    ImageButton upBtn, downBtn;
    TextView outputMenu, dateTime, quantity;

    Interpreter tflite;
    List<String> labels;
    ExecutorService executorService;
    int num_quan = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.diet_check_menu);

        imageView1 = findViewById(R.id.imageView1);
        outputOk = findViewById(R.id.outputOk);
        outputNo = findViewById(R.id.outputNo);
        outputMenu = findViewById(R.id.outputmenu);
        dateTime = findViewById(R.id.dateTime);

        upBtn = findViewById(R.id.up);
        downBtn = findViewById(R.id.down);
        quantity = findViewById(R.id.quantity);

        executorService = Executors.newSingleThreadExecutor();

        try {
            labels = loadLabels();
        } catch (IOException e) {
            Log.e(TAG, "Error loading labels", e);
        }

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView1.setImageURI(imageUri);

            loadModelAndRecognize(imageUri);
        }

        setDateTime();
        quantity.setText(num_quan + " 인분");

        upBtn.setOnClickListener(v->{
            if(num_quan<=7){
                num_quan+=1;
                quantity.setText(num_quan + " 인분");
            }
        });
        downBtn.setOnClickListener(v->{
            if(num_quan>1){
                num_quan-=1;
                quantity.setText(num_quan + " 인분");
            }
        });

        outputOk.setOnClickListener(v -> {
            String MenuString = outputMenu.getText().toString();
            Intent intent = new Intent(DietCheckMenu.this, DietOutputNutrient.class);
            intent.putExtra("outputMenu", MenuString);
            intent.putExtra("quantity", num_quan);
            intent.putExtra("ImgUriStr", imageUriString);
            startActivity(intent);
            if (isBloodSugarAlarmAgreed()) {  // 혈당 알람 동의 여부 확인
                Alarm.setBloodSugarAlarm(this, 10);  // 10초 후 알람 설정
            }
        });

        outputNo.setOnClickListener(v -> {
            Intent intent2 = new Intent(DietCheckMenu.this, FragmentDiet.class);
            intent2.putExtra("showImageSourceDialog", true);
            startActivity(intent2);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private boolean isBloodSugarAlarmAgreed() {
        // SharedPreferences에서 혈당 알람 동의 여부 확인
        SharedPreferences sharedPreferences = getSharedPreferences("AlarmPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isBloodSugarAlarmAgreed", false);  // 기본값 false
    }

    private void loadModelAndRecognize(Uri imageUri) {
        executorService.execute(() -> {
            try {
                MappedByteBuffer tfliteModel = loadModelFile();
                Interpreter.Options options = new Interpreter.Options();
                tflite = new Interpreter(tfliteModel, options);
                Log.d(TAG, "Model loaded successfully");

                Bitmap bitmap = imgUtil.uriToBitmap(this, imageUri);
                if (bitmap != null) {
                    String recognizedFood = recognizeFood(bitmap);
                    runOnUiThread(() -> outputMenu.setText(recognizedFood));
                } else {
                    Log.d(TAG, "Bitmap is null");
                }
            } catch (IOException e) {
                Log.e(TAG, "Error loading model", e);
            }
        });
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        return FileUtil.loadMappedFile(this, "model.tflite");
    }

    private String recognizeFood(Bitmap bitmap) {
        ByteBuffer byteBuffer = preprocessImage(bitmap);
        float[][] result = new float[1][labels.size()];
        tflite.run(byteBuffer, result);

        int maxIndex = -1;
        float maxProb = -1;
        for (int i = 0; i < result[0].length; i++) {
            if (result[0][i] > maxProb) {
                maxProb = result[0][i];
                maxIndex = i;
            }
        }

        return maxIndex != -1 ? labels.get(maxIndex) : "Unknown";
    }

    private ByteBuffer preprocessImage(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[224 * 224];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < 224; ++i) {
            for (int j = 0; j < 224; ++j) {
                int val = intValues[pixel++];
                byteBuffer.putFloat(((val >> 16) & 0xFF) / 255.0f);
                byteBuffer.putFloat(((val >> 8) & 0xFF) / 255.0f);
                byteBuffer.putFloat((val & 0xFF) / 255.0f);
            }
        }
        return byteBuffer;
    }

    private List<String> loadLabels() throws IOException {
        List<String> labels = new ArrayList<>();
        try (InputStream is = getAssets().open("labels.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                labels.add(line);
            }
        }
        return labels;
    }

    private void setDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시:mm분", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        dateTime.setText(currentDateTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
