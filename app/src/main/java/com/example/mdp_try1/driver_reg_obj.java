package com.example.mdp_try1;

public class driver_reg_obj {
    public String uname,name,email,ph,pass;
//    public boolean avail;
    public driver_reg_obj() {
    }

    public driver_reg_obj(String uname,String name, String email,String pass, String phone) {
        this.uname=uname;
        this.name = name;
        this.email = email;
        this.pass=pass;
        this.ph=phone;
    }
    public String getPass(){
        return this.pass;
    }
    public String getno(){
        return this.ph;
    }

    public String getname(){
        return this.name;
    }
//    public boolean checkavail(){
//        return this.avail;
//    }
}
