package com.example.demo.dto.location;

public class LocationDtoRequest {

    private Double latitude;

    private Double longitude;

    public LocationDtoRequest() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
