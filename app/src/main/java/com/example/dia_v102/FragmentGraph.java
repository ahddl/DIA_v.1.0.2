package com.example.dia_v102;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class FragmentGraph extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_graph, container,false);

        TabLayout tabLayout = rootView.findViewById(R.id.store_fragment_tablayout);

        //tabLayout.addTab(tabLayout.newTab().setText("식단 변화량"));
        //tabLayout.addTab(tabLayout.newTab().setText("혈당 변화량"));

        // 첫 번째 탭(TabBloodsugar) 기본 선택.x
        if (saveInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.tab_layout_container, new GraphDiet(), "GraphDiet")
                    .commit();
            // 첫 번째 탭 선택 상태로 만들기
            Objects.requireNonNull(tabLayout.getTabAt(0)).select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                switch (tab.getPosition()) {
                    case 0:
                        transaction.replace(R.id.tab_layout_container, new GraphDiet());
                        break;
                    case 1:
                        transaction.replace(R.id.tab_layout_container, new GraphDiabetes());
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.tab_layout_container);
                if (currentFragment != null) {
                    transaction.remove(currentFragment);
                    transaction.commit();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return rootView;

    }
}
