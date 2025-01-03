package com.example.dia_v102;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.Objects;

public class FragmentDiabetes extends Fragment {
    TextView nickView;
    TextView avgBlood, avgHbA1cView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_diabetes, container,false);
        avgBlood = view.findViewById(R.id.avgBlood);
        avgHbA1cView = view.findViewById(R.id.glycatedhemoglobin);
        nickView = view.findViewById(R.id.nickName);
        nickView.setText(SetUser.getNickname());
        TabLayout tabLayout = view.findViewById(R.id.tabView);

        // 탭 추가
        tabLayout.addTab(tabLayout.newTab().setText("혈당"));
        tabLayout.addTab(tabLayout.newTab().setText("당화혈색소"));

        // 첫 번째 탭(TabBloodsugar) 기본 선택.x
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.tab_layout_container, new TabBloodsugar(), "TabBloodsugar")
                    .commit();
            // 첫 번째 탭 선택 상태로 만들기
            Objects.requireNonNull(tabLayout.getTabAt(0)).select();
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

                // 현재 container 에서 들어 있는 fragment 찾아 제거
                Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.tab_layout_container);
                if (currentFragment != null) {
                    transaction.remove(currentFragment);
                    transaction.commit();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        // 데이터 변경 리스너 등록 (여기서 HealthSet 변경 사항을 감지-> notify 함수를 호출-> UI 값 갱신)
        SetHealth.addBloodSugarChangeListener(this::updateAvgBlood);
        SetHealth.addHbA1cChangeListener(this::updateAVGHbA1c);
        return view;
    }

    // 평균 혈당량 업데이트 메서드
    private void updateAvgBlood() {
        double avgBloodSugar = SetHealth.getBloodSugarAVG();
        avgBlood.setText(String.format(Locale.getDefault(),"평균 혈당량 %.2f mg/dL", avgBloodSugar));
    }
    private void updateAVGHbA1c(){
        double avgHbA1c = SetHealth.getHbA1cAVG();
        avgHbA1cView.setText(String.format(Locale.getDefault(), "평균 당화혈색소 %.2f", avgHbA1c));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 리스너 제거
        SetHealth.removeBloodSugarChangeListener(this::updateAvgBlood);
        SetHealth.removeHbA1CChangeListener(this::updateAVGHbA1c);
    }

}
