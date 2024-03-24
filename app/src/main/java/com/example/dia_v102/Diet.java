package com.example.dia_v102;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Diet extends Fragment {

    /*fragment 안에 fragment 로 측, getChildFragmentManager() 사용하여,
    식단 입력 프래그먼트(DietLog)과 식단 데이터 프래그먼트(DietGraph) 고정바아래에 버튼 형식으로 넘어가게
     고정바에 들어가야 할것: 날짜 및 시간, 닉네임, 키, 몸무게(+몸무게변화추가버튼),프로필이미지 */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.activity_diet, container,false);

    }
}