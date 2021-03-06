package com.androidprog2.eventme.presentation.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.persistance.API.UserDAO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton signUpBtn;
    private MaterialButton loginBtn;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private final boolean validation = false;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent registerIntent = new Intent(this, RegisterActivity.class);
        Intent mainIntent = new Intent(this, MainActivity.class);

        signUpBtn = findViewById(R.id.loginToRegisterBtn);
        loginBtn = findViewById(R.id.login_btn);

        textInputLayoutEmail = findViewById(R.id.login_nickname);
        Objects.requireNonNull(textInputLayoutEmail.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        textInputLayoutPassword = findViewById(R.id.login_password);
        Objects.requireNonNull(textInputLayoutPassword.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputLayoutEmail.getEditText().clearFocus();
                textInputLayoutPassword.getEditText().clearFocus();

                if (validateData()) {
                    String email = textInputLayoutEmail.getEditText().getText().toString();
                    String password = textInputLayoutPassword.getEditText().getText().toString();
                    User user = new User(0, null, null, null, email, password);
                    //Fetch a la api endpoint login
                    CallSingelton.getInstance().loginUser(user, new Callback<String>() {
                        @Override
                        public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {

                            Toast toast = null;
                            int i = response.code();
                            switch (response.code()) {

                                case 200:
                                    JSONObject jObject = null;
                                    try {
                                        jObject = new JSONObject(response.body());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        saveData(jObject.getString("accessToken"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(mainIntent);
                                    break;
                                case 400:
                                case 404:
                                case 500:
                                    toast = Toast.makeText(getApplicationContext(), getString(R.string.login_incorrect), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP, 0, 60);
                                    toast.show();
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Error message code: " + t.getMessage(), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
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

    private void saveData(String message) {
        SharedPreferences sharedPreferences = getSharedPreferences(CallSingelton.TOKEN, MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CallSingelton.TOKEN, message);
        editor.commit();
        CallSingelton.getInstance().setToken(message);
    }

    public boolean validateEmail(String email) {
        if (!email.isEmpty()) {
            if (isEmailValid(email)) {
                textInputLayoutEmail.setErrorEnabled(false);
                return true;
            }
            textInputLayoutEmail.setError(getString(R.string.login_email_syntax_error));
            return false;
        }
        textInputLayoutEmail.setError(getString(R.string.login_email_error));
        return false;
    }

    public boolean validatePassword(String password) {
        if (!password.isEmpty()) {
            textInputLayoutPassword.setErrorEnabled(false);
            return true;
        }
        textInputLayoutPassword.setError(getString(R.string.login_password_error));
        return false;
    }

    public boolean validateData() {
        boolean error = true;
        if (!validateEmail(textInputLayoutEmail.getEditText().getText().toString())) {
            error = false;
        }
        if (!validatePassword(textInputLayoutPassword.getEditText().getText().toString())) {
            error = false;
        }
        return error;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
