package com.androidprog2.eventme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class StartActivity extends AppCompatActivity {
    private MaterialButton accessBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent = new Intent(this, LoginActivity.class);

        accessBtn = (MaterialButton) findViewById(R.id.accessButton);
        accessBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }
}