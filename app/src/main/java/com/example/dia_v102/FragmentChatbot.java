package com.example.dia_v102;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dia_v102.utils.DateUtil;
import com.example.dia_v102.utils.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentChatbot extends Fragment {
    private RecyclerView recyclerView;
    private Adapter_chatbot messageAdapter;
    private final List<Message> messageList = new ArrayList<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    protected String API_KEY = "API_KEY";
    private final int MAX_CLICK = 7;
    private int clicks = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_chatbot, container,false);

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerView_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        messageAdapter = new Adapter_chatbot(messageList);
        recyclerView.setAdapter(messageAdapter);

        // 버튼 초기화 및 클릭 리스너 설정
        Button sendButton1 = view.findViewById(R.id.sendButton);
        Button sendButton2 = view.findViewById(R.id.exampleButton1);

        String[] prompt= new String[2];
        prompt[0] = "식사 메뉴를 3가지 추천하라(번호와 이름만 출력)\n"
                + "그 아래에 영양소 섭취 상태에 근거한 추천 이유를 간략히 서술하라\n"
                + "아래의 정보를 참고하라"
                + "\n성별: " + SetUser.getGender()
                + "\n나이: " + SetUser.getAge()
                + "\n키: " + SetUser.getHeight() + "cm"
                + "\n몸무게: " + SetUser.getWeight() + "kg"
                + "\n당뇨병 정보: " + SetUser.getTypeStr()
                + "\n현재 시간: " + DateUtil.HourNMin()
                + "\n오늘 평균 혈당: " + SetHealth.getBloodSugarAVG()
                + "\n최근 혈당: " + SetHealth.getBloodSugarRecent();
        prompt[1] = "충북대학교에 대해 설명해줘";

        sendButton1.setOnClickListener(v -> {
            clicks++;
            if (clicks <= MAX_CLICK) {
                addMessageToChat("사용자\n식단을 추천해 줘.", Message.USER_TYPE);
                sendPromptToChatGPT(prompt[0]);
            } else {
                Toast.makeText(requireContext(), "클릭을 너무 많이 하지 마세요.", Toast.LENGTH_SHORT).show();
            }
        });

        sendButton2.setOnClickListener(v -> {
            clicks++;
            if (clicks <= MAX_CLICK) {
                addMessageToChat("사용자\n 예시 질문", Message.USER_TYPE);
                sendPromptToChatGPT(prompt[1]);
            } else {
                Toast.makeText(requireContext(), "클릭을 너무 많이 하지 마세요.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void sendPromptToChatGPT(String prompt) {


        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("model", "gpt-3.5-turbo");
            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "user").put("content", prompt));
            json.put("messages", messages);
        } catch (Exception e) {
            Log.d("GPT_JSON", Objects.requireNonNull(e.getMessage()));
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("client_req", Objects.requireNonNull(e.getMessage()));
                updateUI("Failed to get response");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body() != null ? response.body().string() : "";

                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray choices = jsonResponse.getJSONArray("choices");
                        String chatGptResponse = choices.getJSONObject(0).getJSONObject("message").getString("content");

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

    // RecyclerView에 메시지 추가
    @SuppressLint("NotifyDataSetChanged")
    private void addMessageToChat(String messageText, int messageType) {
        Message message = new Message(messageText, messageType);
        messageList.add(message);
        mainHandler.post(() -> {
            messageAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageList.size() - 1); // 최근 메시지로 스크롤
        });
    }

    private void updateUI(String message) {
        addMessageToChat("챗봇\n" + message, Message.BOT_TYPE);
    }
}
