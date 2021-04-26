package com.codepath.iClaim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bot.codepath.Chatbot;
import com.bot.codepath.ChatbotActivity;
import com.bot.codepath.ChatbotSettings;
import com.bot.codepath.DialogflowCredentials;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chatFab = findViewById(R.id.chatFab);
        getStartedButton=findViewById(R.id.startButton);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
    }

    public void openChatbot(View view) {
        DialogflowCredentials.getInstance().setInputStream(getResources().openRawResource(R.raw.auth));

        ChatbotSettings.getInstance().setChatbot( new Chatbot.ChatbotBuilder()
                .setShowMic(true) // False by Default, True if you want to use Voice input from the user to chat
                .build());
        Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
        Bundle bundle = new Bundle();

        // provide a UUID for your session with the Dialogflow agent
        bundle.putString(ChatbotActivity.SESSION_ID, UUID.randomUUID().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}