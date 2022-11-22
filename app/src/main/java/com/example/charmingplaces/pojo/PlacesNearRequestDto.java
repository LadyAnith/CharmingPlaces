package com.example.charmingplaces.pojo;

public class PlacesNearRequestDto {
    private GeoPoint geoPoint;

    public PlacesNearRequestDto(double xcoord, double ycoord) {
        this.geoPoint = new GeoPoint(xcoord, ycoord);
    }

    public PlacesNearRequestDto() {
    }

    public PlacesNearRequestDto(GpsLocation gpsLocation) {
        this.geoPoint = new GeoPoint(gpsLocation.getLonguitude(), gpsLocation.getLatitude());
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
