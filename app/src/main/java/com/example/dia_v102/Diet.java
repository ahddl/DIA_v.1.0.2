package com.example.dia_v102;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Diet extends Fragment {

    /*fragment 안에 fragment 로 측, getChildFragmentManager() 사용하여,
    식단 입력 프래그먼트(DietLog)과 식단 데이터 프래그먼트(DietGraph) 고정바아래에 버튼 형식으로 넘어가게
     고정바에 들어가야 할것: 날짜 및 시간, 닉네임, 키, 몸무게(+몸무게변화추가버튼),프로필이미지 */

    private CustomAdapter adapter;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    ImageButton gallerybutton;
    Uri photoUri;
    ImageView imageView;
    File file;
    ImageButton camerabutton;
    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.activity_diet, container, false);

        ImageButton camerabutton = view.findViewById(R.id.camerabutton);
        Button button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Menu.class);
                startActivity(intent);
            }
        });



        return view;
    }


}




    /*
    //갤러리 불러오기
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri fileUri = data.getData();
                        if (fileUri != null) {
                            ContentResolver resolver = getContentResolver();

                            try {
                                InputStream instream = resolver.openInputStream(fileUri);
                                Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                                gallerybutton.setImageBitmap(imgBitmap);

                                instream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

     */

    /*private void init(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_diet);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 임의의 데이터
        List<String> listTitle = Arrays.asList("아침", "점심", "저녁", "간식");
        *//*List<String> listContent = Arrays.asList( "여기 content에 들어갈 내용 쓰면됨");
        List<Integer> listResId = Arrays.asList(R.drawable.여기 이미지 들어갈것 불러오면 됨);*//*
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 함
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            *//*data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));*//*

            // 각 값이 들어간 data를 adapter에 추가함
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줌
        adapter.notifyDataSetChanged();*/

       /* TabLayout tabLayout = rootView.findViewById(R.id.store_fragment_tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                switch (tab.getPosition()) {
                    case 0:
                        transaction.replace(R.id.tab_layout_container, new DietLog());
                        break;
                    case 1:
                        transaction.replace(R.id.tab_layout_container, new DietGraph());
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return rootView;*/





 /* //===== 테스트를 위한 더미 데이터 생성 ===================
        ArrayList<String> testDataSet = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            testDataSet.add("TEST DATA" + i);
        }
        //========================================================

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_diet);

        //--- LayoutManager는 아래 3가지중 하나를 선택하여 사용 ---
        // 1) LinearLayoutManager()
        // 2) GridLayoutManager()
        // 3) StaggeredGridLayoutManager()
        //---------------------------------------------------------
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);  // LayoutManager 설정

        CustomAdapter customAdapter = new CustomAdapter(testDataSet);
        recyclerView.setAdapter(customAdapter); // 어댑터 설정

        return view; // View 반환
    }
}*/