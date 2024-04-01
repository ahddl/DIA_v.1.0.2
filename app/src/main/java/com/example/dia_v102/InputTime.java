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

public class InputTime extends AppCompatActivity {

    //private Button selectedButton;

    /*평소활동량 하루에 몇시간 운동 중 인지 선택하는 클래스*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_time);


        /*뒤로 가기 버튼 -- 기본정보입력 화면으로 이동*/
        Button back_time = findViewById(R.id.back_time);
        back_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent = new Intent(InputTime.this, InputBasicData.class);
                //intent.putExtra("name","mike"); //인텐트 객체 생성하고 name의 값을 부가데이터로 넣기
                setResult(RESULT_OK, signupintent);
                finish();
            }

        });

        /*완료버튼 -- 누르면 활동량입력-주단위으로 넘어감 (Signup->InputBasicData->InputTime->InputWeek->Diatype)*/
        Button next_time = findViewById(R.id.next_time);
        next_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputTime.this, InputWeek.class);
                startActivity(intent);
            }
        });


        //버튼 선택시 색상 바뀌면서 라디오 버튼처럼 선택되는 거 하다가 실패함 다시 해야함.
/*
        public void onButtonClick(View view) {
            // Reset color of previously selected button
            if (selectedButton != null) {
                selectedButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white)); // Use ContextCompat
            }

            // Set color of clicked button
            selectedButton = (Button) view;
            selectedButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light)); // Use ContextCompat
        }

        public void onNextButtonClick(View view) {
            if (selectedButton != null) {
                // Save selected button information or perform other actions
                // For example, you can get the text of the selected button like this:
                String buttonText = selectedButton.getText().toString();
                // Now you can do whatever you want with the selected button information
            }
*/




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}