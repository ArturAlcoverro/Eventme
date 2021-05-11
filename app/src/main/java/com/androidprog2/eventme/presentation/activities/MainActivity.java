package com.androidprog2.eventme.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.presentation.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        homeFragment = new HomeFragment();
        homeFragment = new HomeFragment();
        homeFragment = new HomeFragment();
        homeFragment = new HomeFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, homeFragment, homeFragment.getTag())
                .commit();
    }
}