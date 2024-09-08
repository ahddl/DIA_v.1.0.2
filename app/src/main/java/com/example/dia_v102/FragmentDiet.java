package com.example.dia_v102;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

public class FragmentDiet extends Fragment {


  /*  //카메라 및 갤러리 열기 (음식인식 위한)
    File file;
    Uri uri;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickGalleryLauncher;*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);


        TextView dateText = view.findViewById(R.id.diet_date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        // 현재 날짜를 기본값으로 설정
        SpannableString spannableString = new SpannableString(currentDate);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateText.setText(spannableString);

        ImageView datePickerButton = view.findViewById(R.id.diet_date_btn);
        datePickerButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year, month, dayOfMonth) -> {
                String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);

                // SpannableString을 사용하여 밑줄 추가
                SpannableString spannableString1 = new SpannableString(selectedDate);
                spannableString1.setSpan(new UnderlineSpan(), 0, spannableString1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                dateText.setText(spannableString1);
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });


         /*// 로딩화면: 사진 입력 후 모델 분석 때 시간 걸릴 때 사용하면 됨
        Button btnLoad = view.findViewById(R.id.btnload);
        customProgressDialog = new ProgressDialog(getContext());
        if (customProgressDialog.getWindow() != null) {
            customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        btnLoad.setOnClickListener(v -> customProgressDialog.show());*/


      /*  takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        startMainActivity2(uri);
                    } else {
                        Toast.makeText(getContext(), "사진을 찍지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        pickGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            startMainActivity2(selectedImageUri);
                        }
                    } else {
                        Toast.makeText(getContext(), "사진을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        FloatingActionButton enterDietButton = view.findViewById(R.id.enterDiet);
        enterDietButton.setOnClickListener(v -> showImageSourceDialog());

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.dietnFrame), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 재촬영 or 갤러리 사진 다시 불러오기
        if (getActivity().getIntent().getBooleanExtra("showImageSourceDialog", false)) {
            showImageSourceDialog();
        }

        return view;
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Image Source")
                .setItems(new String[]{"Camera", "Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        takePicture();
                    } else {
                        openGallery();
                    }
                });
        builder.create().show();
    }

    public void takePicture() {
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
            uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".fileprovider", file);
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
        File outFile = new File(getContext().getFilesDir(), filename);
        Log.d("Main", "File path : " + outFile.getAbsolutePath());

        return outFile;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickGalleryLauncher.launch(intent);
    }

    private void startMainActivity2(Uri imageUri) {
        Intent intent2 = new Intent(getContext(), DietCheckMenu.class);
        intent2.putExtra("imageUri", imageUri.toString());
        startActivity(intent2);
    }*/
        return view;
    }
}
