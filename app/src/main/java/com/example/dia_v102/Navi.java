package com.example.dia_v102;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Navi extends AppCompatActivity {

    /*navigation 하단 탭 생성(5개)*/

    BottomNavigationView bottom_navigation;

    /*Fragment diet;
    Fragment chatbot;
    Fragment bloodsugar;
    Fragment calendar;
    Fragment settings;*/

    Diet diet;
    Chatbot chatbot;
    Bloodsugar bloodsugar;
    Calendar calendar;
    Settings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navi);

        //getSupportActionBar().setTitle("이건뭐야");

        bottom_navigation = findViewById(R.id.bottom_navigation);

        diet = new Diet();
        chatbot = new Chatbot();
        bloodsugar = new Bloodsugar();
        calendar = new Calendar();
        settings = new Settings();

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, diet).commit();

        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                                                        @Override
                                                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                                                            int itemId = item.getItemId();
                                                            if (itemId == R.id.tabdiet) {
                                                                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, diet).commit();
                                                                return true;
                                                            } else if (itemId == R.id.tabchatbot) {
                                                                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, chatbot).commit();
                                                                return true;
                                                            } else if (itemId == R.id.tabbloodsugar) {
                                                                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, bloodsugar).commit();
                                                                return true;
                                                            } else if (itemId == R.id.tabcalendar) {
                                                                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, calendar).commit();
                                                                return true;
                                                            } else if (itemId == R.id.tabsettings) {
                                                                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, settings).commit();
                                                                return true;
                                                            } else {
                                                                return false;
                                                            }

                       /* int itemId = Item.getItemId();
                       Fragment fragment = null;

                       if(itemId==R.id.diet){
                           fragment = diet;
                           getSupportActionBar().setTitle("식단");
                       }
                       else if(itemId==R.id.chatbot){
                           fragment=chatbot;
                           getSupportActionBar().setTitle("챗봇");
                       }
                       else if(itemId==R.id.bloodsugar){
                           fragment=bloodsugar;
                           getSupportActionBar().setTitle("혈당");
                       }
                       else if(itemId==R.id.calendar){
                           fragment=calendar;
                           getSupportActionBar().setTitle("달력");
                       }
                       else if(itemId==R.id.settings){
                           fragment=settings;
                           getSupportActionBar().setTitle("설정");
                       }

                        return loadFragment(fragment);*/

                                                        }
                                                    }
        );

    /*private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.diet, fragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }
}*/
    }
}