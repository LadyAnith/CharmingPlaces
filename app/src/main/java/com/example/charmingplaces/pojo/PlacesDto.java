package com.example.charmingplaces.pojo;

public class PlacesDto {

    private String id;
    private String name;
    private double xcoord;
    private double ycoord;

    public PlacesDto(String id, String name, double xcoord, double ycoord) {
        this.id = id;
        this.name = name;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
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

}
