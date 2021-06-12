package com.androidprog2.eventme.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.Event;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.presentation.adapters.EventsListAdapter;
import com.androidprog2.eventme.presentation.fragments.ProfileFragment;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserEventsActivity extends AppCompatActivity {

    private int id;

    private ImageButton backArrowBtn;
    private ImageButton filterBtn;
    private TextView profileName;
    private RecyclerView recyclerView;
    private EventsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);

        backArrowBtn = findViewById(R.id.arrowLeft);
        filterBtn = findViewById(R.id.filter);
        profileName = findViewById(R.id.usernameTitle);
        recyclerView = findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent intent = getIntent();
        id = intent.getIntExtra(ProfileFragment.EXTRA_ID, 0);
        String type = intent.getStringExtra(ProfileFragment.EXTRA_TYPE);
        String name = intent.getStringExtra(ProfileFragment.EXTRA_NAME);
        profileName.setText(name);

        if(type.equals("EXTRA_CREATED")){
            loadCreatedEvents();
        } else if(type.equals("EXTRA_ASSISTANCE")){
            loadAssistanceEvents();
        }

        backArrowBtn.setOnClickListener(v -> { finish(); });
        filterBtn.setOnClickListener(v -> {
          //load filter
        });
    }

    private void loadAssistanceEvents() {
        CallSingelton
                .getInstance()
                .getUserAssistances(id, new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> assistances = response.body();
                                if(!assistances.isEmpty()){
                                    adapter = new EventsListAdapter(assistances, getApplicationContext());
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
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCreatedEvents() {
        CallSingelton
                .getInstance()
                .getUserEventsCreated(id, new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> created = response.body();
                                if(!created.isEmpty()){
                                    adapter = new EventsListAdapter(created, getApplicationContext());
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
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}