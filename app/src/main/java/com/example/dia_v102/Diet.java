package com.example.dia_v102;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class Diet extends Fragment {

    /*fragment 안에 fragment 로 측, getChildFragmentManager() 사용하여,
    식단 입력 프래그먼트(DietLog)과 식단 데이터 프래그먼트(DietGraph) 고정바아래에 버튼 형식으로 넘어가게
     고정바에 들어가야 할것: 날짜 및 시간, 닉네임, 키, 몸무게(+몸무게변화추가버튼),프로필이미지 */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.activity_diet, container,false);

        TabLayout tabLayout = rootView.findViewById(R.id.store_fragment_tablayout); // Assuming you have a TabLayout with id tab_layout in your layout file
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

        return rootView;

    }
}