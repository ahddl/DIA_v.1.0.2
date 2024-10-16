package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dia_v102.databaseF.Func_UserInfo;
import com.example.dia_v102.databaseF.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//food db 관련
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.example.dia_v102.database.AppDatabase;
import com.example.dia_v102.database.DatabaseProvider;
import com.example.dia_v102.entities.Food_menu;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText loginID, loginPW;
    private FirebaseAuth mAuth;
    private List<Food_menu> foodMenus;
    private FirebaseUser currentUser;


      /*로고 이모티콘 넣기, 앱 이름 넣기, Google or 카카오 로그인 연동, 아이디 및 pw 찾기 버튼 만들기,
      이 화면 전에 로고랑 앱 이름 간단히 뜨는 화면 넣기(앱 진입 시 뜨는 화면), 로그인 이후 바로 메인 화면 나오게 하기*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // DB 작업을 background thread 수행
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // DB 작업 수행
            AppDatabase db = DatabaseProvider.getDatabase(this);
            foodMenus = db.food_menuDao().getAll();
            // 작업이 끝난 후에도 UI 업데이트를 하지 않음
            if (foodMenus.isEmpty()) {
                // DB가 empty 하면 XML 데이터 삽입
                DatabaseProvider.parseCsvAndInsertToDB(this, R.raw.food_data37);
                //Log.d("ROOM DB", "Complete!");
            }
            /* 나중에 확인용.
            else {
                // DB가 이미 채워져 있음을 알리는 메시지
                //Log.d("DB ROOM", "Full DB");
                // DB 내용 확인
                for (Food_menu menu : foodMenus) {
                    Log.d("DB ROOM", "ID: " + menu.getId() + ", Name: " + menu.getFood() + ", Calories: " + menu.getCalories());
                }
            }
            */
        });

        // 자동 로그인
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser(); //로그인 된 유저 확인.
        if(currentUser != null){
            CompleteLogin(currentUser);
        }

        //로그인 버튼 -- ID와 PW 입력 후 로그인 버튼 누르면 로그인 완료 안내와 함께 메인 페이지(MainActivity2) 넘어감
        Button login = findViewById(R.id.btnlogin);
        loginID = findViewById(R.id.loginID);
        loginPW = findViewById(R.id.loginPW);
        login.setOnClickListener(v -> {
            // EditText-입력된 text 가져오기
            String id = loginID.getText().toString().trim();
            String pw = loginPW.getText().toString().trim();

            if (id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(MainActivity.this, "정확한 ID/PW를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sign in the user
            mAuth.signInWithEmailAndPassword(id, pw)
                    .addOnCompleteListener(MainActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            currentUser = mAuth.getCurrentUser();
                            CompleteLogin(currentUser);
                        } else {
                            // Sign in failed
                            Toast.makeText(MainActivity.this, "로그인 불가: ID/PW를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        /*회원가입 버튼 -- 누르면 Signup->InputBasicData->InputTime->InputWeek->Diatype 입력 완료 후
        다시 MainActivity로 돌아와서 로그인 하면 메인 페이지(nav)로 넘어감*/

        Button join_button = findViewById(R.id.joinbotton);
        join_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InputTerms.class);
            startActivity(intent);
        });

        /*//나중에 카카오 로그인 버튼으로 사용할 것임
        //임시 설정: 눌리면 메인 화면(nav) 이동
        Button googleLogin = findViewById(R.id.googlelogin);
        googleLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
        });*/


        /*아이디 찾기 버튼 -- 아이디 찾기 클래스 없음. 일단 주석 처리
        Button findID = findViewById(R.id.findID);

        findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindID.class);
                startActivity(intent);
            }
        });

        */
        //비밀번호 재설정 버튼
        Button findPW = findViewById(R.id.findPW);

        findPW.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FindPW.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
    }
    public void CompleteLogin(FirebaseUser currentUser){
        SetUser.setUserId(currentUser.getUid());
        Func_UserInfo funcUserInfo = new Func_UserInfo();
        funcUserInfo.loadData(currentUser.getUid(), new Func_UserInfo.DataLoadListener() {
            @Override
            public void onDataLoaded(UserInfo userInfo) {
                SetUser.setNickname(userInfo.nick);
                SetUser.setESub(userInfo.eMailSub);
                SetUser.setHeight(userInfo.height);
                SetUser.setWeight(userInfo.weight);
                SetUser.setAge(userInfo.age);
                SetUser.setGender(userInfo.gender.charAt(0));
                SetUser.setType(userInfo.type.charAt(0));
            }

            @Override
            public void onFailure(Exception e) {
                // 에러 처리
            }
        });

        // Navigate to HomeActivity
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
        finish();  // Finish LoginActivity so that the user cannot go back to it
    }
}