package com.example.demo.dto.animalVisitedLocations;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimalVisitedLocationsDtoRequest {

    @Min(value = 1)
    private long visitedLocationPointId;

    @Min(value = 1)
    private long locationPointId;

    public AnimalVisitedLocationsDtoRequest() {
    }

}
