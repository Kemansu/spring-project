package com.example.demo.dto.animalVisitedLocations;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnimalVisitedLocationsDtoSearchRequest {
    private Long animalId;
    private String startDateTime;
    private String endDateTime;
    private Integer from;
    private Integer size;
}
