package com.androidprog2.eventme.business;

public class User {
    private int id;
    private String image;
    private String nickname;
    private String full_name;
    private String email;
    private String password;

    public User(int id, String image, String nickname, String full_name, String email, String password) {
        this.id = id;
        this.image = image;
        this.nickname = nickname;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
    }

    public User(int id, String nickname, String full_name, String email, String password) {
        this.id = id;
        this.nickname = nickname;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
