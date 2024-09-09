package com.example.dia_v102;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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


public class DietCheckMenu extends AppCompatActivity {

    private static final String TAG = "CheckMenu";

    ImageView imageView1;
    Button outputOk;
    Button outputNo;
    TextView outputmenu;
    TextView dateTime;

    Interpreter tflite;
    List<String> labels;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.diet_check_menu);

        imageView1 = findViewById(R.id.imageView1);
        outputOk = findViewById(R.id.outputOk);
        outputNo = findViewById(R.id.outputNo);
        outputmenu = findViewById(R.id.outputmenu);
        dateTime = findViewById(R.id.dateTime);

        executorService = Executors.newSingleThreadExecutor();

        try {
            labels = loadLabels();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading labels", e);
        }

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView1.setImageURI(imageUri);

            loadModelAndRecognize(imageUri);
        }

        setDateTime();

        outputOk.setOnClickListener(v -> {
            String outputMenu = outputmenu.getText().toString();
            Intent intent = new Intent(DietCheckMenu.this, DietOutputNutient.class);
            intent.putExtra("outputMenu", outputMenu);
            startActivity(intent);
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

    private void loadModelAndRecognize(Uri imageUri) {
        executorService.execute(() -> {
            try {
                MappedByteBuffer tfliteModel = loadModelFile();
                Interpreter.Options options = new Interpreter.Options();
                tflite = new Interpreter(tfliteModel, options);
                Log.d(TAG, "Model loaded successfully");

                Bitmap bitmap = uriToBitmap(imageUri);
                if (bitmap != null) {
                    String recognizedFood = recognizeFood(bitmap);
                    runOnUiThread(() -> outputmenu.setText(recognizedFood));
                } else {
                    Log.d(TAG, "Bitmap is null");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error loading model", e);
            }
        });
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        return FileUtil.loadMappedFile(this, "model.tflite");
    }

    private Bitmap uriToBitmap(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap != null) {
                Log.d(TAG, "Bitmap loaded successfully");
                return Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            } else {
                Log.d(TAG, "Bitmap is null");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error converting URI to Bitmap", e);
            return null;
        }
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