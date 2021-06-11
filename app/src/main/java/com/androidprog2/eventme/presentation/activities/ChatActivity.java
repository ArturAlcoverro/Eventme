package com.androidprog2.eventme.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.androidprog2.eventme.R;
import com.androidprog2.eventme.VolleySingleton;
import com.androidprog2.eventme.business.Message;
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.presentation.adapters.ChatListAdapter;
import com.androidprog2.eventme.presentation.adapters.MessageListAdapter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements Callback<List<Message>> {

    private User user;
    private MessageListAdapter adapter;
    private TextView userName;
    private ImageView userImage;
    private ImageView arrowBack_btn;
    private EditText textToSend;
    private RecyclerView recyclerView;
    private ImageButton send_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userName = findViewById(R.id.nameProfile);
        userImage = findViewById(R.id.imageProfile);
        arrowBack_btn = findViewById(R.id.xat_back_arrow);
        textToSend = findViewById(R.id.xat_text_send);
        send_btn = findViewById(R.id.xat_send_button);
        recyclerView = findViewById(R.id.recyclerViewChat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent intent = getIntent();
        int id = intent.getIntExtra(ChatListAdapter.EXTRA_ID, 0);
        String name = intent.getStringExtra(ChatListAdapter.EXTRA_NAME);
        String last_name = intent.getStringExtra(ChatListAdapter.EXTRA_LAST_NAME);
        String email = intent.getStringExtra(ChatListAdapter.EXTRA_EMAIL);
        String image = intent.getStringExtra(ChatListAdapter.EXTRA_IMAGE);
        this.user = new User(id, image, name, last_name, email, null);

        userName.setText(this.user.getFull_name());
        setImage();

        CallSingelton
                .getInstance()
                .getMessages(this.user.getId(), this);

        arrowBack_btn.setOnClickListener(v -> { finish(); });
        send_btn.setOnClickListener(v -> { sendMessage(); });
    }

    private void setImage() {
        String url = "http://puigmal.salle.url.edu/img/" + this.user.getImage();
        ImageLoader imageLoader = VolleySingleton.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null){
                    userImage.setImageBitmap(response.getBitmap());
                }
            }

            public void onErrorResponse(VolleyError error) {
                userImage.setImageResource(R.drawable.avatar_profile);
            }
        });
    }

    private void sendMessage() {
    }

    @Override
    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
        if (response.isSuccessful()) {
            if (response.code() == 200) {
                List<Message> messages = (List<Message>) response.body();
                if(!messages.isEmpty()){
                    /*for (int i = 0; i < messages.size(); i++) {
                        Message message = messages.get(i);
                        if(message.getUser_id_send() == this.user.getId()){

                        }else{

                        }
                    }*/
                    adapter = new MessageListAdapter(messages, this.user, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
            }
        } else {
            try {
                Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<List<Message>> call, Throwable t) {
        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}