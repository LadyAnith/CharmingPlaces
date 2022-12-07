package com.example.charmingplaces.pojo;

public class PlacesDto {

    private String id;
    private String name;
    private String imageContent;
    private double xcoord;
    private double ycoord;
    private Integer votes;
    private boolean voted;

    public PlacesDto(String id, String name, double xcoord, double ycoord, String imageContent, Integer votes, boolean voted) {
        this.id = id;
        this.name = name;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.imageContent = imageContent;
        this.votes = votes;
        this.voted = voted;
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

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }
}
