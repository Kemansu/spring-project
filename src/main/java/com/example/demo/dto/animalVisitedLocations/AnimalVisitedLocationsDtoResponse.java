package com.example.demo.dto.animalVisitedLocations;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimalVisitedLocationsDtoResponse {

    private long id;

    private String dateTimeOfVisitLocationPoint;

    private long locationPointId;

    public AnimalVisitedLocationsDtoResponse() {
    }

}
