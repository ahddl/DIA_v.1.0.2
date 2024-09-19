package com.example.dia_v102;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;


public class MainActivity2 extends AppCompatActivity {

    /*//FragmentDiet.java로 이동 필요
    private FirebaseAuth mAuth;
    private String userNick;

    TextView nickView;*/

    /*navigation 하단 탭 생성(4개)*/
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;

    FragmentDiet diet;
    FragmentDiabetes diabetes;
    FragmentChatbot chatbot;
    FragmentGraph graph;

    //카메라 및 갤러리 열기 (음식인식 위한)
    File file;
    Uri uri;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> pickGalleryLauncher;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            // 최초 실행 시 기본 프래그먼트를 설정
            replaceFragment(new FragmentDiet());
            bottomNavigationView.setSelectedItemId(R.id.tabdiet);  // 하단 네비게이션 뷰에서 초기 선택 아이템 설정
        }

        diet = new FragmentDiet();
        diabetes = new FragmentDiabetes();
        chatbot = new FragmentChatbot();
        graph = new FragmentGraph();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.tabdiet) {
                    replaceFragment(diet);
                    return true;
                } else if (itemId == R.id.tabdiabetes) {
                    replaceFragment(diabetes);
                    return true;
                } else if (itemId == R.id.tabchatbot) {
                    replaceFragment(chatbot);
                    return true;
                } else if (itemId == R.id.tabgraph) {
                    replaceFragment(graph);
                    return true;
                } else {
                    return false;
                }
            }
        });

        // 연결 이 안되요 이게 아직 해결이 안됌...
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.bardiet) {
                    bottomNavigationView.setSelectedItemId(R.id.tabdiet); // bottomNavigationView에서 tabdiet 선택
                } else if (id == R.id.bardiabetes) {
                    bottomNavigationView.setSelectedItemId(R.id.tabdiabetes); // bottomNavigationView에서 tabdiabetes 선택
                } else if (id == R.id.barchatbot) {
                    bottomNavigationView.setSelectedItemId(R.id.tabchatbot); // bottomNavigationView에서 tabchatbot 선택
                } else if (id == R.id.bargraph) {
                    bottomNavigationView.setSelectedItemId(R.id.tabgraph); // bottomNavigationView에서 tabgraph 선택
                } else if (id == R.id.setting_logout) {
                    logout();
                }

                // 드로어 닫기
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });

        // Bottom Sheet Dialog 제거 및 이미지 소스 대화상자 활성화
        fab.setOnClickListener(v -> showImageSourceDialog());

        // ActivityResultLauncher 초기화
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        startMainActivity2(uri);
                    } else {
                        Toast.makeText(this, "사진을 찍지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        pickGalleryLauncher = registerForActivityResult(
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

        // CollapsingToolbarLayout 및 AppBarLayout 초기화
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        appBarLayout = findViewById(R.id.app_bar_layout); // AppBarLayout ID에 맞게 수정

        // 제목 설정
        collapsingToolbarLayout.setTitle("카메라로! /n 음식을 인식해보세요");

        // AppBarLayout의 상태 변화에 따라 제목 변경
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
        });

    }
    //Outside onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 메뉴를 인플레이트하는 메서드 추가
        getMenuInflater().inflate(R.menu.toolbar_menu, menu); // toolbar_menu.xml 메뉴 리소스 인플레이트

        //toolbar_menu 알람, 더보기(아직 무슨기능 넣을지 모르겠음) 설정 여기서 해야함
        return true;
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지 소스 선택")
                .setItems(new String[]{"카메라", "갤러리"}, (dialog, which) -> {
                    if (which == 0) {
                        takePicture();
                    } else {
                        openGallery();
                    }
                });
        builder.create().show();
    }

    private void takePicture() {
        try {
            file = createFile();
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        takePictureLauncher.launch(intent);
    }

    private File createFile() {
        String filename = "capture.jpg";
        File outFile = new File(getFilesDir(), filename);
        Log.d("Main", "파일 경로 : " + outFile.getAbsolutePath());
        return outFile;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickGalleryLauncher.launch(intent);
    }

    private void startMainActivity2(Uri imageUri) {
        Intent intent2 = new Intent(this, DietCheckMenu.class);
        intent2.putExtra("imageUri", imageUri.toString());
        startActivity(intent2);
    }

    // 로그아웃 메소드
    private void logout() {
        Log.d("logout", "is it clicked?");
        // FirebaseAuth 인스턴스 가져오기
        FirebaseAuth.getInstance().signOut();

        // 로그인 화면으로 이동
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // 현재 액티비티 종료
    }
}