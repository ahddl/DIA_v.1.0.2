package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class InputBasicData extends AppCompatActivity {

    FirebaseAuth mAuth;
    /* 기본 정보 키, 몸무게, 만나이, 성별 입력하는 클래스*/

    private char selectedGender = '\0';
    private char selectDiabetes ='\0';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.input_basic_data);

        mAuth = FirebaseAuth.getInstance();


        /*뒤로 가기 버튼 -- 회원가입 화면으로 이동*/
        Button back_basic = findViewById(R.id.back_basic);
        back_basic.setOnClickListener(v -> {
            Intent signupintent = new Intent(InputBasicData.this, InputSignup.class);
            setResult(RESULT_OK, signupintent);
            finish();
        });

        // 성별 선택 TextViews
        TextView genderFemale = findViewById(R.id.gender_female);
        TextView genderMale = findViewById(R.id.gender_male);

        TextView type1 = findViewById(R.id.type_1);
        TextView type2 = findViewById(R.id.type_2);
        TextView typePreg = findViewById(R.id.type_preg);
        TextView typeOther = findViewById(R.id.type_other);


        // 성별 선택 (여, 남) 리스너
        View.OnClickListener selectClickListener = v -> {
            // 성별 버튼 클릭 시
            if (v == genderFemale) {
                selectedGender = 'F';
                genderFemale.setSelected(true); // 성별 여성 버튼을 선택 상태로 설정
                genderMale.setSelected(false);  // 성별 남성 버튼을 선택 해제 상태로 설정
            } else if (v == genderMale) {
                selectedGender = 'M';
                genderFemale.setSelected(false); // 성별 여성 버튼을 선택 해제 상태로 설정
                genderMale.setSelected(true);    // 성별 남성 버튼을 선택 상태로 설정
            }

            if (v == type1) {
                selectDiabetes='1';
                type1.setSelected(true);
                type2.setSelected(false);
                typePreg.setSelected(false);
                typeOther.setSelected(false);
            }
            else if (v == type2) {
                selectDiabetes='2';
                type1.setSelected(false);
                type2.setSelected(true);
                typePreg.setSelected(false);
                typeOther.setSelected(false);
            }
            else if (v == typePreg) {
                selectDiabetes='p';
                type1.setSelected(false);
                type2.setSelected(false);
                typePreg.setSelected(true);
                typeOther.setSelected(false);
            }
            else if (v == typeOther) {
                selectDiabetes='o';
                type1.setSelected(false);
                type2.setSelected(false);
                typePreg.setSelected(false);
                typeOther.setSelected(true);
            }
        };

        genderFemale.setOnClickListener(selectClickListener);
        genderMale.setOnClickListener(selectClickListener);
        type1.setOnClickListener(selectClickListener);
        type2.setOnClickListener(selectClickListener);
        typePreg.setOnClickListener(selectClickListener);
        typeOther.setOnClickListener(selectClickListener);

        /*완료버튼 -- 누르면 활동량입력-하루시간단위 화면으로 넘어감 (Signup->InputBasicData->InputTime->InputWeek->Diatype)*/

        EditText heightEditText = findViewById(R.id.height);
        EditText weightEditText = findViewById(R.id.weight);
        EditText ageEditText = findViewById(R.id.age);
        Button next_basic = findViewById(R.id.next_basic);

        // 완료 버튼 리스너
        next_basic.setOnClickListener(v -> {
            // 입력값 가져오기
            String heightText = heightEditText.getText().toString();
            String weightText = weightEditText.getText().toString();
            String ageText = ageEditText.getText().toString();

            // 입력값 검증
            if (selectedGender == '\0') {
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

            if (TextUtils.isEmpty(ageText)) {
                Toast.makeText(InputBasicData.this, "나이를 입력해주세요.(만나이)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectDiabetes=='\0'){
                Toast.makeText(InputBasicData.this, "당뇨병 타입을 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 키와 몸무게 검증
            int height;
            int weight;
            int age;
            try {
                height = Integer.parseInt(heightText);
                weight = Integer.parseInt(weightText);
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                Toast.makeText(InputBasicData.this, "키와 몸무게는 숫자만 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (height < 100 || height > 280) {
                Toast.makeText(InputBasicData.this, "키는 100cm 이상 280cm 이하로 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (weight < 20 || weight > 250) {
                Toast.makeText(InputBasicData.this, "몸무게는 20kg 이상 250kg 이하로 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(age >150 || age<0){
                Toast.makeText(InputBasicData.this, "나이는 0~150사이로 입력해야합니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            //전부 UserSet에 넣기
            UserSet.setType(selectDiabetes);
            UserSet.setHeight(height);
            UserSet.setWeight(weight);
            UserSet.setAge(age);
            UserSet.setGender(selectedGender);
            //Log.d("Firebase", UserSet.getUserId()+"\n"+UserSet.getESub()+"\n"+UserSet.getNickname()+"\n"+UserSet.getHeight()+"\n"+UserSet.getWeight()+"\n"+UserSet.getAge()+"\n"+UserSet.getGender()+"\n"+UserSet.getType());



            try {
                registerUser();
            } catch (Exception e) {
                Log.d("EError", "Error saving user info: " + e.getMessage());
            }



        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerUser() {
        Log.d("EError", "Error Register");
        // Firebase authentication 사용자 생성
        mAuth.createUserWithEmailAndPassword(UserSet.getEmail(), UserSet.getPW())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("EError", "Success Register");
                        // 회원가입 성공
                        UserSet.loginComplete(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                        Toast.makeText(InputBasicData.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        // 다음 활동으로 이동 (원래는 InputTime)
                        Intent intent = new Intent(InputBasicData.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // 회원가입 실패
                        Log.d("EError", "Error Register2");
                        Toast.makeText(InputBasicData.this, "Registration failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}