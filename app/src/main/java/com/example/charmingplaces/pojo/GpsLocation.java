package com.example.charmingplaces.pojo;

public class GpsLocation {
    private String address;
    private String country;
    private String city;
    private Double latitude;
    private Double longuitude;

    public GpsLocation() {
    }

    public String getAddress() {
        return address;
    }

    public GpsLocation setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public GpsLocation setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public GpsLocation setCity(String city) {
        this.city = city;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public GpsLocation setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLonguitude() {
        return longuitude;
    }

    public GpsLocation setLonguitude(Double longuitude) {
        this.longuitude = longuitude;
        return this;
    }
}
