package com.androidprog2.eventme.business;

public class User {
    private int id;
    private String image;
    private String name;
    private String last_name;
    private String email;
    private String password;

    public User(int id, String image, String name, String last_name, String email, String password) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFull_name(){
        return name + " " + last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
