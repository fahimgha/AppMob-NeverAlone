package com.example.neveralone;

public class CreateUser {
    public CreateUser()
    {}
    public String name;

    public CreateUser(String name, String email, String password, String code, String issharing, String lat, String lng) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.issharing = issharing;
        this.lat = lat;
        this.lng = lng;
        //this.imageUrl = imageUrl;
    }

    public String email;
    public String password;
    public String code;
    public String issharing;
    public String lat;
    public String lng;
    //public String imageUrl;
}
