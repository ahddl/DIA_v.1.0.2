package com.example.dia_v102;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dia_v102.databaseF.Func_InfoBox;
import com.example.dia_v102.databaseF.Func_UserInfo;
import com.example.dia_v102.databaseF.InfoBox;
import com.example.dia_v102.utils.DateUtil;
import com.example.dia_v102.utils.NicknameCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FragmentDiabetes extends Fragment {
    TextView nickView;
    private String userNick;
    private Func_InfoBox infoBox;
    private final FirebaseUser CurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userID;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TextView AvgBlood;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_diabetes, container,false);

        nickView = view.findViewById(R.id.nickName);
        AvgBlood = view.findViewById(R.id.averblood);
        FindNick(new NicknameCallback() {
            @Override
            public void onCallback(String nickname) {
                userNick = nickname;
                UserSet.setNickname(userNick);
                nickView.setText(userNick);
            }
        });
        TabLayout tabLayout = view.findViewById(R.id.tabdiabetes);

        // 탭 추가하기
        tabLayout.addTab(tabLayout.newTab().setText("혈당"));
        tabLayout.addTab(tabLayout.newTab().setText("당화혈색소"));

        // 첫 번째 탭을 기본으로 선택하고 TabBloodsugar 프래그먼트 표시
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.tab_layout_container, new TabBloodsugar(), "TabBloodsugar")
                    .commit();
            // 첫 번째 탭 선택 상태로 만들기
            tabLayout.getTabAt(0).select();
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                Log.d("FragmentDiabetes", "Tab " + tab.getPosition() + " selected");
                switch (tab.getPosition()) {
                    case 0:
                        Log.d("FragmentDiabetes", "Tab 0 selected");  // 로그 추가
                        transaction.replace(R.id.tab_layout_container, new TabBloodsugar());
                        break;
                    case 1:
                        Log.d("FragmentDiabetes", "Tab 1 selected");  // 로그 추가
                        transaction.replace(R.id.tab_layout_container, new TabGlycated());
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                // 현재 container에 들어있는 fragment를 찾아 제거
                Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.tab_layout_container);
                if (currentFragment != null) {
                    transaction.remove(currentFragment);
                    transaction.commit();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });


        loadDiabeteData(DateUtil.dateToString(new Date()));  // 데이터 로드

        return view;
    }

    private void FindNick(NicknameCallback callback) {
        if (CurrUser == null) {
            callback.onCallback(null);
            return;
        }

        userID = CurrUser.getUid();
        UserSet.setUserId(userID);
        Func_UserInfo userInfo = new Func_UserInfo();

        userInfo.getNick(userID, callback);
    }

    private void loadDiabeteData(String date) {
        infoBox = new Func_InfoBox();
        userID = CurrUser.getUid();

        executorService.execute(() -> {

            infoBox.loadInfoBox(userID, date, new Func_InfoBox.OnDataReceivedListener(){
                @Override
                public void onDataReceived(List<InfoBox> infoBoxList){
                    Log.d("BoxOut", "Success");
                    double sumValue = 0.0;
                    double avgValue;

                    for (InfoBox infoBox : infoBoxList) {
                        Log.d("BoxOut", "InfoBox Data: " + infoBox.getTime()); // infoBox의 toString() 메서드를 사용하여 데이터를 출력
                        sumValue = sumValue + infoBox.getValue();
                    }
                    avgValue = sumValue/infoBoxList.size();
                    AvgBlood.setText(String.format("평균 혈당량 %.2f mg/dL", avgValue));
                }

                @Override
                public void onDataFailed(Exception exception) {
                    Log.d("BoxOut", exception.getMessage());
                }
            });
        });

    }
}
