package com.androidprog2.eventme.Persistance.API;

public class APIConnector {
    private static APIConnector instance = null;
    private final String url;

    public static APIConnector getInstance() {
        if (instance == null) {
            instance = new APIConnector("http://puigmal.salle.url.edu/api/");
        }
        return instance;
    }

    private APIConnector(String url) {
        this.url = url;
    }

    public void getApi() {
    }

    public void postApi() {
    }

    public void putApi() {
    }

    public void deleteApi() {
    }
}