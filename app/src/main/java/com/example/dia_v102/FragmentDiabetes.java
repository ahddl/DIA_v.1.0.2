package com.example.dia_v102;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.databaseF.Func_InfoBox;
import com.example.dia_v102.databaseF.Func_UserInfo;
import com.example.dia_v102.databaseF.InfoBox;
import com.example.dia_v102.utils.DateUtil;
import com.example.dia_v102.utils.NicknameCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FragmentDiabetes extends Fragment {
    TextView nickView;
    private String userNick;
    private Func_InfoBox infoBox;
    private Dialog dialog;
    private final FirebaseUser CurrUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userID;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private BSAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_diabetes, container,false);
        Func_InfoBox FinfoBox = new Func_InfoBox();

        nickView = view.findViewById(R.id.nickName);
        recyclerView = view.findViewById(R.id.recycler_vieww);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FindNick(new NicknameCallback() {
            @Override
            public void onCallback(String nickname) {
                userNick = nickname;
                UserSet.setNickname(userNick);
                nickView.setText(userNick);
            }
        });
        /*
        FloatingActionButton fab = view.findViewById(R.id.fab);
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_layout);
        // Dialog 중앙에 위치시키기
        dialog.setOnShowListener(dialogInterface -> {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
            }
        });
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        Spinner dropdownSelect = dialog.findViewById(R.id.dropdown_menu);
        Button dButton_save = dialog.findViewById(R.id.save_button);
        EditText sugarBlood = dialog.findViewById(R.id.blood_sugar);
        dButton_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 버튼 클릭 시 실행할 동작 정의
                String tag2 = dropdownSelect.getSelectedItem().toString();
                double sugar = Double.parseDouble(sugarBlood.getText().toString());
                FinfoBox.saveInfoBox(CurrUser.getUid(), null, HourNMin(), "혈당", tag2, sugar);
                Toast.makeText(requireContext(), "혈당 데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show();

                dialog.dismiss();  // Dialog 닫기

            }
        });

         */


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

    private String HourNMin(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        return String.format(Locale.getDefault(), "%02d-%02d", hour, min);
    }

    private void loadDiabeteData(String date) {
        infoBox = new Func_InfoBox();
        userID = CurrUser.getUid();
        executorService.execute(() -> {
            infoBox.loadInfoBox(userID, date, new Func_InfoBox.OnDataReceivedListener(){
                @Override
                public void onDataReceived(List<InfoBox> infoBoxList){
                    Log.d("BoxOut", "Success");

                    for (InfoBox infoBox : infoBoxList) {
                        Log.d("BoxOut", "InfoBox Data: " + infoBox.getTime()); // infoBox의 toString() 메서드를 사용하여 데이터를 출력
                    }
                    mainThreadHandler.post(()->{
                       adapter = new BSAdapter(infoBoxList);
                       recyclerView.setAdapter(adapter);
                    });
                }

                @Override
                public void onDataFailed(Exception exception) {
                    Log.d("BoxOut", exception.getMessage());
                }
            });
        });

    }
}
