package huynhph30022.fpoly.chatapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketHandler {
    private Socket socket;
    private final MutableLiveData<Chat> _onNewChat = new MutableLiveData<>();
    public LiveData<Chat> onNewChat = _onNewChat;

    private static final String SOCKET_URL = "http://10.0.2.2:3000/";

    public SocketHandler() {
        try {
            socket = IO.socket(SOCKET_URL);
            socket.connect();

            registerOnNewChat();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void registerOnNewChat() {
        socket.on(CHAT_KEYS.BROADCAST, args -> {
            if (args != null && args.length > 0) {
                Object data = args[0];
                Log.d("DATADEBUG", String.valueOf(data));
                if (data != null && !String.valueOf(data).isEmpty()) {
                    Chat chat = new Gson().fromJson(String.valueOf(data), Chat.class);
                    _onNewChat.postValue(chat);
                }
            }
        });
    }

    public void disconnectSocket() {
        if (socket != null) {
            socket.disconnect();
            socket.off();
        }
    }

    public void emitChat(Chat chat) {
        String jsonStr = new Gson().toJson(chat, Chat.class);
        socket.emit(CHAT_KEYS.NEW_MESSAGE, jsonStr);
    }

    private static class CHAT_KEYS {
        static final String NEW_MESSAGE = "new_message";
        static final String BROADCAST = "broadcast";
    }
}
