package com.example.charmingplaces.pojo;

import com.google.android.gms.maps.model.LatLng;

public class GeoPoint {
    private Double xcoord;
    private Double ycoord;

    public GeoPoint(Double xCoord, Double yCoord) {
        this.xcoord = xCoord;
        this.ycoord = yCoord;
    }

    public GeoPoint() {
    }

    public GeoPoint(LatLng farLeft) {
        this.xcoord = farLeft.longitude;
        this.ycoord = farLeft.latitude;
    }

    public Double getXcoord() {
        return xcoord;
    }

    public void setXcoord(Double xcoord) {
        this.xcoord = xcoord;
    }

    public Double getYcoord() {
        return ycoord;
    }

    public void setYcoord(Double ycoord) {
        this.ycoord = ycoord;
    }
}
