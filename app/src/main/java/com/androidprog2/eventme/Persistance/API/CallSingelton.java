package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.User;
import com.google.android.gms.common.util.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.androidprog2.eventme.persistance.API.UserDAO;
import com.androidprog2.eventme.persistance.API.APIConnector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class CallSingelton {

    private static CallSingelton instance;

    private CallSingelton() {
    }

    public static CallSingelton getInstance() {
        if (instance == null) {
            instance = new CallSingelton();
        }
        return instance;
    }

    public void insertUser(String firstName, String lastName, File image, String email, String password, Callback<User> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part requestImage = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        Call<User> call = userDAO.createUser(
                RequestBody.create(MultipartBody.FORM, firstName),
                RequestBody.create(MultipartBody.FORM, lastName),
                requestImage,
                RequestBody.create(MultipartBody.FORM, email),
                RequestBody.create(MultipartBody.FORM, password));

        call.enqueue(callback);
    }

    public void insertUser(String firstName, String lastName, String imageURL, String email, String password, Callback<User> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<User> call = userDAO.createUserWithoutImg(
                firstName,
                lastName,
                imageURL,
                email,
                password);

        call.enqueue(callback);
    }

    public String readInputString(InputStream input) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = input.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
