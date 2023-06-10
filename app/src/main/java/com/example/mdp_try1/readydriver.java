package com.example.mdp_try1;

public class readydriver {
    private String lat,lon,uname;

    public readydriver(String s, String s1, String user) {
        this.lat=s;
        this.lon=s1;
        this.uname=user;
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
