package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;


public class InputSignup extends AppCompatActivity {

    /*아이디 중복 확인 기능, 아이디/별명/pw/이메일 DB에 저장, pw 소문자/특수 문자1개씩 총 7문자 이상인지 확인 기능,
    pw랑 pw 확인란 일치 여부 확인 기능, 이메일 수신 동의 및 알림 사항 이메일 전송 가능하게 연결, 별명 mainpage에 뜰 수있게 하기*/

    private boolean isEmailAvailable = false;
    private boolean isPWAvailable = false;
    private boolean isEmailSubscribed = false;  //// 이메일 수신 동의 상태를 추적하는 변수
    RadioButton radioButton;
    private EditText textID,textPW,checkPW, nickName;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.input_signup);
        mAuth = FirebaseAuth.getInstance();

        /*뒤로 가기 버튼 -- main 화면으로 이동*/
        Button back1 = findViewById(R.id.back1);
        back1.setOnClickListener(v -> {
            Intent signupintent = new Intent(InputSignup.this, MainActivity.class);
            setResult(RESULT_OK, signupintent);
            finish();
        });

        textID = findViewById(R.id.textID);
        textPW = findViewById(R.id.password);
        checkPW = findViewById(R.id.Password2);
        nickName = findViewById(R.id.nickName);

        // TextWatcher 추가
        textID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력이 변경될 때마다 호출됨
                check_Email(s.toString());  // 아이디 입력이 변경되면 dup_check를 true로 리셋
            }
            @Override
            public void afterTextChanged(Editable s){}
        });

        /*완료 버튼 -- 누르면 기본 정보 입력으로 넘어감 (Signup->InputBasicData->InputTime->InputWeek->Diatype)*/
        Button nextSignup = findViewById(R.id.nextSignup);
        nextSignup.setOnClickListener(v -> {
            check_PW(textPW.getText().toString(), checkPW.getText().toString());
            if(isEmailAvailable && isPWAvailable) {
                UserSet.setNickname(nickName.getText().toString());
                registerUser();
            }
            if(!isEmailAvailable){
                Toast.makeText(InputSignup.this, "이메일이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            }
            if(!isPWAvailable) {
                Toast.makeText(InputSignup.this, "비밀번호 입력이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        /* 이메일 수신 동의 라디오 버튼 */
        radioButton = findViewById(R.id.radioButton);
        radioButton.setChecked(isEmailSubscribed); // 초기 상태 설정


        radioButton.setOnClickListener(v -> {
            isEmailSubscribed = !isEmailSubscribed; // 구독 상태 토글
            radioButton.setChecked(isEmailSubscribed);  // 라디오 버튼 상태 업데이트
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
    }

    private void registerUser() {
        String email = textID.getText().toString().trim();
        String password = textPW.getText().toString().trim();

        UserSet.setEmail(email);
        UserSet.setPW(password);
        UserSet.setESub(isEmailSubscribed);

        Intent intent = new Intent(InputSignup.this, InputBasicData.class);
        startActivity(intent);
        finish();
    }

    private void check_Email(String email){
        if(email.isEmpty()){
            textID.setError("Please Write Your E-mail");
            isEmailAvailable = false;
            return;
        }
        //유효한 이메일인지 확인
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textID.setError("Please enter a valid email");
            isEmailAvailable = false;
            return;
        }
        /* Firebase Authentication으로 이메일 중복 여부 확인
        mAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener(this, new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.isSuccessful()) {
                        SignInMethodQueryResult result = task.getResult();
                        if (Objects.requireNonNull(result.getSignInMethods()).isEmpty()) {
                            // 이메일이 사용 가능한 경우
                            isEmailAvailable = true;
                            textID.setError(null);
                        } else {
                            // 이메일이 이미 사용 중인 경우
                            isEmailAvailable = false;
                            textPW.setError("Email is already in use");
                        }
                    } else {
                        // 이메일 확인 실패
                        Toast.makeText(Signup.this, "Failed to check email: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        isEmailAvailable = false;
                    }
                }
            });

         */
        isEmailAvailable = true;
    }
    private void check_PW(String PW, String check){
        if(!PW.equals(check)){
            checkPW.setError("비밀번호가 일치하지 않습니다.");
            isPWAvailable = false;
            return;
        }
        if(PW.length()<7){
            checkPW.setError("비밀번호는 7자 이상이어야 합니다.");
            isPWAvailable = false;
            return;
        }
        // 비밀번호에 특수문자 포함 여부 확인
        if (!PW.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            checkPW.setError("하나 이상의 특수문자가 포함되어야 합니다.");
            isPWAvailable = false;
            return;
        }

        isPWAvailable = true;
    }
}

//이메일 구독 상태, 즉 이메일 전송을 동의할 때 이메일 전송 해야함, isEmailSubscribed() 함수 작성