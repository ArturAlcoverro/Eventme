package com.androidprog2.eventme.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.Event;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.presentation.adapters.EventsListAdapter;
import com.androidprog2.eventme.presentation.fragments.ProfileFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

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
    private BottomSheetBehavior<ConstraintLayout> bottomSheet;
    private ConstraintLayout constraintLayout;
    private RadioGroup radioGroup;
    private RadioButton buttonAll;
    private RadioButton buttonDone;
    private RadioButton buttonTodo;
    private RadioButton buttonFinished;
    private RadioButton buttonStarted;
    private RadioButton buttonToGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);

        backArrowBtn = findViewById(R.id.arrowLeft);
        filterBtn = findViewById(R.id.filter);
        profileName = findViewById(R.id.usernameTitle);
        recyclerView = findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        constraintLayout = findViewById(R.id.bottomSheet);
        bottomSheet = BottomSheetBehavior.from(constraintLayout);
        radioGroup = findViewById(R.id.radioGroup);
        buttonAll = findViewById(R.id.radio_button_1);
        buttonFinished = findViewById(R.id.radio_button_2);
        buttonStarted = findViewById(R.id.radio_button_3);
        buttonToGetStarted = findViewById(R.id.radio_button_4);
        buttonDone = findViewById(R.id.radio_button_5);
        buttonTodo = findViewById(R.id.radio_button_6);

        bottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        Intent intent = getIntent();
        id = intent.getIntExtra(ProfileFragment.EXTRA_ID, 0);
        String type = intent.getStringExtra(ProfileFragment.EXTRA_TYPE);
        String name = intent.getStringExtra(ProfileFragment.EXTRA_NAME);
        profileName.setText(name);

        if(type.equals("EXTRA_CREATED")){
            loadCreatedEvents();
        } else if(type.equals("EXTRA_ASSISTANCE")){
            loadAssistanceEvents();
            buttonFinished.setVisibility(View.GONE);
            buttonStarted.setVisibility(View.GONE);
            buttonToGetStarted.setVisibility(View.GONE);
            buttonDone.setVisibility(View.VISIBLE);
            buttonTodo.setVisibility(View.VISIBLE);
        }

        backArrowBtn.setOnClickListener(v -> { finish(); });
        filterBtn.setOnClickListener(v -> {
            if (bottomSheet.getState() == BottomSheetBehavior.STATE_HIDDEN){
                bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            }else if(bottomSheet.getState() == BottomSheetBehavior.STATE_EXPANDED){
                bottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(type.equals("EXTRA_CREATED")){
                if(buttonAll.isChecked()) loadCreatedEvents();
                if(buttonFinished.isChecked()) loadCreatedFinishedEvents();
                if(buttonStarted.isChecked()) loadCreatedCurrentEvents();
                if(buttonToGetStarted.isChecked()) loadCreatedFutureEvents();
            }else if(type.equals("EXTRA_ASSISTANCE")){
                if(buttonAll.isChecked()) loadAssistanceEvents();
                if(buttonDone.isChecked()) loadAssistanceFinishedEvents();
                if(buttonTodo.isChecked()) loadAssistanceFutureEvents();
            }
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
                                System.out.println(assistances.size() + "aqui aqui mira aqui");
                                adapter = new EventsListAdapter(assistances, getApplicationContext());
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
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadAssistanceFutureEvents() {
        CallSingelton
                .getInstance()
                .getUserAssistancesFuture(id, new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> assistances = response.body();
                                System.out.println(assistances.size() + "aqui aqui mira aqui");
                                adapter = new EventsListAdapter(assistances, getApplicationContext());
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
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadAssistanceFinishedEvents() {
        CallSingelton
                .getInstance()
                .getUserAssistancesFinished(id, new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> assistances = response.body();
                                System.out.println(assistances.size() + "aqui aqui mira aqui");
                                adapter = new EventsListAdapter(assistances, getApplicationContext());
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
                                adapter = new EventsListAdapter(created, getApplicationContext());
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
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCreatedFutureEvents() {
        CallSingelton
                .getInstance()
                .getUserEventsCreatedFuture(id, new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> created = response.body();
                                adapter = new EventsListAdapter(created, getApplicationContext());
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
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCreatedFinishedEvents() {
        CallSingelton
                .getInstance()
                .getUserEventsCreatedFinished(id, new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> created = response.body();
                                adapter = new EventsListAdapter(created, getApplicationContext());
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
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCreatedCurrentEvents() {
        CallSingelton
                .getInstance()
                .getUserEventsCreatedCurrent(id, new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> created = response.body();
                                adapter = new EventsListAdapter(created, getApplicationContext());
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
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}