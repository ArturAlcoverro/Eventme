package com.androidprog2.eventme.presentation.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.androidprog2.eventme.R;
import com.androidprog2.eventme.VolleySingleton;
import com.androidprog2.eventme.business.Message;
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.persistance.API.DocumentHelper;
import com.androidprog2.eventme.persistance.API.ImageResponse;
import com.androidprog2.eventme.persistance.API.ImgurService;
import com.androidprog2.eventme.presentation.adapters.ChatListAdapter;
import com.androidprog2.eventme.presentation.adapters.MessageListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements Callback<List<Message>> {

    public static String EXTRA_ID = "EXTRA_ID";
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int READ_EXTERNAL = 1001;
    public static final int WRITE_EXTERNAL = 1002;
    public static final int READ_WRITE_EXTERNAL = 1003;
    private static final int REQUEST_GALLERY_IMAGE = 1;

    private User user;
    private MessageListAdapter adapter;
    private TextView userName;
    private ImageView userImage;
    private ImageView arrowBack_btn;
    private EditText textToSend;
    private RecyclerView recyclerView;
    private LinearLayout profileInformation;
    private ImageButton send_btn;
    private ImageButton send_image_btn;
    private List<Message> messages;

    private File imageFile;
    private Uri returnUri;
    private int user_id_send;
    private int user_id_recived;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userName = findViewById(R.id.nameProfile);
        userImage = findViewById(R.id.imageProfile);
        arrowBack_btn = findViewById(R.id.xat_back_arrow);
        textToSend = findViewById(R.id.xat_text_send);
        send_image_btn = findViewById(R.id.xat_send_image);
        send_btn = findViewById(R.id.xat_send_button);
        recyclerView = findViewById(R.id.recyclerViewChat);
        profileInformation = findViewById(R.id.profileInformation);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);

        listener();

        Intent intent = getIntent();
        int id = intent.getIntExtra(ChatListAdapter.EXTRA_ID, 0);
        String name = intent.getStringExtra(ChatListAdapter.EXTRA_NAME);
        String last_name = intent.getStringExtra(ChatListAdapter.EXTRA_LAST_NAME);
        String email = intent.getStringExtra(ChatListAdapter.EXTRA_EMAIL);
        String image = intent.getStringExtra(ChatListAdapter.EXTRA_IMAGE);
        this.user = new User(id, image, name, last_name, email, null);

        userName.setText(this.user.getFull_name());
        setImage();

        CallSingelton
                .getInstance()
                .getMessages(this.user.getId(), this);

        arrowBack_btn.setOnClickListener(v -> {
            finish();
        });
        send_btn.setOnClickListener(v -> {
            sendMessage();
        });
        send_image_btn.setOnClickListener(v -> {
            selectImage();
        });
        profileInformation.setOnClickListener(v -> {
            startProfileActivity();
        });
    }

    private void selectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            returnUri = data.getData();

            super.onActivityResult(requestCode, resultCode, data);

            final List<String> permissionsList = new ArrayList<String>();
            addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
            addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (!permissionsList.isEmpty())
                ActivityCompat.requestPermissions(ChatActivity.this,
                        permissionsList.toArray(new String[permissionsList.size()]),
                        READ_WRITE_EXTERNAL);
            else {
                getFilePath();

                ImgurService imgurService = ImgurService.retrofit.create(ImgurService.class);

                final Call<ImageResponse> call =
                        imgurService.postImage("", "", "", "",
                                MultipartBody.Part.createFormData(
                                        "image",
                                        imageFile.getName(),
                                        RequestBody.create(MediaType.parse("image/*"), imageFile)
                                ));

                call.enqueue(new Callback<ImageResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        if (response == null) {
                            return;
                        }
                        if (response.isSuccessful()) {
                            Toast.makeText(ChatActivity.this, "Upload successful !", Toast.LENGTH_SHORT)
                                    .show();
                            String url = "http://imgur.com/" + response.body().data.id + ".png";
                            Log.d("URL Picture", url);
                            sendMessage(url);
                        }
                    }

                    @Override
                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "An unknown error has occured.", Toast.LENGTH_SHORT)
                                .show();
                        t.printStackTrace();
                    }
                });
            }
        }
    }

    private void startProfileActivity() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(EXTRA_ID, this.user.getId());
        startActivity(intent);
    }

    public void listener() {
        textToSend.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);

            }
        });
    }

    private void setImage() {
        String url;
        if (this.user.getImage().startsWith("http")) {
            url = this.user.getImage();
        } else {
            url = "http://puigmal.salle.url.edu/img/" + this.user.getImage();
        }
        ImageLoader imageLoader = VolleySingleton.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    userImage.setImageBitmap(response.getBitmap());
                }
            }

            public void onErrorResponse(VolleyError error) {
                userImage.setImageResource(R.drawable.avatar_profile);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMessage(){
        String content = textToSend.getText().toString();
        sendMessage(content);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMessage(String content) {
        if (!content.equals("")) {
            user_id_send = CallSingelton.getUserId();
            user_id_recived = this.user.getId();

            CallSingelton
                    .getInstance()
                    .insertMessage(content, user_id_send, user_id_recived, new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if (response.isSuccessful()) {
                                if (response.code() == 201) {
                                    Message message = new Message(content, user_id_send, user_id_recived);
                                    adapter.addItem(message);
                                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                    textToSend.setText("");
                                }
                            } else {
                                try {
                                    Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
        if (response.isSuccessful()) {
            if (response.code() == 200) {
                messages = (List<Message>) response.body();
                adapter = new MessageListAdapter(messages, this.user, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        } else {
            try {
                Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<List<Message>> call, Throwable t) {
        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void getFilePath() {
        String filePath = DocumentHelper.getPath(this, this.returnUri);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        imageFile = new File(filePath);
        Log.d("FilePath", filePath);
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            shouldShowRequestPermissionRationale(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE_EXTERNAL: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChatActivity.this, "All Permission are granted.", Toast.LENGTH_SHORT)
                            .show();
                    getFilePath();
                } else {
                    Toast.makeText(ChatActivity.this, "Some permissions are denied", Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}