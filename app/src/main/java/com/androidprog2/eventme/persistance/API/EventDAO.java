package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.Event;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface EventDAO {
    //POST /events	Crea un event associat a l'usuari autenticat.
    @Multipart
    @POST("events")
    Call<Event> createEvent(@Part("name") RequestBody name,
                            @Part MultipartBody.Part image,
                            @Part("location") RequestBody location,
                            @Part("description") RequestBody description,
                            @Part("eventStart_date") RequestBody eventStart_date,
                            @Part("eventEnd_date") RequestBody eventEnd_date,
                            @Part("n_participators") RequestBody n_participators,
                            @Part("type") RequestBody type);

    // POST /events	Crea un event amb URL
    @FormUrlEncoded
    @POST("events")
    Call<Event> createEventWithoutImg(
            @Header("authorization") String token,
            @Field("name") String name,
            @Field("image") String image,
            @Field("location") String location,
            @Field("description") String description,
            @Field("eventStart_date") String eventStart_date,
            @Field("eventEnd_date") String eventEnd_date,
            @Field("n_participators") String n_participators,
            @Field("type") String type
    );

    //GET /events	Retorna tots el events
    public ArrayList<Event> getAllEvents();

    // GET /events/ID	Retorna l'event amb l'ID
    public Event getEvent(int id);

    // PUT /events/ID	Modifica l'event autenticat
    public void changeEvent(Event event);

    // DELETE /events/ID	Esborra l'event ID
    public void deleteEvent(Event event);

    // GET /events/ID/assistances	Obté la llista d'asistents que asistiràn a l'event ID
    // GET /events/ID/assistances/ID_USER	Obté la assistència de l'ID_USER que asistirà a l'event ID
    // POST /events/ID/assistances	Crea una assistència de l'usuari autenticat a l'event ID
    // PUT /events/ID/assistances	Crea la informació de assistència de l'usuari autenticat a l'event ID
    // DELETE /events/ID/assistances	Esborra l'asistència de l'usuari autenticat a l'event ID
}
