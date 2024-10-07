package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Edit_info extends AppCompatActivity {
    // 선언한 View들
    private EditText heightEditText, weightEditText, ageEditText;
    private Button genderFemale, genderMale, type1, type2, typePreg, typeOther;
    private RadioButton emailRadio;

    private char selectedGender = '\0', selectDiabetes ='\0';
    private boolean isEmailSubscribed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fix_basic_data);

        setting_Current();

        /*뒤로 가기 버튼 -- MainActivity2 이동*/
        Button back_basic = findViewById(R.id.back_basic);
        back_basic.setOnClickListener(v -> {
            Intent intent = new Intent(Edit_info.this, MainActivity2.class);
            startActivity(intent);
            finish();
        });

        // 성별 선택 (여, 남) 리스너
        View.OnClickListener selectedClickListener = v -> {
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
        genderFemale.setOnClickListener(selectedClickListener);
        genderMale.setOnClickListener(selectedClickListener);
        type1.setOnClickListener(selectedClickListener);
        type2.setOnClickListener(selectedClickListener);
        typePreg.setOnClickListener(selectedClickListener);
        typeOther.setOnClickListener(selectedClickListener);

        emailRadio.setOnClickListener(v -> {
            isEmailSubscribed = !isEmailSubscribed; // 구독 상태 토글
            emailRadio.setChecked(isEmailSubscribed);  // 라디오 버튼 상태 업데이트
        });

        Button saveButton = findViewById(R.id.saveInfo);
        saveButton.setOnClickListener(v->{
            // 입력값 가져오기
            String heightText = heightEditText.getText().toString();
            String weightText = weightEditText.getText().toString();
            String ageText = ageEditText.getText().toString();

            if (TextUtils.isEmpty(heightText) || TextUtils.isEmpty(weightText) || TextUtils.isEmpty(ageText)) {
                Toast.makeText(Edit_info.this, "값이 모두 채워지지 않았습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 키와 몸무게 검증
            int height, weight, age;
            try {
                height = Integer.parseInt(heightText);
                weight = Integer.parseInt(weightText);
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                Toast.makeText(Edit_info.this, "키와 몸무게는 숫자만 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (height < 100 || height > 280) {
                heightEditText.setError("키는 100cm 이상 280cm 이하로 입력해주세요.");
                return;
            }

            if (weight < 20 || weight > 250) {
                weightEditText.setError("몸무게는 20kg 이상 250kg 이하로 입력해주세요.");
                return;
            }

            if(age >150 || age<0){
                ageEditText.setError("나이는 0~150사이로 입력해주세요.");
                return;
            }
            //전부 UserSet에 넣기
            UserSet.setESub(isEmailSubscribed);
            UserSet.setType(selectDiabetes);
            UserSet.setHeight(height);
            UserSet.setWeight(weight);
            UserSet.setAge(age);
            UserSet.setGender(selectedGender);

            try {
                UserSet.saveUserSet();
                Toast.makeText(Edit_info.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d("EError", "Error saving user info: " + e.getMessage());
            }
        });
    }
    void setting_Current(){
        // 각 View 초기화
        genderFemale = findViewById(R.id.gender_female);
        genderMale = findViewById(R.id.gender_male);
        type1 = findViewById(R.id.type_1);
        type2 = findViewById(R.id.type_2);
        typePreg = findViewById(R.id.type_preg);
        typeOther = findViewById(R.id.type_other);
        emailRadio = findViewById(R.id.radioButton);
        heightEditText = findViewById(R.id.height);
        weightEditText = findViewById(R.id.weight);
        ageEditText = findViewById(R.id.age);

        heightEditText.setText(String.valueOf(UserSet.getHeight()));
        weightEditText.setText(String.valueOf(UserSet.getWeight()));
        ageEditText.setText(String.valueOf(UserSet.getAge()));

        isEmailSubscribed = UserSet.getESub();
        emailRadio.setChecked(isEmailSubscribed);

        //성별 설정
        selectedGender = UserSet.getGender();
        boolean isMale = (selectedGender == 'M');
        genderFemale.setSelected(!isMale);
        genderMale.setSelected(isMale);

        // 당뇨병 타입
        selectDiabetes = UserSet.getType();
        if(selectDiabetes=='1'){type1.setSelected(true);}
        else if(selectDiabetes=='2'){type2.setSelected(true);}
        else if(selectDiabetes=='p'){typePreg.setSelected(true);}
        else if(selectDiabetes=='o'){typeOther.setSelected(true);}

    }
}
