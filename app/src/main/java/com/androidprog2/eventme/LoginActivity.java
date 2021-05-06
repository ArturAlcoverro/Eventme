package com.androidprog2.eventme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton signUpBtn;
    private MaterialButton loginBtn;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent registerIntent = new Intent(this, RegisterActivity.class);
        Intent mainIntent = new Intent(this, MainActivity.class);

        textInputLayoutEmail = findViewById(R.id.login_nickname);
        textInputLayoutPassword = findViewById(R.id.login_password);
        signUpBtn = (MaterialButton) findViewById(R.id.loginToRegisterBtn);
        loginBtn = (MaterialButton) findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", textInputLayoutEmail.getEditText().getText().toString());
                    jsonObject.put("password", textInputLayoutPassword.getEditText().getText().toString());
                    //Fetch a la api endpoint login

                    if(true){   //si el login es correcte
                        //startActivity(mainIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registerIntent);
            }
        });
    }
}