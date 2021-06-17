package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FriendDAO {
    // GET /friends/requests	Obté la llista d'usuaris que han enviat una petició d'amistat a l'usuari autenticat.
    @GET("friends/requests")
    Call<List<User>> getUserFriendsRequests(@Header("Authorization") String token);

    // GET /friends	Obté la llista d'usuaris que són amics amb l'usuari autenticat
    // POST /friends/ID	Crea una petició d'amistat de l'usuari autenticat a l'usuari ID.
    @POST("friends/{id}")
    Call<Response<Void>> requestFriendShip(@Header("Authorization") String token, @Path("id") int id);

    // PUT /friends/ID	Accepta la petició d'amistad de l'usuari ID (que ha rebut l'usuari autenticat)
    @PUT("friends/{id}")
    Call<Response<Void>> acceptFriendShip(@Header("Authorization") String token, @Path("id") int id);

    // DELETE /friends/ID	Denega la petició d'amistad i/o (si ja són amics) l'esborra de la llista d'amics.
    @DELETE("friends/{id}")
    Call<Response<Void>> declineFriendShip(@Header("Authorization") String token, @Path("id") int id);

}
