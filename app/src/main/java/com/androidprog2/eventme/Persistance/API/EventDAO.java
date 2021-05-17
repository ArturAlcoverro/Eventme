package com.androidprog2.eventme.persistance.API;

import com.androidprog2.eventme.business.Event;

import java.util.ArrayList;

public interface EventDAO {
    //POST /events	Crea un event associat a l'usuari autenticat.
    public void createEvent(Event event);

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
