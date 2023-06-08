package com.example.mdp_try1;

public class user_obj {
    public String uname,name,email,ph,pass;
    public user_obj() {
    }

    public user_obj(String uname,String name, String email,String pass, String phone) {
        this.uname=uname;
        this.name = name;
        this.email = email;
        this.pass=pass;
        this.ph=phone;
    }
    public String getPass(){
        return this.pass;
    }
//    public String getlat(){
//        return this.lat;
//    }
//    public String getlon(){
//        return this.lon;
//    }
    public String getname(){
        return this.name;
    }



}