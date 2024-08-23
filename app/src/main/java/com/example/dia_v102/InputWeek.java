package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InputWeek extends AppCompatActivity {

    /*평소활동량 일주일에 몇 회씩 운동 중 인지 선택하는 클래스*/

    private Button selectedButton = null;

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

        Button next_week = findViewById(R.id.next_week);

        // 당뇨병 타입 선택 버튼들
        Button weekAct1 = findViewById(R.id.week_act1);
        Button weekAct2 = findViewById(R.id.week_act2);
        Button weekAct3 = findViewById(R.id.week_act3);
        Button weekAct4 = findViewById(R.id.week_act4);

        // 당뇨병 타입 버튼 클릭 리스너
        View.OnClickListener weekClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 버튼 색상 변경
                if (selectedButton != null) {
                    selectedButton.setSelected(false); // 이전 버튼의 선택 해제
                }
                selectedButton = (Button) v;
                selectedButton.setSelected(true); // 새로운 버튼 선택
                next_week.setEnabled(true);
            }
        };

        weekAct1.setOnClickListener(weekClickListener);
        weekAct2.setOnClickListener(weekClickListener);
        weekAct3.setOnClickListener(weekClickListener);
        weekAct4.setOnClickListener(weekClickListener);

        /*완료버튼 -- 누르면 당뇨병 타입 선택 화면으로 넘어감 (Signup->InputBasicData->InputTime->InputWeek->Diatype)*/
        next_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedButton == null) {
                    Toast.makeText(InputWeek.this, "평소 활동량을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 선택된 타입 처리 후 다음 화면으로 이동
                    Intent intent = new Intent(InputWeek.this, InputDiatype.class);
                    startActivity(intent);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}