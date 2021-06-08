package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.Event;
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

    public void insertEvent(String name, File image, String location, String description, String eventStart_date,
                            String eventEnd_date, String type, String n_participators, Callback<Event> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        EventDAO eventDAO = retrofit.create(EventDAO.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part requestImage = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        Call<Event> call = eventDAO.createEvent(
                RequestBody.create(MultipartBody.FORM, name),
                requestImage,
                RequestBody.create(MultipartBody.FORM, location),
                RequestBody.create(MultipartBody.FORM, description),
                RequestBody.create(MultipartBody.FORM, eventStart_date),
                RequestBody.create(MultipartBody.FORM, eventEnd_date),
                RequestBody.create(MultipartBody.FORM, n_participators),
                RequestBody.create(MultipartBody.FORM, type));

        call.enqueue(callback);
    }

    public void insertEvent(String name, String image, String location, String description, String eventStart_date,
                            String eventEnd_date, String type, String n_participators, Callback<Event> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        EventDAO eventDAO = retrofit.create(EventDAO.class);

        Call<Event> call = eventDAO.createEventWithoutImg(
                name,
                image,
                location,
                description,
                eventStart_date,
                eventEnd_date,
                n_participators,
                type);

        call.enqueue(callback);
    }
}
