package com.example.cutetogether.Network;

public class MatchObject {
    private String name1, userid1, status1, name2, userid2, status2;
    private int endorsers;

    public MatchObject(){}
    public MatchObject(String name1, String userid1, String status1, String name2, String userid2, String status2) {
        this.name1 = name1;
        this.userid1 = userid1;
        this.status1 = status1;
        this.name2 = name2;
        this.userid2 = userid2;
        this.status2 = status2;
    }

    public MatchObject(String name1, String userid1, String status1, String name2, String userid2, String status2, int endorsers) {
        this.name1 = name1;
        this.userid1 = userid1;
        this.status1 = status1;
        this.name2 = name2;
        this.userid2 = userid2;
        this.status2 = status2;
        this.endorsers = endorsers;
    }

    public int getEndorsers() {
        return endorsers;
    }

    public void setEndorsers(int endorsers) {
        this.endorsers = endorsers;
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

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
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

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }
}
