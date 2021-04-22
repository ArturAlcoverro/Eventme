package com.androidprog2.eventme;

public class FriendShip {

    private int user_id;
    private int user_id_friend;
    private int status;

    public FriendShip(int user_id, int user_id_friend, int status) {
        this.user_id = user_id;
        this.user_id_friend = user_id_friend;
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getUser_id_friend() {
        return user_id_friend;
    }

    public int getStatus() {
        return status;
    }
}
