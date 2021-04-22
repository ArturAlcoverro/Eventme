package com.androidprog2.eventme.Persistance.API;

import com.androidprog2.eventme.Persistance.EventDAO;
import com.androidprog2.eventme.business.Event;

import java.util.ArrayList;

public class APIEventDAO implements EventDAO {
    @Override
    public void createEvent(Event event) {
    }

    @Override
    public ArrayList<Event> getAllEvents() {
        return null;
    }

    @Override
    public Event getEvent(int id) {
        return null;
    }

    @Override
    public void changeEvent(Event event) {

    }

    @Override
    public void deleteEvent(Event event) {

    }
}
