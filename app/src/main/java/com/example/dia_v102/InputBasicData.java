package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InputBasicData extends AppCompatActivity {

    private String selectedGender = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_basic_data);


        /*뒤로 가기 버튼 -- 회원가입 화면으로 이동*/
        Button back_basic = findViewById(R.id.back_basic);
        back_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent = new Intent(InputBasicData.this, Signup.class);
                //intent.putExtra("name","mike"); //인텐트 객체 생성하고 name의 값을 부가데이터로 넣기
                setResult(RESULT_OK, signupintent);
                finish();
            }

        });

        // 성별 선택 TextViews
        TextView genderFemale = findViewById(R.id.gender_female);
        TextView genderMale = findViewById(R.id.gender_male);

       /* // 성별 선택 리스너
        View.OnClickListener genderClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == genderFemale) {
                    selectedGender = "여";
                    genderFemale.setBackgroundColor(ContextCompat.getColor(InputBasicData.this, R.drawable.selected_background));
                    genderMale.setBackgroundColor(ContextCompat.getColor(InputBasicData.this,  R.drawable.unselected_background));
                } else if (v == genderMale) {
                    selectedGender = "남";
                    genderFemale.setBackgroundColor(ContextCompat.getColor(InputBasicData.this,  R.drawable.unselected_background));
                    genderMale.setBackgroundColor(ContextCompat.getColor(InputBasicData.this,  R.drawable.selected_background));
                }
            }
        };*/
        // 성별 선택 리스너
        View.OnClickListener genderClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == genderFemale) {
                    selectedGender = "여";
                    genderFemale.setBackgroundResource(R.drawable.selected_background);
                    genderMale.setBackgroundResource(R.drawable.unselected_background);
                } else if (v == genderMale) {
                    selectedGender = "남";
                    genderFemale.setBackgroundResource(R.drawable.unselected_background);
                    genderMale.setBackgroundResource(R.drawable.selected_background);
                }
            }
        };

        genderFemale.setOnClickListener(genderClickListener);
        genderMale.setOnClickListener(genderClickListener);


        /*완료버튼 -- 누르면 활동량입력-하루시간단위 화면으로 넘어감 (Signup->InputBasicData->InputTime->InputWeek->Diatype)*/
        Button next_basic = findViewById(R.id.next_basic);
        next_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputBasicData.this, InputTime.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}