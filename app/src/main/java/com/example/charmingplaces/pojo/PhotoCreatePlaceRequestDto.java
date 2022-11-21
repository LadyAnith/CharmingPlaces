package com.example.charmingplaces.pojo;

public class PhotoCreatePlaceRequestDto {
    private String name;
    private String city;
    private String address;
    private double xcoord;
    private double ycoord;
    private byte[] image;

    public String getName() {
        return name;
    }

    public PhotoCreatePlaceRequestDto setName(String name) {
        this.name = name;
        return this;
    }

    public double getXcoord() {
        return xcoord;
    }

    public PhotoCreatePlaceRequestDto setXcoord(double xcoord) {
        this.xcoord = xcoord;
        return this;
    }

    public double getYcoord() {
        return ycoord;
    }

    public PhotoCreatePlaceRequestDto setYcoord(double ycoord) {
        this.ycoord = ycoord;
        return this;
    }

    public byte[] getImage() {
        return image;
    }

    public PhotoCreatePlaceRequestDto setImage(byte[] image) {
        this.image = image;
        return this;
    }

    public String getCity() {
        return city;
    }

    public PhotoCreatePlaceRequestDto setCity(String city) {
        this.city = city;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PhotoCreatePlaceRequestDto setAddress(String address) {
        this.address = address;
        return this;
    }
}
