package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InputDiatype extends AppCompatActivity {

    /*당뇨병 타입이 무엇인지 선택하는 클래스*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_diatype);


        /*뒤로 가기 버튼 -- 활동량입력-주단위 화면으로 이동*/
        Button back_diatype = findViewById(R.id.back_diatype);
        back_diatype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent = new Intent(InputDiatype.this, InputWeek.class);
                //intent.putExtra("name","mike"); //인텐트 객체 생성하고 name의 값을 부가데이터로 넣기
                setResult(RESULT_OK, signupintent);
                finish();
            }

        });

        /*완료버튼 -- 누르면 "회원가입이 완료되었습니다. 다시 로그인을 시도해주십시오."의 안내멘트 후 main 화면으로 돌아감
        아직 안내멘트 설정 안함, 임시로 main으로 돌아가게만 설정해둠
        (Signup->InputBasicData->InputTime->InputWeek->Diatype)*/
        Button next_diatype = findViewById(R.id.next_diatype);
        next_diatype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputDiatype.this, MainActivity.class);
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