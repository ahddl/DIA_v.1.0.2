package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class TermsActivity extends AppCompatActivity {

    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        Button btnBack = findViewById(R.id.backBtn);
        btnNext = findViewById(R.id.nextBtn);
        CheckBox cbAgree = findViewById(R.id.cb_agree);

        // '이전' 버튼 클릭 시 처음으로 이동
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TermsActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        });

        // 체크박스 상태에 따라 '다음' 버튼 활성화
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> btnNext.setEnabled(isChecked));

        // '다음' 버튼 클릭 시의 동작 (예: 회원가입 다음 단계로 이동)
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(TermsActivity.this, InputSignup.class);
            startActivity(intent);
        });
    }
}
