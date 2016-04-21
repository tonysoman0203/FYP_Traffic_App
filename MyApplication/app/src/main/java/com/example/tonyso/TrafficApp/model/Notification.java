package com.example.tonyso.TrafficApp.model;

/**
 * Created by TonySo on 21/4/16.
 */
public class Notification {
    private Integer id;
    private String title;
    private String Message;
    private String From;
    private String date;

    public Notification(Integer id, String title, String message, String from, String date) {
        this.id = id;
        this.title = title;
        Message = message;
        From = from;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Notification setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Notification setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return Message;
    }

    public Notification setMessage(String message) {
        Message = message;
        return this;
    }

    public String getFrom() {
        return From;
    }

    public Notification setFrom(String from) {
        From = from;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Notification setDate(String date) {
        this.date = date;
        return this;
    }
}
