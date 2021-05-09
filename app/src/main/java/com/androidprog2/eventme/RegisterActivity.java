package com.androidprog2.eventme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androidprog2.eventme.business.User;
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
    private TextInputLayout passwordInputRepeat;

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
        passwordInputRepeat = findViewById(R.id.signup_repeat_password);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = firstNameInput.getEditText().getText().toString();
                String lastname = lastNameInput.getEditText().getText().toString();
                String email = emailInput.getEditText().getText().toString();
                String password = passwordInput.getEditText().getText().toString();
                String repeatPsswd = passwordInputRepeat.getEditText().getText().toString();

                if(password.equals(repeatPsswd) && password.length() >= 8 && !name.isEmpty()
                        && !lastname.isEmpty() && !email.isEmpty()){
                    User user = new User(0, null, name, lastname, email, password);
                    //Fetch a la api

                    if(true){      //si el registre es correcte anem a pantalla de login
                        startActivity(loginIntent);
                    }
                }else{
                    //mostrar error ja que els dos passwords no coincideixen
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