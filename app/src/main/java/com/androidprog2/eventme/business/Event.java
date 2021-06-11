package com.androidprog2.eventme.business;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Event {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("location")
    private String localization;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String img;
    @SerializedName("type")
    private String type;
    @SerializedName("comentary")
    private String comentary;
    @SerializedName("puntuation")
    private String puntuation;
    private Calendar calendar;

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

    public Event(int id, String name, int ownerId, String localization, Date creationDate, Date startDate, Date endDate, int numParticipants, String img) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.localization = localization;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numParticipants = numParticipants;
        this.img = img;
        this.calendar = Calendar.getInstance();
    }

    public String getPeriod() {
        this.calendar = Calendar.getInstance();
        String r;
        int startDay, startMonth, endDay, endMonth;
        SimpleDateFormat format = new SimpleDateFormat("MMM dd");

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
        return r;
    }

    public String getNameAndLocalization(){
        return this.name + " - " + this.localization;
    }

    public int getNumParticipants(){
        return this.numParticipants;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getLocalization() {
        return localization;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getType() {
        return type;
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
