package com.example.dia_v102;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dia_v102.utils.CameraGalleryPicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends AppCompatActivity {

    /*navigation 하단 탭 생성(4개)*/
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FragmentDiet diet;
    FragmentDiabetes diabetes;
    FragmentChatbot chatting;
    FragmentGraph graph;
    FragmentWarning danger;

    LinearLayout dietBtn, diabetesBtn, chatBtn, graphBtn;
    private  List<LinearLayout> tabButtons;
    CameraGalleryPicker imgPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // 제목을 "DIABETIC:CARE"로 변경
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("DIABETIC:CARE");
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        dietBtn = findViewById(R.id.tabDiet);
        diabetesBtn = findViewById(R.id.tabDiabetes);
        chatBtn = findViewById(R.id.tabChatbot);
        graphBtn = findViewById(R.id.tabGraph);

        bottomNavigation();

        //기본 fragment 설정
        if (savedInstanceState == null) {onTabButtonSelected(dietBtn);}

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.bardiet) {
                onTabButtonSelected(dietBtn);
            } else if (id == R.id.bardiabetes) {
                onTabButtonSelected(diabetesBtn);
            } else if (id == R.id.barchatbot) {
                onTabButtonSelected(chatBtn);
            } else if (id == R.id.bargraph) {
                onTabButtonSelected(graphBtn);
            } else if (id == R.id.blood_sugar_warning){
                //onTabButtonSelected();
                replaceFragment(new FragmentWarning());
            }else if(id == R.id.setting_edit_info){
                Intent intent = new Intent(this, Edit_info.class);
                startActivity(intent);
                finish();
            }else if (id == R.id.setting_logout) {
                logout();
            }
            else if (id == R.id.setting_alarm_settings) {  // 알람 설정 처리 부분 추가
                Intent intent = new Intent(MainActivity2.this, Alarm.class);
                startActivity(intent);  // Alarm 액티비티로 이동
            }
            drawerLayout.closeDrawer(navigationView);// 드로어 닫기
            return true;
        });

        cameraInput();


        /* CollapsingToolbarLayout 및 AppBarLayout 초기화
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        appBarLayout = findViewById(R.id.app_bar_layout); // AppBarLayout ID에 맞게 수정

        // 제목 설정
        collapsingToolbarLayout.setTitle("카메라로! /n 음식을 인식해보세요");

        // AppBarLayout 상태 변화에 따라 제목 변경
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed 상태
                    collapsingToolbarLayout.setTitle("DIA");
                } else {
                    // Expanded 상태
                    collapsingToolbarLayout.setTitle("카메라로!\n음식을 인식해보세요");
                }
            }
        });*/

    }

    private void cameraInput() {
        // ActivityResultLauncher 초기화
        //카메라 및 갤러리 열기 (음식 인식 위한)
        ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri uri = imgPicker.getUri();
                        startMainActivity2(uri);
                    } else {
                        Toast.makeText(this, "사진을 촬영하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        ActivityResultLauncher<Intent> pickGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            startMainActivity2(selectedImageUri);
                        }
                    } else {
                        Toast.makeText(this, "사진을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imgPicker = new CameraGalleryPicker(this, takePictureLauncher, pickGalleryLauncher);
        // Bottom Sheet Dialog 제거 및 이미지 소스 창 활성화
        fab.setOnClickListener(v -> imgPicker.showImageSourceDialog());
    }

    private void bottomNavigation() {
        tabButtons = new ArrayList<>();

        tabButtons.add(dietBtn);
        tabButtons.add(diabetesBtn);
        tabButtons.add(chatBtn);
        tabButtons.add(graphBtn);

        for(LinearLayout btn : tabButtons){//onclick 메서드
            btn.setOnClickListener(v -> onTabButtonSelected(btn));
        }
    }
    private void onTabButtonSelected(LinearLayout selectedButton) {
        int color = ContextCompat.getColor(this, R.color.dark_blue);
        ImageView icon;
        TextView text;
        for (LinearLayout tabButton : tabButtons) {
            icon = tabButton.findViewById(R.id.imgView);
            text = tabButton.findViewById(R.id.textView);
            if (tabButton == selectedButton) {
                // 선택된 버튼 상태 설정
                icon.setColorFilter(color, PorterDuff.Mode.SRC_IN); // 아이콘 색상 변경
                text.setTextColor(color); // 텍스트 색상 변경
                replaceFragment(getFragmentByButton(tabButton)); // Fragment 교체
            } else {
                // 선택 되지 않은 것들은 색상 리셋
                icon.clearColorFilter();
                text.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        }
    }
    private Fragment getFragmentByButton(LinearLayout tabButton) {
        diet = new FragmentDiet();
        diabetes = new FragmentDiabetes();
        chatting = new FragmentChatbot();
        graph = new FragmentGraph();
        danger = new FragmentWarning();

        if (tabButton.getId() == R.id.tabDiet) {
            return diet;
        } else if (tabButton.getId() == R.id.tabDiabetes) {
            return diabetes;
        } else if (tabButton.getId() == R.id.tabChatbot) {
            return chatting;
        } else if (tabButton.getId() == R.id.tabGraph) {
            return graph;
        } //tab 추가하려면 여기
        return null; // 기본값
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 메뉴를 inflate 하는 메서드 추가
        getMenuInflater().inflate(R.menu.toolbar_menu, menu); // toolbar_menu.xml 메뉴 리소스 inflate

        //toolbar_menu 알람, 더보기(아직 무슨 기능 넣을지 미정) 설정 여기서 해야함
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        SetUser.logOut();

        // 로그인 창으로 이동
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // 현재 activity 종료
    }

    private void startMainActivity2(Uri imageUri) {
        Intent intent = new Intent(this, DietCheckMenu.class);
        intent.putExtra("imageUri", imageUri.toString());
        startActivity(intent);
    }
}
