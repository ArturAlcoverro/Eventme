package com.androidprog2.eventme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private MaterialButton loginBtn;
    private MaterialButton signUpBtn;

    private TextInputLayout firstNameInput;
    private TextInputLayout lastNameInput;
    private TextInputLayout emailInput;
    private TextInputLayout passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent loginIntent = new Intent(this, LoginActivity.class);

        loginBtn = (MaterialButton) findViewById(R.id.registerToLoginBtn);
        signUpBtn = (MaterialButton) findViewById(R.id.signup_btn);
        firstNameInput = findViewById(R.id.signup_firstname);
        lastNameInput = findViewById(R.id.signup_lastname);
        emailInput = findViewById(R.id.signup_email);
        passwordInput = findViewById(R.id.signup_password);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", firstNameInput.getEditText().getText().toString());
                    jsonObject.put("last_name", lastNameInput.getEditText().getText().toString());
                    jsonObject.put("email", emailInput.getEditText().getText().toString());
                    jsonObject.put("password", passwordInput.getEditText().getText().toString());
                    //Fetch a la api

                    if(true){      //si el registre es correcte anem a pantalla de login
                        startActivity(loginIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
            }
        });
    }
}