package com.example.mdp_try1;

public class readydriver {
    public String lat,lon,uname,usern="NA";
    public int set;
    public readydriver(String s, String s1, String user) {
        this.lat=s;
        this.lon=s1;
        this.uname=user;
        this.set=0;
    }
    public readydriver() {
        this.lat="0.0";
        this.lon="0.0";
        this.uname="NA";
        this.usern="NA";
        set=0;
    }


    public void setLat(String lat)
    {
        this.lat=lat;
    }
    public void setLon(String lon)
    {
        this.lon=lon;
    }
    public String getLat()
    {
        return this.lat;
    }
    public String getLon()
    {
        return this.lon;
    }
    public String getname()
    {
        return this.uname;
    }
}
