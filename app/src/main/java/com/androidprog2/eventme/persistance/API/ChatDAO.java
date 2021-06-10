package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ChatDAO {
    // POST /messages	Crea un missatge

    // GET /messages/users	Obté tots els usuaris que han enviat algún missatge a l'usuari autenticat.
    @GET("messages/users")
    Call<List<User>> usersListChat(@Header("Authorization") String token);

    // GET /messages/USER_ID	Obté tots els missatges de l'USER_ID a l'usuari autenticat.
}
