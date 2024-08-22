package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Signup extends AppCompatActivity {

    /*아이디 중복확인 기능, 아이디별명비밀번호이메일 DB에 저장, 비밀번호 소문자특수문자1개씩 총7문자이상인지 확인기능,
    비밀번호랑 비밀번호확인란 일치하는지 확인기능, 이메일 수신동의 및 알림사항 이메일 전송 가능하게 연결, 별명 mainpage에 뜰 수있게 하기*/


    private boolean isEmailSubscribed = false;  //// 이메일 수신 동의 상태를 추적하는 변수
    RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_signup);

        /*뒤로 가기 버튼 -- main 화면으로 이동*/
        Button back1 = findViewById(R.id.back1);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent = new Intent(Signup.this, MainActivity.class);
                //intent.putExtra("name","mike"); //인텐트 객체 생성하고 name의 값을 부가데이터로 넣기
                setResult(RESULT_OK, signupintent);
                finish();
            }

        });

        /*완료버튼 -- 누르면 기본정보입력으로 넘어감 (Signup->InputBasicData->InputTime->InputWeek->Diatype)*/
        Button nextSignup = findViewById(R.id.nextSignup);
        nextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, InputBasicData.class);
                startActivity(intent);
            }
        });


        /* 이메일 수신 동의 라디오 버튼 */
        radioButton = findViewById(R.id.radioButton);
        radioButton.setChecked(isEmailSubscribed); // 초기 상태 설정


        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEmailSubscribed = !isEmailSubscribed; // 구독 상태 토글
                radioButton.setChecked(isEmailSubscribed);  // 라디오 버튼 상태 업데이트
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
    }
}

//이메일 구독상태, 즉 이메일에 전송 동의했을 떄 이메일 날라가게 해야함, isEmailSubscribed() 함수작성