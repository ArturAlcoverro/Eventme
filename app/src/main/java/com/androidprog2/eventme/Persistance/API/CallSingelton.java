package com.androidprog2.eventme.Persistance.API;

import com.androidprog2.eventme.business.User;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class CallSingelton {
    private static CallSingelton instance;
    private String token;

    public CallSingelton() {
    }

    public static CallSingelton getInstance() {
        if (instance == null) {
            instance = new CallSingelton();
        }
        return instance;
    }

    private void createUser(String firstName, String lastName, File image, String email, String password, Callback callback) {
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

    public void loginUser(User user, Callback callback) {
        UserDAO userDAO = APIConnector.getRetrofitInstance().create(UserDAO.class);
        Call<String> call = userDAO.loginUser(user.getEmail(), user.getPassword());
        call.enqueue(callback);
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
