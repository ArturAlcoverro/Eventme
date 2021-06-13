package com.androidprog2.eventme.presentation.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements Callback<User> {
    private static final int REQUEST_GALLERY_IMAGE = 1;
    private ProgressBar progressBar;
    private ImageView eventImage;
    private File mImageFile;

    private ImageButton backArrow_btn;
    private MaterialButton apply_changes_btn;
    private MaterialButton change_image_btn;
    private TextView profileName;

    private TextInputLayout nameInput;
    private TextInputLayout lastNameInput;
    private TextInputLayout emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mImageFile = null;
        backArrow_btn = (ImageButton) findViewById(R.id.arrowLeft);
        apply_changes_btn = (MaterialButton) findViewById(R.id.apply_changes_btn);
        change_image_btn = (MaterialButton) findViewById(R.id.upload_btn);
        progressBar = findViewById(R.id.progress_bar_profle_activity);
        eventImage = findViewById(R.id.imageProfileEdit);
        profileName = findViewById(R.id.usernameTitle);

        nameInput = findViewById(R.id.editProfileNickname);
        lastNameInput = findViewById(R.id.editProfileName);
        emailInput = findViewById(R.id.editProfileEmail);

        validationListeners();

        backArrow_btn.setOnClickListener(v -> { finish(); });
        change_image_btn.setOnClickListener(v -> { selectImage(); });
        apply_changes_btn.setOnClickListener(v -> { saveChanges(); });
    }

    public void saveChanges() {
        if(validateData()){
            String name = nameInput.getEditText().getText().toString();
            String lastName = lastNameInput.getEditText().getText().toString();
            String email = emailInput.getEditText().getText().toString();

            loading(true);
            if(mImageFile == null) {
                CallSingelton
                        .getInstance()
                        .updateUser(mImageFile, name, lastName, "Asdasd123", email, this);            }else {
                CallSingelton
                        .getInstance()
                        .updateUser(mImageFile, name, lastName, "Asdasd123", email, this);
            }
        }
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        loading(false);
        if (response.isSuccessful()) {
            if (response.code() == 201) {
                //Decidir que fer
                System.out.println("vamos actulaitzat");
            }
            else if (response.code() == 400){
                Toast.makeText(getApplicationContext(), getString(R.string.incorrect_body_error), Toast.LENGTH_LONG).show();
            }else if (response.code() == 409){
                Toast.makeText(getApplicationContext(), getString(R.string.incorrect_to_insert), Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                System.out.println(response.errorBody().toString());
                Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
        loading(false);
    }

    public void validationListeners(){
        nameInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFirstName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        lastNameInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateLastName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        emailInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        nameInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                validateFirstName(nameInput.getEditText().getText().toString());
        });

        lastNameInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                validateFirstName(lastNameInput.getEditText().getText().toString());
        });

        emailInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                validateFirstName(emailInput.getEditText().getText().toString());
        });

        emailInput.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                hideKeyboard();
                saveChanges();
                return true;
            }
            return false;
        });
    }

    private boolean validateFirstName(String firstName) {
        if (firstName.isEmpty()) {
            nameInput.setError(getString(R.string.signup_name_error));
            return true;
        }
        nameInput.setErrorEnabled(false);
        return false;
    }

    private boolean validateLastName(String lastName) {
        if (lastName.isEmpty()) {
            lastNameInput.setError(getString(R.string.signup_lastname_error));
            return true;
        }
        lastNameInput.setErrorEnabled(false);
        return false;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            emailInput.setError(getString(R.string.login_email_error));
            return true;
        }
        if (!isEmailValid(email)) {
            emailInput.setError(getString(R.string.signup_email_syntax_error));
            return true;
        }
        emailInput.setErrorEnabled(false);
        return false;
    }

    public boolean validateData(){
        boolean error = true;
        if(validateFirstName(nameInput.getEditText().getText().toString())) error = false;
        if(validateLastName(lastNameInput.getEditText().getText().toString())) error = false;
        if(validateEmail(emailInput.getEditText().getText().toString())) error = false;
        return error;
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void selectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
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
                eventImage.setImageURI(uriData);
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

    public void loading(boolean state) {
        boolean enable = !state;

        change_image_btn.setEnabled(enable);
        apply_changes_btn.setEnabled(enable);
        nameInput.setEnabled(enable);
        lastNameInput.setEnabled(enable);
        emailInput.setEnabled(enable);

        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            apply_changes_btn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            apply_changes_btn.setVisibility(View.VISIBLE);
        }
    }

}