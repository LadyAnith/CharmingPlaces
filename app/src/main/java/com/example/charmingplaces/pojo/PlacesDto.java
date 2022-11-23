package com.example.charmingplaces.pojo;

public class PlacesDto {

    private String id;
    private String name;
    private String url;
    private double xcoord;
    private double ycoord;

    public PlacesDto(String id, String name, double xcoord, double ycoord, String url) {
        this.id = id;
        this.name = name;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.url = url;
    }

    public PlacesDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
