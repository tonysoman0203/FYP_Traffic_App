package com.example.tonyso.TrafficApp.model;

/**
 * Created by TonySo on 21/4/16.
 */
public class NotifyMsg {
    private Integer id;
    private String title;
    private String Message;
    private String From;
    private String date;

    public NotifyMsg(Integer id, String title, String message, String from, String date) {
        this.id = id;
        this.title = title;
        Message = message;
        From = from;
        this.date = date;
    }

    public NotifyMsg() {

    }


    public Integer getId() {
        return id;
    }

    public NotifyMsg setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NotifyMsg setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return Message;
    }

    public NotifyMsg setMessage(String message) {
        Message = message;
        return this;
    }

    public String getFrom() {
        return From;
    }

    public NotifyMsg setFrom(String from) {
        From = from;
        return this;
    }

    public String getDate() {
        return date;
    }

    public NotifyMsg setDate(String date) {
        this.date = date;
        return this;
    }
}
