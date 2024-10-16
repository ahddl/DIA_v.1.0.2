package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dia_v102.utils.FileUtil;

public class InputTerms extends AppCompatActivity {

    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_term);
        TextView term = findViewById(R.id.tv_terms_content);
        String termString = FileUtil.readFromAssets(this, "terms.txt");
        Button btnBack = findViewById(R.id.backBtn);
        btnNext = findViewById(R.id.nextBtn);
        CheckBox cbAgree = findViewById(R.id.cb_agree);
        term.setText(termString);
        // '이전' 버튼 클릭 시 처음으로 이동
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(InputTerms.this,MainActivity.class);
            startActivity(intent);
            finish();
        });

        // check-box 상태에 따라 '다음' 버튼 활성화
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> btnNext.setEnabled(isChecked));

        // '다음' 버튼 클릭 시의 동작 (예: 회원가입 다음 단계로 이동)
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(InputTerms.this, InputSignup.class);
            startActivity(intent);
        });
    }
}
