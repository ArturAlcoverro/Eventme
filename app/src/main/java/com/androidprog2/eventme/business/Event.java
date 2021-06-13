package com.androidprog2.eventme.business;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Event {

    private int id;
    private String name;
    @SerializedName("location")
    private String location;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String image;
    @SerializedName("type")
    private String type;
    @SerializedName("comentary")
    private String comentary;
    @SerializedName("puntuation")
    private String puntuation;

    @SerializedName("owner_id")
    private int ownerId;
    @SerializedName("date")
    private Date creationDate;
    @SerializedName("eventStart_date")
    private Date startDate;
    @SerializedName("eventEnd_date")
    private Date endDate;
    @SerializedName("n_participators")
    private int numParticipants;


    public Event() {
    }

    public Event(int id, String name, String location, String image, String description, String type, int ownerId, Date creationDate, Date startDate, Date endDate, int numParticipants) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.image = image;
        this.description = description;
        this.type = type;
        this.ownerId = ownerId;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numParticipants = numParticipants;
    }

    public String getPeriod() {
        Calendar calendar = Calendar.getInstance();
        String r = "";
        int startDay, startMonth, endDay, endMonth;
        SimpleDateFormat format = new SimpleDateFormat("MMM dd");

        if(startDate != null && endDate != null) {
            calendar.setTime(startDate);
            startDay = calendar.get(Calendar.DAY_OF_MONTH);
            startMonth = calendar.get(Calendar.MONTH);

            calendar.setTime(endDate);
            endDay = calendar.get(Calendar.DAY_OF_MONTH);
            endMonth = calendar.get(Calendar.MONTH);

            if (startDay == endDay && startMonth == endMonth) {
                r = format.format(startDate);
            } else {
                r = format.format(startDate) + " - " + format.format(endDate);
            }
        }
        return r;
    }

    public String getNameAndLocation(){
        return this.name + " - " + this.location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public void setNumParticipants(int numParticipants) {
        this.numParticipants = numParticipants;
    }

    public String getDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(day);
    }

    public String getMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int month = cal.get(Calendar.MONTH);
        return String.valueOf(month);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
