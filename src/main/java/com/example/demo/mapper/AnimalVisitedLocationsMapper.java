package com.example.demo.mapper;

import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.model.AnimalVisitedLocations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalVisitedLocationsMapper {

    @Mapping(source = "location.id", target = "locationPointId")
    AnimalVisitedLocationsDtoResponse toAnimalVisitedLocationsDtoResponse(
            AnimalVisitedLocations animalVisitedLocations);
}
