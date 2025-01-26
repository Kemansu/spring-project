package com.example.demo.dto.location;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationDtoResponse {

    private long id;

    private double latitude;

    private double longitude;

    public LocationDtoResponse() {
    }

}
