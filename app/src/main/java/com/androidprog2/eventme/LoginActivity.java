package com.androidprog2.eventme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
            @SuppressLint("ResourceAsColor")
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

        textInputLayoutEmail.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    textInputLayoutEmail.setError(null);
                    textInputLayoutEmail.setErrorEnabled(false);
                }
            }
        });

        textInputLayoutPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    textInputLayoutPassword.setError(null);
                    textInputLayoutPassword.setErrorEnabled(false);
                }
            }
        });

        textInputLayoutPassword.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND && event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    loginBtn.performClick();
                    handled = true;
                }
                return handled;
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}