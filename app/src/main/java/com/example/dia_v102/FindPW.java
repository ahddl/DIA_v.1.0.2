package com.example.dia_v102;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class FindPW extends AppCompatActivity {
    private EditText editTextEmail;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpw);

        // FirebaseAuth 초기화
        auth = FirebaseAuth.getInstance();

        // UI 요소 초기화
        editTextEmail = findViewById(R.id.editTextEmail);
        Button resetPWButton = findViewById(R.id.resetPasswordButton);

        /*뒤로 가기 버튼 -- main activity 로 이동*/
        Button back1 = findViewById(R.id.back1);
        back1.setOnClickListener(v -> {
            Intent signupintent = new Intent(FindPW.this, MainActivity.class);
            setResult(RESULT_OK, signupintent);
            finish();
        });

        // 버튼 클릭 리스너 설정
        resetPWButton.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(FindPW.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            //이메일 유효 확인
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("유효한 이메일을 입력하세요.");
                return;
            }
            sendPasswordResetEmail(email);
        });
    }

    // 비밀번호 재설정 이메일 보내기
    private void sendPasswordResetEmail(String emailAddress) {
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // 이메일 전송 성공 시
                        Log.d("PasswordReset", "비밀번호 재설정 이메일 전송 완료");
                        Toast.makeText(FindPW.this, "비밀번호 재설정 이메일이 전송되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        // 이메일 전송 실패 시
                        Log.w("PasswordReset", "비밀번호 재설정 이메일 전송 실패", task.getException());
                        Toast.makeText(FindPW.this, "이메일 전송 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
