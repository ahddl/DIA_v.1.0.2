package com.example.dia_v102;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dia_v102.databaseF.Func_UserInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Navi extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String userNick;

    /*navigation 하단 탭 생성(5개)*/

    BottomNavigationView bottom_navigation;
    TextView nickView;

    Diet diet;
    Chatbot chatbot;
    Diabetes bloodsugar;
    //Calendar calendar;
    Graph settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diet);

        bottom_navigation = findViewById(R.id.bottom_navigation);

        nickView = findViewById(R.id.textView);
        FindNick(new NicknameCallback() {
            @Override
            public void onCallback(String nickname) {
                userNick = nickname;
                UserSet.setNickname(userNick);
                nickView.setText(userNick);
            }
        });

        diet = new Diet();
        chatbot = new Chatbot();
        bloodsugar = new Diabetes();
        settings = new Graph();

        getSupportFragmentManager().beginTransaction().replace(R.id.dietnFrame, diet).commit();

        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                                                        @Override
                                                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                                                            int itemId = item.getItemId();
                                                            if (itemId == R.id.tabdiet) {
                                                                getSupportFragmentManager().beginTransaction().replace(R.id.dietnFrame, diet).commit();
                                                                return true;
                                                            } else if (itemId == R.id.tabchatbot) {
                                                                getSupportFragmentManager().beginTransaction().replace(R.id.dietnFrame, chatbot).commit();
                                                                return true;
                                                            } else if (itemId == R.id.tabbloodsugar) {
                                                                getSupportFragmentManager().beginTransaction().replace(R.id.dietnFrame, bloodsugar).commit();
                                                                return true;
                                                            } else if (itemId == R.id.tabsettings) {
                                                                getSupportFragmentManager().beginTransaction().replace(R.id.dietnFrame, settings).commit();
                                                                return true;
                                                            } else {
                                                                return false;
                                                            }


                                                        }
                                                    }
        );
    }
    private void FindNick(NicknameCallback callback) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            callback.onCallback(null);
            return;
        }

        String userUID = user.getUid();
        UserSet.setUserId(userUID);
        Func_UserInfo userInfo = new Func_UserInfo();

        userInfo.getNick(userUID, new NicknameCallback() {
            @Override
            public void onCallback(String nickname) {
                callback.onCallback(nickname);
            }
        });
    }

}