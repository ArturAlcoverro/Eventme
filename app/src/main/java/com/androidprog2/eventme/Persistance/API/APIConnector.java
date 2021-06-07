package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIConnector {
    private static final String API_BASE_URL = "http://puigmal.salle.url.edu/api/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public void loginUser(User user,Callback callback) {
        UserDAO userDAO = APIConnector.getRetrofitInstance().create(UserDAO.class);
        Call<String> call = userDAO.loginUser(user.getEmail(), user.getPassword());
        call.enqueue(callback);
    }
}