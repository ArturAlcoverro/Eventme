package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.Message;
import com.androidprog2.eventme.business.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatDAO {
    // POST /messages	Crea un missatge
    @FormUrlEncoded
    @POST("messages")
    Call<Message> createMessage(
            @Header("Authorization") String token,
            @Field("content") String content,
            @Field("user_id_send") int user_id_send,
            @Field("user_id_recived") int user_id_recived);

    // GET /messages/users	Obté tots els usuaris que han enviat algún missatge a l'usuari autenticat.
    @GET("messages/users")
    Call<List<User>> usersListChat(@Header("Authorization") String token);

    // GET /messages/USER_ID	Obté tots els missatges de l'USER_ID a l'usuari autenticat.
    @GET("messages/{id}")
    Call<List<Message>> getMessages(@Header("Authorization") String token, @Path("id") int id);
}
