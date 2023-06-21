package com.example.mdp_try1;

import java.io.Serializable;

public class user_req_obj implements Serializable {
    public String user,driver,ulat,ulon,dlat,dlon,eta;
    public int served;

    public user_req_obj()
    {
    }
    public user_req_obj(String user,String lat,String lon)
    {
        this.user=user;
        this.ulat=lat;
        this.ulon=lon;
        this.driver="NA";
        this.dlat="NA";
        this.dlon="NA";
        this.served=0;
    }
    public int getServed()
    {
        return this.served;
    }
}
