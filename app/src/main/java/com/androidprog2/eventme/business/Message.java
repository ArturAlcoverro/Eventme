package com.androidprog2.eventme.business;

import java.sql.Timestamp;
import java.util.Date;

public class Message {

    private int id;
    private String content;
    private int user_id_send;
    private int user_id_recived;
    private Timestamp timeStamp;

    public Message(int id, String content, int user_id_send, int user_id_recived, Timestamp timeStamp) {
        this.id = id;
        this.content = content;
        this.user_id_send = user_id_send;
        this.user_id_recived = user_id_recived;
        this.timeStamp = timeStamp;
    }

    public Message(String content, int user_id_send, int user_id_recived){
        this.content = content;
        this.user_id_send = user_id_send;
        this.user_id_recived = user_id_recived;
        this.id = 0;
        this.timeStamp = new Timestamp(System.currentTimeMillis());
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
        Date date = new Date(timeStamp.getTime());
        String time = date.toString();
        String hourAndMin = time.substring(11,16);
        return hourAndMin;
    }

    public Timestamp getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timeStamp = timestamp;
    }
}
