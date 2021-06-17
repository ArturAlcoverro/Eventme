package com.androidprog2.eventme.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.google.android.material.button.MaterialButton;

public class StartActivity extends AppCompatActivity {
    private MaterialButton accessBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent = new Intent(this, LoginActivity.class);
        Intent mainIntent = new Intent(this, MainActivity.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.SP_DARKMODE, MODE_PRIVATE);
        boolean darkmode = sharedPreferences.getBoolean(SettingsActivity.SP_DARKMODE, true);

        if (darkmode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);


        if (loadData()!=null){
            startActivity(mainIntent);
        }

        accessBtn = findViewById(R.id.accessButton);
        accessBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    private String loadData(){
        String message;

        SharedPreferences sharedPreferences = getSharedPreferences(CallSingelton.TOKEN, MODE_PRIVATE);
        message= sharedPreferences.getString(CallSingelton.TOKEN,null);
        if (message!=null){
            Log.d(CallSingelton.TOKEN,message);
            CallSingelton.getInstance().setToken(message);
        }
        return message;
    }
}
