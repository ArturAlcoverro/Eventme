package com.androidprog2.eventme.presentation.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<<< HEAD:app/src/main/java/com/androidprog2/eventme/presentation/activities/StartActivity.java

import com.androidprog2.eventme.R;
========
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
>>>>>>>> feature/Arreglant_la_merda_del_david:app/src/main/java/com/androidprog2/eventme/StartActivity.java

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
