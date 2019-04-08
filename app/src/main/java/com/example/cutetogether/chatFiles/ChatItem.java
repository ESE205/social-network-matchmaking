package com.example.cutetogether.chatFiles;

public class ChatItem {

    String name;
    String chatid;
    String userid;

    public ChatItem (){ }

    public  ChatItem (String name, String chatid, String userid){
        this.name = name;
        this.chatid = chatid;
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
