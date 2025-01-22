package com.example.demo.dto.animalVisitedLocations;



public class AnimalVisitedLocationsDtoResponse {

    private long id;

    private String dateTimeOfVisitLocationPoint;

    private long locationPointId;

    public AnimalVisitedLocationsDtoResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String  getDateTimeOfVisitLocationPoint() {
        return dateTimeOfVisitLocationPoint;
    }

    public void setDateTimeOfVisitLocationPoint(String dateTimeOfVisitLocationPoint) {
        this.dateTimeOfVisitLocationPoint = dateTimeOfVisitLocationPoint;
    }

    public long getLocationPointId() {
        return locationPointId;
    }

    public void setLocationPointId(long locationPointId) {
        this.locationPointId = locationPointId;
    }
}
