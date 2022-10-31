package com.example.charmingplaces.POJO;

public class Photo {
    private double xcoord;
    private double ycoord;
    private byte[] image;

    public Photo() {
    }

    public Photo(double xcoord, double ycoord, byte[] image) {
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
