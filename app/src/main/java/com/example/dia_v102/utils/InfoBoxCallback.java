package com.example.dia_v102.utils;

import com.example.dia_v102.databaseF.InfoBox;

import java.util.List;

public interface InfoBoxCallback {
    void onInfoBoxesRetrieved(List<InfoBox> infoBoxes); // 데이터를 성공적으로 가져왔을 때 호출
    void onFailure(Exception e);  // 데이터 가져오기에 실패했을 때 호출
}
