package com.example.demo.dto.animalVisitedLocations;

public class AnimalVisitedLocationsDtoRequest {

    private long visitedLocationPointId;

    private long locationPointId;

    public AnimalVisitedLocationsDtoRequest() {
    }

    public long getVisitedLocationPointId() {
        return visitedLocationPointId;
    }

    public void setVisitedLocationPointId(long visitedLocationPointId) {
        this.visitedLocationPointId = visitedLocationPointId;
    }

    public long getLocationPointId() {
        return locationPointId;
    }

    public void setLocationPointId(long locationPointId) {
        this.locationPointId = locationPointId;
    }
}
