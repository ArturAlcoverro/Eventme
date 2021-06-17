package com.androidprog2.eventme.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.google.android.material.switchmaterial.SwitchMaterial;


public class SettingsActivity extends AppCompatActivity {

    public static final String SP_DARKMODE = "DARMODE";

    private LinearLayout mTheme;
    private LinearLayout mLogout;
    private ImageButton mBackBtn;
    private SwitchMaterial mThemeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mBackBtn = findViewById(R.id.arrowLeft);
        mTheme = findViewById(R.id.settings_theme);
        mLogout = findViewById(R.id.settings_logout);
        mThemeSwitch = findViewById(R.id.settings_theme_switch);

        mTheme.setOnClickListener(v -> {
            mThemeSwitch.setChecked(!mThemeSwitch.isChecked());
        });

        mLogout.setOnClickListener(v -> {
            logOut();
        });

        mBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        SharedPreferences sp = getSharedPreferences(SettingsActivity.SP_DARKMODE, MODE_PRIVATE);
        boolean darkmode = sp.getBoolean(SettingsActivity.SP_DARKMODE, true);

        mThemeSwitch.setChecked(darkmode);

        mThemeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences sharedPreferences = getSharedPreferences(SP_DARKMODE, MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SP_DARKMODE, isChecked);
            editor.apply();

            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        });

    }

    private void logOut() {
        deleteToken();
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void deleteToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(CallSingelton.TOKEN, MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CallSingelton.TOKEN);
        editor.commit();
    }
}