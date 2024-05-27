package com.example.dia_v102;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckMenu extends AppCompatActivity {

    private static final String TAG = "CheckMenu";

    ImageView imageView1;
    Button outputOk;
    Button outputNo;
    TextView outputmenu;
    TextView dateTime;

    private Interpreter tflite;
    private List<String> labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_menu);

        // UI 요소 초기화
        imageView1 = findViewById(R.id.imageView1);
        outputOk = findViewById(R.id.outputOk);
        outputNo = findViewById(R.id.outputNo);
        outputmenu = findViewById(R.id.outputmenu);
        dateTime = findViewById(R.id.dateTime);

        // 모델 및 라벨 로드
        try {
            tflite = new Interpreter(loadModelFile());
            labels = FileUtil.loadLabels(this, "labels.txt");
            Log.d(TAG, "Model and labels loaded successfully");
        } catch (IOException e) {
            Log.e(TAG, "Error loading model or labels", e);
            Toast.makeText(this, "Error loading model or labels", Toast.LENGTH_LONG).show();
            tflite = null;
            return;
        }

        // 인텐트에서 이미지 URI 가져오기
        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            try {
                Uri imageUri = Uri.parse(imageUriString);
                imageView1.setImageURI(imageUri);

                // 이미지 비트맵으로 변환 및 모델을 통해 분류
                Bitmap bitmap = loadImage(imageUri);
                if (bitmap != null) {
                    classifyImage(bitmap);
                } else {
                    Log.e(TAG, "Failed to load bitmap from image URI");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error processing image URI: " + imageUriString, e);
            }
        } else {
            Log.e(TAG, "No image URI found in intent");
        }

        // 날짜 및 시간 설정
        setDateTime();

        // '확인' 버튼 클릭 리스너 설정
        outputOk.setOnClickListener(v -> {
            String outputMenu = outputmenu.getText().toString();
            Intent intent = new Intent(CheckMenu.this, OutputNutient.class);
            intent.putExtra("outputMenu", outputMenu);
            startActivity(intent);
        });

        // '다시 시도' 버튼 클릭 리스너 설정
        outputNo.setOnClickListener(v -> {
            Intent intent2 = new Intent(CheckMenu.this, Diet.class);
            intent2.putExtra("showImageSourceDialog", true);
            startActivity(intent2);
        });

        // 시스템 바 인셋 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 날짜 및 시간 설정
    private void setDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시:mm분", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        dateTime.setText(currentDateTime);
    }

    // TFLite 모델 파일 로드
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // 이미지 로드 및 비트맵으로 변환
    private Bitmap loadImage(Uri imageUri) {
        try {
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
        } catch (Exception e) {
            Log.e(TAG, "Error loading image", e);
            return null;
        }
    }

    // 소프트맥스 함수
    private float[] softmax(float[] logits) {
        float[] expValues = new float[logits.length];
        float sum = 0.0f;

        for (int i = 0; i < logits.length; i++) {
            expValues[i] = (float) Math.exp(logits[i]);
            sum += expValues[i];
        }

        for (int i = 0; i < logits.length; i++) {
            expValues[i] /= sum;
        }


        return expValues;
    }

    // 이미지 분류
    private void classifyImage(Bitmap bitmap) {
        if (tflite == null) {
            Log.e(TAG, "tflite is not initialized");
            return;
        }

        // 이미지 크기 조정 및 전처리
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
        int width = resizedBitmap.getWidth();
        int height = resizedBitmap.getHeight();
        int[] intValues = new int[width * height];
        resizedBitmap.getPixels(intValues, 0, width, 0, 0, width, height);

        // 이미지 데이터를 float 배열로 변환
        float[][][][] input = new float[1][3][128][128];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int pixel = intValues[i * width + j];
                input[0][0][i][j] = ((pixel >> 16) & 0xFF) / 255.0f;
                input[0][1][i][j] = ((pixel >> 8) & 0xFF) / 255.0f;
                input[0][2][i][j] = (pixel & 0xFF) / 255.0f;
            }
        }

        // 모델 예측
        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(new int[]{1, 15}, DataType.FLOAT32);
        tflite.run(input, outputBuffer.getBuffer().rewind());

        // 소프트맥스 함수 적용
        float[] probabilities = softmax(outputBuffer.getFloatArray());

        // 결과 처리
        int maxIndex = -1;
        float maxProb = -1;

        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > maxProb) {
                maxProb = probabilities[i];
                maxIndex = i;
            }
        }

        String result = labels.get(maxIndex);
        Toast.makeText(this, "Prediction: " + result, Toast.LENGTH_LONG).show();

        // 결과를 TextView에 표시
        outputmenu.setText(result);
    }
}