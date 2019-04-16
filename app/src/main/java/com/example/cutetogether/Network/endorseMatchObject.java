package com.example.cutetogether.Network;

public class endorseMatchObject {
    private String name1, userid1, name2, userid2, name3, userid3;

    public endorseMatchObject(String name1, String userid1, String name2, String userid2, String name3, String userid3) {
        this.name1 = name1;
        this.userid1 = userid1;
        this.name2 = name2;
        this.userid2 = userid2;
        this.name3 = name3;
        this.userid3 = userid3;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getUserid1() {
        return userid1;
    }

    public void setUserid1(String userid1) {
        this.userid1 = userid1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getUserid2() {
        return userid2;
    }

    public void setUserid2(String userid2) {
        this.userid2 = userid2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getUserid3() {
        return userid3;
    }

    public void setUserid3(String userid3) {
        this.userid3 = userid3;
    }
}
