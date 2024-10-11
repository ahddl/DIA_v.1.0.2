package com.example.dia_v102;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dia_v102.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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
    private final int MAX_CLICK = 3;
    private int clicks = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_chatbot, container,false);
        ResponseView = view.findViewById(R.id.GPTRes);
        Button sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            clicks++;
            if(clicks <= MAX_CLICK){
                sendPromptToChatGPT();
            }
            else{
                Toast.makeText(requireContext(), "클릭을 너무 많이 하지 마세요.", Toast.LENGTH_SHORT).show();
            }

        });
        return view;

    }

    private void sendPromptToChatGPT() {
        String prompt = "식사 메뉴를 3가지 추천하라(번호와 이름만 출력)\n"
                +"그 아래에 영양소 섭취 상태에 근거한 추천 이유를 간략히 서술하라\n"
                + "아래의 정보를 참고하라"
                +"\n성별: "+UserSet.getGender()
                +"\n나이: "+UserSet.getAge()
                +"\n키: "+UserSet.getHeight()+"cm"
                +"\n몸무게: "+UserSet.getWeight()+"kg"
                +"\n당뇨병 정보: "+UserSet.getTypeStr()
                +"\n현재 시간: "+ DateUtil.HourNMin()
                +"\n오늘 평균 혈당: "+HealthSet.getBloodSugarAVG()
                +"\n최근 혈당: "+HealthSet.getBloodSugarRecent()
                ;
        /*
                +"\n상태: "
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
            Log.d("GPT_JSON", Objects.requireNonNull(e.getMessage()));
        }

        RequestBody body;
        body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

        // API 요청 빌드
        Request request;
        request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + API_KEY)//여기 api-key 추가.
                .post(body)
                .build();

        // 비동기 요청 전송
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("client_req", Objects.requireNonNull(e.getMessage()));
                updateUI("Failed to get response");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = (response.body() != null ?  response.body().string() : "");

                    try {
                        // 응답 처리
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray choices = jsonResponse.getJSONArray("choices");
                        String chatGptResponse = choices.getJSONObject(0).getJSONObject("message").getString("content");

                        // UI 업데이트
                        updateUI(chatGptResponse);
                    } catch (Exception e) {
                        Log.d("Response", Objects.requireNonNull(e.getMessage()));
                    }
                } else {
                    updateUI("Error: " + response.message());
                }
            }
        });
    }

    // UI 스레드-TextView 업데이트
    private void updateUI(String message) {
        mainHandler.post(() -> ResponseView.setText(message));
    }
}