package com.androidprog2.eventme.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Event {

    private int id;
    private String name;
    private int ownerId;
    private String localization;
    private Date creationDate;
    private Date startDate;
    private Date endDate;
    private int maxParticipants;
    private int numParticipants;
    private String img;
    private String type;
    private Calendar calendar;

    public Event() {
    }

    public Event(int id, String name, int ownerId, String localization, Date creationDate, Date startDate, Date endDate, int maxParticipants, int numParticipants, String img) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.localization = localization;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxParticipants = maxParticipants;
        this.numParticipants = numParticipants;
        this.img = img;
        this.calendar = Calendar.getInstance();
    }

    public String getPeriod() {
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
}
