package com.example.dia_v102;

import android.util.Log;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    // 클래스 멤버 변수로 선언
    private List<Food_menu> foodMenus;
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

      /*로고 이모티콘 넣기, 앱 이름 넣기, Google or 카카오 로그인 연동, 아이디 및 pw 찾기 버튼 만들기,
      이 화면 전에 로고랑 앱 이름 간단히 뜨는 화면 넣기(앱 버튼 누르자마자 뜨는 화면), 로그인 이후 바로 메인화면 나오게 하기*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // 데이터베이스 작업을 백그라운드 스레드에서 수행
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // DB 작업 수행
            AppDatabase db = DatabaseProvider.getDatabase(this);
            foodMenus = db.food_menuDao().getAll();
            // 작업이 끝난 후에도 UI 업데이트를 하지 않음
            if (foodMenus.isEmpty()) {
                // 데이터베이스가 비어있다면, XML 데이터를 삽입
                DatabaseProvider.parseCsvAndInsertToDB(this, R.raw.fooddata);
                Log.d("DBROOM", "Complete!");
            } else {
                // 데이터베이스가 이미 채워져 있음을 알리는 메시지
                Log.d("DBROOM", "Full DB");
            }
        });




        mAuth = FirebaseAuth.getInstance();

        //User check
        FirebaseUser currentUser = mAuth.getCurrentUser();

        /*
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this, Navi.class);
            startActivity(intent);
        }

         */


        //로그인 버튼 -- ID와 PW 입력 후 로그인 버튼 누르면 로그인 완료 안내와 함께 메인 페이지(nav)로 넘어감

        Button login = findViewById(R.id.btnlogin);
        loginID = findViewById(R.id.loginID);
        loginPW = findViewById(R.id.loginPW);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 입력된 텍스트를 가져오기
                String id = loginID.getText().toString().trim();
                String pw = loginPW.getText().toString().trim();

                if (id.isEmpty() || pw.isEmpty()) {
                    Toast.makeText(MainActivity.this, "정확한 ID/PW를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sign in the user
                mAuth.signInWithEmailAndPassword(id, pw)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                    // Navigate to HomeActivity
                                    Intent intent = new Intent(MainActivity.this, Navi.class);
                                    startActivity(intent);
                                    finish();  // Finish LoginActivity so that the user cannot go back to it
                                } else {
                                    // Sign in failed
                                    Toast.makeText(MainActivity.this, "로그인 불가: ID/PW를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                /*
                //AlertDialog를 생성하고 설정
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Login Information");
                builder.setMessage("ID: ", id, "\nPW: ", pw);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 확인 버튼 클릭 시 대화창 닫기
                        dialog.dismiss();
                    }
                });


                // AlertDialog를 보여주기
                AlertDialog dialog = builder.create();
                dialog.show();
                */


            }
        });

        /*회원가입 버튼 -- 누르면 Signup->InputBasicData->InputTime->InputWeek->Diatype 입력 완료 후
        다시 main으로 돌아와서 로그인 하면 메인페이지(nav)로 넘어감*/

        Button join_button = findViewById(R.id.joinbotton);
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });

        //나중에 카카오 로그인 버튼으로 사용할 것임
        //임시로 일단 눌리면 메인화면(nav) 나타나게 설정
        Button googlelogin = findViewById(R.id.googlelogin);
        googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Navi.class);
                startActivity(intent);
            }
        });


/*
        *//*아이디 찾기 버튼 -- 아이디 찾기 클래스 없음. 일단 주석 처리*//*
        Button findID = findViewById(R.id.findID);

        findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindID.class);
                startActivity(intent);
            }
        });

        *//*비밀번호 재설정 버튼 -- 비밀번호 재설정 클래스 없음. 일단 주석 처리*//*
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