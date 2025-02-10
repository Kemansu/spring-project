package com.example.demo.dto.animalVisitedLocations;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimalVisitedLocationsDtoResponse {

    private Long id;

    private String dateTimeOfVisitLocationPoint;

    private Long locationPointId;

}
