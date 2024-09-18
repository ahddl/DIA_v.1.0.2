package com.example.dia_v102;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.dia_v102.databaseF.Func_UserInfo;
import com.example.dia_v102.utils.NicknameCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentDiabetes extends Fragment {
    TextView nickView;
    private String userNick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_diabetes, container,false);
        nickView = view.findViewById(R.id.nickName);
        FindNick(new NicknameCallback() {
            @Override
            public void onCallback(String nickname) {
                userNick = nickname;
                UserSet.setNickname(userNick);
                nickView.setText(userNick);
            }
        });
        /*
        TabLayout tabLayout = rootView.findViewById(R.id.store_fragment_tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                switch (tab.getPosition()) {
                    case 0:
                        transaction.replace(R.id.tab_layout_container2, new DiabetesLog());
                        break;
                    case 1:
                        transaction.replace(R.id.tab_layout_container2, new DiabetesGraph());
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
        */
        return view;

    }
    private void FindNick(NicknameCallback callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            callback.onCallback(null);
            return;
        }

        String userUID = user.getUid();
        UserSet.setUserId(userUID);
        Func_UserInfo userInfo = new Func_UserInfo();

        userInfo.getNick(userUID, callback);
    }
}