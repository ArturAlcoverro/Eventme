package com.androidprog2.eventme.Persistance.API;

import com.androidprog2.eventme.business.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserDAO {

    // POST /users	Crea un usuari amb els següents parámetres
    @Multipart
    @POST("users")
    Call<User> createUser(
            @Part("name") RequestBody name,
            @Part("last_name") RequestBody last_name,
            @Part("image") MultipartBody.Part image,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );
//                "name" : "John",
//                "last_name" : "Doe",
//                "email" : "jonh@doe.com",
//                "password" : "i<3cats",

    // POST /users/login	Autentica al usuari
    @POST("users/login")
    Call<String> loginUser(User user);

    // GET /users	Retorna tots el ususaris
    public ArrayList<User> getAllUsers();

    // GET /users/ID	Retorna l'usuari amb l'ID
    public User searchById(int id);

    // GET /users/search	Busca l'usuari que tingui el email, nom o cognom semblant al querystring s.
    //TODO

    // PUT /users/	Modifica l'usuari autenticat
    public void changeUser(User user);

    // DELETE /users	Borra l'usuari autenticat
    public void deleteUser();


    // GET /users/ID/events	Obté la llista d'events (que n'és propietari) l'usuari ID
    // GET /users/ID/events/future	Obté la llista d'events (que n'és propietari) l'usuari ID que encara no han succeït
    // GET /users/ID/events/finished	Obté la llista d'events (que n'és propietari) l'usuari ID que ja han acabat
    // GET /users/ID/events/current	Obté la llista d'events (que n'és propietari) l'usuari ID que están succeint en aquest moment.
    // GET /users/ID/assistances	Obté la llista d'assistències a events per a l'usuari ID
    // GET /users/ID/assistances/future	Obté la llista d'assistències a events per a l'usuari ID que encara no han succeît
    // GET /users/ID/assistances/finished	Obté la llista d'assistències a events per a l'usuari ID que ja ha finalitzat
    // GET /users/ID/friends	Obté el llistat d'usuaris que són amics de l'usuari ID

}
