package com.androidprog2.eventme.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.persistance.API.APIConnector;
import com.androidprog2.eventme.persistance.API.UserDAO;
import com.androidprog2.eventme.business.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}