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

public class MainActivity extends AppCompatActivity {

    //엄 이거 뭐지
    /*private ActivityResultLauncher<Intent> mPreContractStartActivityResult =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult a_result) {
                            if (a_result.getResultCode() == Activity.RESULT_OK) {

                            }
                        }
                    });*/

      /*로고 이모티콘 넣기, 앱 이름 넣기, 구글이나 카카오 로그인 연동하기, 아이디 및 비밀번호 찾기 버튼 만들기,
      이 화면 전에 로고랑 앱이름만 간단히 뜨는 화면 넣기(앱 버튼누르자말자 뜨는 화면), 로그인 이후에는 바로 메인화면 나오게 하기*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        /*로그인버튼 -- ID와 PW 입력 후 로그인 버튼 누르면 로그인 완료 안내와 함께 메인 페이지(nav)로 넘어가게 만들어야함
        임시로 회원가입 Signup class로 넘어가게 해둠*/
        Button login = findViewById(R.id.btnlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });

        /*회원가입 버튼 -- 누르면 Signup->InputBasicData->InputTime->InputWeek->Diatype 입력 완료 후
        다시 main으로 돌아와서 로그인 하면 메인페이지(nav)로 넘어감*/

        Button joinbotton = findViewById(R.id.joinbotton);

        joinbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });

        //나중에 카카오로그인 버튼으로 사용할 것임
        //임시로 일단 눌리면 메인화면(nav) 나타나게 설정해둠
        Button googlelogin = findViewById(R.id.googlelogin);
        googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Navi.class);
                startActivity(intent);
            }
        });


/*
        *//*아이디 찾기 버튼 -- 아이디 찾기 클래스 안만들어서 일단 주석 처리해둠*//*
        Button findID = findViewById(R.id.findID);

        findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindID.class);
                startActivity(intent);
            }
        });

        *//*비밀번호 재설정 버튼 -- 비밀번호 재설정 클래스 안만들어서 일단 주석 처리해둠*//*
        Button findPW = findViewById(R.id.findPW);

        findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindPW.class);
                startActivity(intent);
            }
        });
*/

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
    }
}