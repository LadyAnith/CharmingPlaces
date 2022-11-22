package com.example.charmingplaces.pojo;

import java.util.ArrayList;
import java.util.List;

public class PlacesInsideAreaRequestDto {
   private GeoPoint geoPointTopLeft;
   private GeoPoint geoPointBottomRight;

    public PlacesInsideAreaRequestDto(GeoPoint geoPointTopLeft, GeoPoint geoPointBottomRight) {
        this.geoPointTopLeft = geoPointTopLeft;
        this.geoPointBottomRight = geoPointBottomRight;
    }

    public PlacesInsideAreaRequestDto() {
    }

    public GeoPoint getGeoPointTopLeft() {
        return geoPointTopLeft;
    }

    public void setGeoPointTopLeft(GeoPoint geoPointTopLeft) {
        this.geoPointTopLeft = geoPointTopLeft;
    }

    public GeoPoint getGeoPointBottomRight() {
        return geoPointBottomRight;
    }

    public void setGeoPointBottomRight(GeoPoint geoPointBottomRight) {
        this.geoPointBottomRight = geoPointBottomRight;
    }
}
