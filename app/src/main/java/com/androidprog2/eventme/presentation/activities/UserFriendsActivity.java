package com.androidprog2.eventme.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.presentation.adapters.FriendsListAdapter;
import com.androidprog2.eventme.presentation.fragments.ProfileFragment;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFriendsActivity extends AppCompatActivity {

    private int id;
    private ImageButton arrowBackBtn;
    private RecyclerView recyclerView;
    private FriendsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friends);

        arrowBackBtn = findViewById(R.id.arrowLeft);
        arrowBackBtn.setOnClickListener(v -> {
            finish();
        });
        recyclerView = findViewById(R.id.recyclerViewFriends);

        Intent intent = getIntent();
        id = intent.getIntExtra(ProfileFragment.EXTRA_ID, 0);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        callAdapter();
    }

    private void callAdapter() {
        CallSingelton
                .getInstance()
                .getUserFriends(id, new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<User> friends = (List<User>) response.body();
                                System.out.println(friends.size() + "ei auqi esan els aics");
                                CallSingelton
                                        .getInstance()
                                        .getUserFriendsRequests(new Callback<List<User>>() {
                                            @Override
                                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                                if (response.isSuccessful()) {
                                                    if (response.code() == 200) {
                                                        List<User> requests = response.body();
                                                        System.out.println(requests.size() + "joe quantes requests");
                                                        adapter = new FriendsListAdapter(friends, requests, getApplicationContext());
                                                        recyclerView.setAdapter(adapter);
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
                                            public void onFailure(Call<List<User>> call, Throwable t) {
                                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
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
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}