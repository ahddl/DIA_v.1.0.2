package com.example.dia_v102;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentChatbot extends Fragment {
    private TextView ResponseView;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    protected String API_KEY = "API KEY PUTIN";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_chatbot, container,false);
        ResponseView = view.findViewById(R.id.GPTRes);
        Button sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendPromptToChatGPT();
            }
        });
        return view;

    }

    private void sendPromptToChatGPT() {
        String prompt = "식사 메뉴를 3~5가지 추천해 줘.\n";
        /*
                + "아래의 정보를 참고하라\n"
                +"성별: "+UserSet.getGender()
                +"나이: "+UserSet.getAge()
                +"키: "+UserSet.getHeight()+"cm"
                +"몸무게: "+UserSet.getWeight()+"kg"
                +"현재시간: "
                +"오늘 영양소 섭취"
                +"오늘 평균 혈당"
                +"최근 혈당"
         */
        OkHttpClient client = new OkHttpClient();

        // 요청 본문 생성
        JSONObject json = new JSONObject();
        try {
            json.put("model", "gpt-3.5-turbo");
            JSONArray messages;
            messages = new JSONArray();
            messages.put(new JSONObject().put("role", "user").put("content", prompt));
            json.put("messages", messages);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body;
        body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

        // API 요청 빌드
        Request request;
        request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + API_KEY)//여기 api-key 추가해야함.
                .post(body)
                .build();

        // 비동기 요청 전송
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                updateUI("Failed to get response");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        // 응답 처리
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray choices = jsonResponse.getJSONArray("choices");
                        String chatGptResponse = choices.getJSONObject(0).getJSONObject("message").getString("content");

                        // UI 업데이트
                        updateUI(chatGptResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    updateUI("Error: " + response.message());
                }
            }
        });
    }

    // UI 스레드에서 TextView 업데이트
    private void updateUI(String message) {
        mainHandler.post(() -> ResponseView.setText(message));
    }
}