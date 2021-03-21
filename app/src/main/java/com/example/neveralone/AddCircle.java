package com.example.neveralone;


public class AddCircle
{
    public AddCircle(String name, String issharing, String lat, String lng) {
        this.name = name;
        this.issharing = issharing;
        this.lat = lat;
        this.lng = lng;
    }

    public String name,issharing,lat,lng;

    public AddCircle()
    {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssharing() {
        return issharing;
    }

    public void setIssharing(String issharing) {
        this.issharing = issharing;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
