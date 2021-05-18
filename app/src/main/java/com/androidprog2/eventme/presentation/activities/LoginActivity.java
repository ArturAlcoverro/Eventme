package com.androidprog2.eventme.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.androidprog2.eventme.business.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.androidprog2.eventme.R;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton signUpBtn;
    private MaterialButton loginBtn;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private boolean validation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent registerIntent = new Intent(this, RegisterActivity.class);
        Intent mainIntent = new Intent(this, MainActivity.class);

        signUpBtn = (MaterialButton) findViewById(R.id.loginToRegisterBtn);
        loginBtn = (MaterialButton) findViewById(R.id.login_btn);

        textInputLayoutEmail = findViewById(R.id.login_nickname);
        textInputLayoutEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textInputLayoutPassword = findViewById(R.id.login_password);
        textInputLayoutPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputLayoutEmail.getEditText().clearFocus();
                textInputLayoutPassword.getEditText().clearFocus();

                String email = textInputLayoutEmail.getEditText().getText().toString();
                String password =  textInputLayoutPassword.getEditText().getText().toString();
                boolean errorControl = false;
                if(password.isEmpty()){
                    textInputLayoutPassword.setError(getString(R.string.login_password_error));
                    errorControl = true;
                }
                if(email.isEmpty()){
                    textInputLayoutEmail.setError(getString(R.string.login_email_error));
                    errorControl = true;
                } else if(!isEmailValid(email)){
                    textInputLayoutEmail.setError(getString(R.string.login_email_syntax_error));
                    errorControl = true;
                }

                if(!errorControl) {
                    User user = new User(0, null, null, null, email, password);
                    //Fetch a la api endpoint login

                    if (2==1) {   //si el login es correcte
                        startActivity(mainIntent);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_incorrect), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 60);
                        toast.show();
                    }
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

    public boolean validateEmail(CharSequence email){
        if(email.length() != 0){
            if(isEmailValid(email.toString())){
                return true;
            }else{
                return false;
            }
        }else{
            textInputLayoutEmail.setError(getString(R.string.login_email_error));
            return false;
        }
    }

    public boolean validatePassword(CharSequence password){
        if(password.length() != 0){
            return true;
        }
        return false;
    }

    public boolean validateData(){
        
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}