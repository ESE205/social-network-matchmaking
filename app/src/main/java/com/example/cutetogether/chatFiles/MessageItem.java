package com.example.cutetogether.chatFiles;

public class MessageItem {
    String message;
    String name;
    String time;
    String id;

    public MessageItem(String message, String name, String time, String id) {
        this.message = message;
        this.name = name;
        this.time = time;
        this.id = id;
    }

    public MessageItem(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
