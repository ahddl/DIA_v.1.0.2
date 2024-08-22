package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InputBasicData extends AppCompatActivity {

    /* 기본 정보 키, 몸무게, 만나이, 성별 입력하는 클래스*/

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

        // 성별 선택 (여, 남) 리스너
        View.OnClickListener genderClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 성별 버튼 클릭 시
                if (v == genderFemale) {
                    selectedGender = "Female";
                    genderFemale.setSelected(true); // 성별 여성 버튼을 선택 상태로 설정
                    genderMale.setSelected(false);  // 성별 남성 버튼을 선택 해제 상태로 설정
                } else if (v == genderMale) {
                    selectedGender = "Male";
                    genderFemale.setSelected(false); // 성별 여성 버튼을 선택 해제 상태로 설정
                    genderMale.setSelected(true);    // 성별 남성 버튼을 선택 상태로 설정
                }
            }
        };

        genderFemale.setOnClickListener(genderClickListener);
        genderMale.setOnClickListener(genderClickListener);


        /*완료버튼 -- 누르면 활동량입력-하루시간단위 화면으로 넘어감 (Signup->InputBasicData->InputTime->InputWeek->Diatype)*/

        EditText heightEditText = findViewById(R.id.height);
        EditText weightEditText = findViewById(R.id.weight);
        Button next_basic = findViewById(R.id.next_basic);

        // 완료 버튼 리스너
        next_basic.setOnClickListener(v -> {
            // 입력값 가져오기
            String heightText = heightEditText.getText().toString();
            String weightText = weightEditText.getText().toString();

            // 입력값 검증
            if (selectedGender == null) {
                Toast.makeText(InputBasicData.this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(heightText)) {
                Toast.makeText(InputBasicData.this, "키를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(weightText)) {
                Toast.makeText(InputBasicData.this, "몸무게를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 키와 몸무게 검증
            int height;
            int weight;
            try {
                height = Integer.parseInt(heightText);
                weight = Integer.parseInt(weightText);
            } catch (NumberFormatException e) {
                Toast.makeText(InputBasicData.this, "키와 몸무게는 숫자만 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (height < 100 || height > 280) {
                Toast.makeText(InputBasicData.this, "키는 100cm 이상 280cm 이하로 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (weight < 20 || weight > 700) {
                Toast.makeText(InputBasicData.this, "몸무게는 20kg 이상 700kg 이하로 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 다음 활동으로 이동
            Intent intent = new Intent(InputBasicData.this, InputTime.class);
            startActivity(intent);
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}