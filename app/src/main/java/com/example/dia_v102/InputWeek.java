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

public class InputWeek extends AppCompatActivity {

    /*평소활동량 일주일에 몇 회씩 운동 중 인지 선택하는 클래스*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_week);


        /*뒤로 가기 버튼 -- 활동량입력-하루시간단위 화면으로 이동*/
        Button back_week = findViewById(R.id.back_week);
        back_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent = new Intent(InputWeek.this, InputTime.class);
                //intent.putExtra("name","mike"); //인텐트 객체 생성하고 name의 값을 부가데이터로 넣기
                setResult(RESULT_OK, signupintent);
                finish();
            }

        });

        /*완료버튼 -- 누르면 당뇨병 타입 선택 화면으로 넘어감 (Signup->InputBasicData->InputTime->InputWeek->Diatype)*/
        Button next_week = findViewById(R.id.next_week);
        next_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputWeek.this, Diatype.class);
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