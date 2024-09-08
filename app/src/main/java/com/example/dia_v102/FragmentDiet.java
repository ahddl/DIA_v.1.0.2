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

        





        return view;
    }
}
