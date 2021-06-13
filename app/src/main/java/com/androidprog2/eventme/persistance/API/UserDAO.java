package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.Event;
import com.androidprog2.eventme.business.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserDAO {

    // POST /users	Crea un usuari amb fitxer
    @Multipart
    @POST("users")
    Call<User> createUser(@Part("name") RequestBody name, @Part("last_name") RequestBody last_name,
                          @Part MultipartBody.Part image, @Part("email") RequestBody email,
                          @Part("password") RequestBody password);

    // POST /users	Crea un usuari amb URL
    @FormUrlEncoded
    @POST("users")
    Call<User> createUserWithoutImg(
            @Field("name") String name,
            @Field("last_name") String last_name,
            @Field("image") String image,
            @Field("email") String email,
            @Field("password") String password
    );

    // POST /users/login	Autentica al usuari
    @POST("users/login")
    @FormUrlEncoded
    Call<String> loginUser(@Field("email") String email, @Field("password") String password);
    /*
    {
        "email" : "jonh@doe.com",
        "password" : "i<3cats"
    }
    */

    // GET /users	Retorna tots el ususaris
    public ArrayList<User> getAllUsers();

    // GET /users/ID	Retorna l'usuari amb l'ID
    @GET("users/{id}")
    Call<List<User>> getUserProfile(@Header("Authorization") String token, @Path("id") int id);

    // GET /users/search	Busca l'usuari que tingui el email, nom o cognom semblant al querystring s.

    // PUT /users/	Modifica l'usuari autenticat
    @Multipart
    @PUT("users")
    Call<User> updateUser(@Header("authorization") String token,
                          @Part MultipartBody.Part image,
                          @Part("name") RequestBody name,
                          @Part("last_name") RequestBody last_name,
                          @Part("email") RequestBody email);

    // DELETE /users	Borra l'usuari autenticat
    public void deleteUser();

    // GET /users/ID/events	Obté la llista d'events (que n'és propietari) l'usuari ID
    @GET("users/{id}/events")
    Call<List<Event>> userEvents(@Header("Authorization") String token, @Path("id") int id);

    // GET /users/ID/events/future	Obté la llista d'events (que n'és propietari) l'usuari ID que encara no han succeït
    @GET("users/{id}/events/future")
    Call<List<Event>> userEventsFuture(@Header("Authorization") String token, @Path("id") int id);

    // GET /users/ID/events/finished	Obté la llista d'events (que n'és propietari) l'usuari ID que ja han acabat
    @GET("users/{id}/events/finished")
    Call<List<Event>> userEventsFinished(@Header("Authorization") String token, @Path("id") int id);

    // GET /users/ID/events/current	Obté la llista d'events (que n'és propietari) l'usuari ID que están succeint en aquest moment.
    @GET("users/{id}/events/current")
    Call<List<Event>> userEventsCurrent(@Header("Authorization") String token, @Path("id") int id);

    // GET /users/ID/assistances	Obté la llista d'assistències a events per a l'usuari ID
    @GET("users/{id}/assistances")
    Call<List<Event>> userAssistances(@Header("Authorization") String token, @Path("id") int id);

    // GET /users/ID/assistances/future	Obté la llista d'assistències a events per a l'usuari ID que encara no han succeît
    @GET("users/{id}/assistances/future")
    Call<List<Event>> userAssistancesFuture(@Header("Authorization") String token, @Path("id") int id);

    // GET /users/ID/assistances/finished	Obté la llista d'assistències a events per a l'usuari ID que ja ha finalitzat
    @GET("users/{id}/assistances/finished")
    Call<List<Event>> userAssistancesFinished(@Header("Authorization") String token, @Path("id") int id);

    // GET /users/ID/friends	Obté el llistat d'usuaris que són amics de l'usuari ID
    @GET("users/{id}/friends")
    Call<List<User>> getUserFriends(@Header("Authorization") String token, @Path("id") int id);
}
