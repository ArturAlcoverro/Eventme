package com.androidprog2.eventme.business;

import java.sql.Timestamp;

public class Message {

    private int id;
    private String content;
    private int user_id_send;
    private int user_id_recived;
    private Timestamp timestamp;

    public Message(int id, String content, int user_id_send, int user_id_recived, Timestamp timestamp) {
        this.id = id;
        this.content = content;
        this.user_id_send = user_id_send;
        this.user_id_recived = user_id_recived;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getUser_id_send() {
        return user_id_send;
    }

    public int getUser_id_recived() {
        return user_id_recived;
    }

    public String getTs() {
        //Date date = new Date(timestamp.getTime());
        //String time = date.toString();
        //String hourAndMin = timestamp.substring(11,16);
        return null;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
