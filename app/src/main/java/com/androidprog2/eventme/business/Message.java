package com.androidprog2.eventme.business;

import java.sql.Timestamp;
import java.util.Date;

public class Message {

    private int id;
    private String content;
    private int user_id_send;
    private int user_id_recived;
    private Timestamp ts;

    public Message(int id, String content, int user_id_send, int user_id_recived, Timestamp ts) {
        this.id = id;
        this.content = content;
        this.user_id_send = user_id_send;
        this.user_id_recived = user_id_recived;
        this.ts = ts;
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
        Date date = new Date(ts.getTime());
        String time = date.toString();
        String hourAndMin = time.substring(11,16);
        return hourAndMin;
    }
}
