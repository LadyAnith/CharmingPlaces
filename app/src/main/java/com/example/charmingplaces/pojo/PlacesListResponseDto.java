package com.example.charmingplaces.pojo;

import java.util.ArrayList;
import java.util.List;

public class PlacesListResponseDto {

    List<PlacesDto> data = new ArrayList<>();

    public List<PlacesDto> getData() {
        return data;
    }

    public void setData(List<PlacesDto> data) {
        this.data = data;
    }


}
