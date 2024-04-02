package huynhph30022.fpoly.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UserNameActivity extends AppCompatActivity {

    private EditText etUsername;
    private Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_name);

        etUsername = findViewById(R.id.etUsername);
        btnProceed = findViewById(R.id.btnProceed);

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = s.toString();
                btnProceed.setEnabled(!username.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                if (!username.isEmpty()) {
                    Intent intent = new Intent(UserNameActivity.this, ChatActivity.class);
                    intent.putExtra(ChatActivity.USERNAME, username);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        etUsername.requestFocus();
    }
}