package com.example.charmingplaces.pojo;

public class PlacesNearRequestDto {
    private double xcoord;
    private double ycoord;

    public PlacesNearRequestDto(double xcoord, double ycoord) {
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }

    public PlacesNearRequestDto() {
    }

    public PlacesNearRequestDto(GpsLocation gpsLocation) {
        this.xcoord = gpsLocation.getLonguitude();
        this.ycoord = gpsLocation.getLatitude();
    }

    public double getXcoord() {
        return xcoord;
    }

    public void setXcoord(double xcoord) {
        this.xcoord = xcoord;
    }

    public double getYcoord() {
        return ycoord;
    }

    public void setYcoord(double ycoord) {
        this.ycoord = ycoord;
    }
}
