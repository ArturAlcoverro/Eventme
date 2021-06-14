package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface FriendDAO {
    // GET /friends/requests	Obté la llista d'usuaris que han enviat una petició d'amistat a l'usuari autenticat.
    @GET("friends/requests")
    Call<List<User>> getUserFriendsRequests(@Header("Authorization") String token);

    // GET /friends	Obté la llista d'usuaris que són amics amb l'usuari autenticat
    // POST /friends/ID	Crea una petició d'amistat de l'usuari autenticat a l'usuari ID.
    // PUT /friends/ID	Accepta la petició d'amistad de l'usuari ID (que ha rebut l'usuari autenticat)
    // DELETE /friends/ID	Denega la petició d'amistad i/o (si ja són amics) l'esborra de la llista d'amics.
}
