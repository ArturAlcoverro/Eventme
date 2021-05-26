package com.androidprog2.eventme.presentation.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements Callback<User> {

    private static final int REQUEST_GALLERY_IMAGE = 1;
    private static final String DEFAULT_IMG = "https://i.imgur.com/toKfoY6.png";

    private MaterialButton mLoginBtn;
    private MaterialButton mRegisterBtn;
    private MaterialButton mUploadImageBtn;
    private TextInputLayout mFirstNameInputLayout;
    private TextInputLayout mLastNameInputLayout;
    private TextInputLayout mEmailInputLayout;
    private TextInputLayout mPasswordInputLayout;
    private TextInputLayout mRepeatPasswordInputLayout;
    private ImageView mProfileImageView;
    private ProgressBar mProgressBar;
    private File mImageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterBtn = findViewById(R.id.signup_btn);
        mFirstNameInputLayout = findViewById(R.id.signup_firstname);
        mLastNameInputLayout = findViewById(R.id.signup_lastname);
        mEmailInputLayout = findViewById(R.id.signup_email);
        mPasswordInputLayout = findViewById(R.id.signup_password);
        mRepeatPasswordInputLayout = findViewById(R.id.signup_repeat_password);
        mUploadImageBtn = findViewById(R.id.upload_btn);
        mProfileImageView = findViewById(R.id.profile_image_view);
        mLoginBtn = findViewById(R.id.registerToLoginBtn);
        mProgressBar = findViewById(R.id.progress_bar);

        loadInputValidationTriggers();
        mRegisterBtn.setOnClickListener(v -> {
            signup();
        });
        mUploadImageBtn.setOnClickListener(v -> {
            selectImage();
        });
        mLoginBtn.setOnClickListener(v -> {
            goLogin();
        });
    }

    private void selectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
    }

    private void goLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK) {
            Uri uriData = data.getData();
            try {
                // Creating file
                mImageFile = null;
                try {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    ContentResolver cr = getContentResolver();
                    mImageFile = createImageFile(mime.getExtensionFromMimeType(cr.getType(uriData)));
                } catch (IOException ex) {
                    Log.d("ERR", "Error occurred while creating the file");
                }
                mProfileImageView.setImageURI(uriData);
                InputStream inputStream = getContentResolver().openInputStream(uriData);
                FileOutputStream fileOutputStream = new FileOutputStream(mImageFile);
                // Copying
                copyStream(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();

            } catch (Exception e) {
                Log.d("ERR", "onActivityResult: " + e.toString());
            }
        }
    }

    private File createImageFile(String extension) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,
                "." + extension,
                storageDir
        );
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void signup() {
        String firstName = mFirstNameInputLayout.getEditText().getText().toString();
        String lastName = mLastNameInputLayout.getEditText().getText().toString();
        String email = mEmailInputLayout.getEditText().getText().toString();
        String password = mPasswordInputLayout.getEditText().getText().toString();
        String rePassword = mRepeatPasswordInputLayout.getEditText().getText().toString();

        boolean err = validateData(firstName, lastName, email, password, rePassword);
        loading(true);

        if (!err) {
            if (mImageFile != null)
                CallSingelton
                        .getInstance()
                        .insertUser(firstName, lastName, mImageFile, email, password, this);
            else
                CallSingelton
                        .getInstance()
                        .insertUser(firstName, lastName, DEFAULT_IMG, email, password, this);

        } else {
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(Call call, Response response) {
        loading(false);

        if (response.isSuccessful()) {
            if (response.code() == 201) {
                goLogin();
            }
        } else {
            try {
                Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private boolean validateData(String firstName, String lastName, String email, String password, String rePassword) {
        boolean error = false;
        if (validateFirstName(firstName)) error = true;
        if (validateLastName(lastName)) error = true;
        if (validateEmail(email)) error = true;
        if (validatePassword(password)) error = true;
        if (validateRePassword(rePassword)) error = true;

        return error;
    }

    private boolean validateFirstName(String firstName) {
        if (firstName.isEmpty()) {
            mFirstNameInputLayout.setError("Missing first name field");
            return true;
        }
        mFirstNameInputLayout.setErrorEnabled(false);
        return false;
    }

    private boolean validateLastName(String lastName) {
        if (lastName.isEmpty()) {
            mLastNameInputLayout.setError("Missing last name field");
            return true;
        }
        mLastNameInputLayout.setErrorEnabled(false);
        return false;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            mEmailInputLayout.setError(getString(R.string.login_email_error));
            return true;
        }
        if (!isEmailValid(email)) {
            mEmailInputLayout.setError(getString(R.string.signup_email_syntax_error));
            return true;
        }
        mEmailInputLayout.setErrorEnabled(false);
        return false;
    }

    private boolean validatePassword(String password) {
        Pattern numsExp = Pattern.compile(".*[0-9].*");
        Pattern capsExp = Pattern.compile(".*[A-Z].*");
        Pattern lowsExp = Pattern.compile(".*[a-z].*");
        Pattern noSpacesExp = Pattern.compile("\\s");
        Pattern specialsExp = Pattern.compile("^[a-zA-Z0-9!@#$%^&*._]*$");

        if (password.isEmpty()) {
            mPasswordInputLayout.setError(getString(R.string.signup_password_error));
            return true;
        }
        if (password.length() < 8) {
            mPasswordInputLayout.setError(getString(R.string.signup_password_regex));
            return true;
        }
        if (!capsExp.matcher(password).matches()) {
            mPasswordInputLayout.setError(getString(R.string.signup_password_caps_error));
            return true;
        }
        if (!lowsExp.matcher(password).matches()) {
            mPasswordInputLayout.setError(getString(R.string.signup_password_lows_error));
            return true;
        }
        if (!numsExp.matcher(password).matches()) {
            mPasswordInputLayout.setError(getString(R.string.signup_password_nums_error));
            return true;
        }
        if (noSpacesExp.matcher(password).matches()) {
            mPasswordInputLayout.setError(getString(R.string.signup_password_spaces_error));
            return true;
        }
        if (!specialsExp.matcher(password).matches()) {
            mPasswordInputLayout.setError(getString(R.string.signup_password_char_error));
            return true;
        }
        mPasswordInputLayout.setErrorEnabled(false);
        return false;
    }

    private boolean validateRePassword(String password) {
        String rePassword = mPasswordInputLayout.getEditText().getText().toString();
        if (rePassword.isEmpty()) {
            mRepeatPasswordInputLayout.setError(getString(R.string.signup_repeat_password_error));
            return true;
        }
        if (!rePassword.equals(password)) {
            mRepeatPasswordInputLayout.setError(getString(R.string.signup_password_compare));
            return true;
        }
        mRepeatPasswordInputLayout.setErrorEnabled(false);
        return false;
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void loadInputValidationTriggers() {
        mFirstNameInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFirstName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mFirstNameInputLayout.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                validateFirstName(mFirstNameInputLayout.getEditText().getText().toString());
        });

        mLastNameInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateLastName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mLastNameInputLayout.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                validateLastName(mLastNameInputLayout.getEditText().getText().toString());
        });

        mEmailInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
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

        mEmailInputLayout.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateEmail(mEmailInputLayout.getEditText().getText().toString());
        });

        mPasswordInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
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

        mPasswordInputLayout.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                validatePassword(mPasswordInputLayout.getEditText().getText().toString());
        });

        mRepeatPasswordInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateRePassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mRepeatPasswordInputLayout.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                validateRePassword(mRepeatPasswordInputLayout.getEditText().getText().toString());
        });

        mRepeatPasswordInputLayout.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                hideKeyboard();
                mRepeatPasswordInputLayout.clearFocus();
                signup();
                return true;
            }
            return false;
        });

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void loading(boolean state) {
        boolean enable = !state;

        mRegisterBtn.setEnabled(enable);
        mFirstNameInputLayout.setEnabled(enable);
        mLastNameInputLayout.setEnabled(enable);
        mEmailInputLayout.setEnabled(enable);
        mPasswordInputLayout.setEnabled(enable);
        mRepeatPasswordInputLayout.setEnabled(enable);
        mUploadImageBtn.setEnabled(enable);
        mLoginBtn.setEnabled(enable);
        mRegisterBtn.setEnabled(enable);

        if (state) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRegisterBtn.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mRegisterBtn.setVisibility(View.VISIBLE);
        }
    }

}