package com.example.charmingplaces.pojo;

public class VoteRequestDto {
    private String userId;
    private String placeId;

    public VoteRequestDto() {
    }

    public VoteRequestDto(String userId, String placeId) {
        this.userId = userId;
        this.placeId = placeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
