package com.androidprog2.eventme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

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
                firstNameInput.getEditText().clearFocus();
                lastNameInput.getEditText().clearFocus();
                emailInput.getEditText().clearFocus();
                passwordInput.getEditText().clearFocus();
                passwordInputRepeat.getEditText().clearFocus();

                String name = firstNameInput.getEditText().getText().toString();
                String lastname = lastNameInput.getEditText().getText().toString();
                String email = emailInput.getEditText().getText().toString();
                String password = passwordInput.getEditText().getText().toString();
                String repeatPsswd = passwordInputRepeat.getEditText().getText().toString();
                boolean errorControl = false;

                if (name.isEmpty()) {
                    firstNameInput.setError(getString(R.string.signup_name_error));
                    errorControl = true;
                }
                if (lastname.isEmpty()) {
                    lastNameInput.setError(getString(R.string.signup_lastname_error));
                    errorControl = true;
                }
                if (email.isEmpty()) {
                    emailInput.setError(getString(R.string.signup_email_error));
                    errorControl = true;
                } else if (!LoginActivity.isEmailValid(email)) {
                    emailInput.setError(getString(R.string.signup_email_syntax_error));
                    errorControl = true;
                }
                if (password.isEmpty()) {
                    passwordInput.setError(getString(R.string.signup_password_error));
                    errorControl = true;
                } else if (password.length() < 8) {
                    passwordInput.setError(getString(R.string.signup_password_regex));
                    errorControl = true;
                }
                if (!repeatPsswd.equals(password)) {
                    passwordInputRepeat.setError(getString(R.string.signup_password_compare));
                    errorControl = true;
                }

                if (!errorControl) {
                    User user = new User(0, null, name, lastname, email, password);
                    //Fetch a la api
                    if (2 == 1) {      //si el registre es correcte anem a pantalla de login
                        startActivity(loginIntent);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.signup_incorrect), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 60);
                        toast.show();
                    }
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
            }
        });

        firstNameInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    firstNameInput.setError(null);
                    firstNameInput.setErrorEnabled(false);
                }
            }
        });

        lastNameInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    lastNameInput.setError(null);
                    lastNameInput.setErrorEnabled(false);
                }
            }
        });

        emailInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    emailInput.setError(null);
                    emailInput.setErrorEnabled(false);
                }
            }
        });

        passwordInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    passwordInput.setError(null);
                    passwordInput.setErrorEnabled(false);
                }
            }
        });

        passwordInputRepeat.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    passwordInputRepeat.setError(null);
                    passwordInputRepeat.setErrorEnabled(false);
                }
            }
        });
    }
}