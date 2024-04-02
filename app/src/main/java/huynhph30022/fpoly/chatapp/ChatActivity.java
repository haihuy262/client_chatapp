package huynhph30022.fpoly.chatapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private SocketHandler socketHandler;
    private ChatAdapter chatAdapter;
    private RecyclerView rvChat;
    private EditText etMsg;
    private Button btnSend;
    private List<Chat> chatList = new ArrayList<>();
    private String userName = "";

    public static final String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        userName = getIntent().getStringExtra(USERNAME);

        if (userName.isEmpty()) {
            finish();
        } else {
            socketHandler = new SocketHandler();

            rvChat = findViewById(R.id.rvChat);
            etMsg = findViewById(R.id.etMsg);
            btnSend = findViewById(R.id.btnSend);

            chatAdapter = new ChatAdapter();
            rvChat.setLayoutManager(new LinearLayoutManager(this));
            rvChat.setAdapter(chatAdapter);

            btnSend.setOnClickListener(view -> {
                String message = etMsg.getText().toString();
                if (!message.isEmpty()) {
                    Chat chat = new Chat(0, userName, message, false);
                    socketHandler.emitChat(chat);
                    etMsg.setText("");
                }
            });

            socketHandler.onNewChat.observe(this, chat -> {
                chat.setSelf(chat.getUsername().equals(userName));
                chatList.add(chat);
                chatAdapter.submitChat(chatList);
                rvChat.scrollToPosition(chatList.size() - 1);
            });
        }
    }

    @Override
    protected void onDestroy() {
        socketHandler.disconnectSocket();
        super.onDestroy();
    }
}