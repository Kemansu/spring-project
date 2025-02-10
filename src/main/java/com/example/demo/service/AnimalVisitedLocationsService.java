package com.example.demo.service;

import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoSearchRequest;
import com.example.demo.model.AnimalVisitedLocations;
import java.util.List;

public interface AnimalVisitedLocationsService {

    List<AnimalVisitedLocationsDtoResponse> getAnimalVisitedLocations(
            AnimalVisitedLocationsDtoSearchRequest dtoSearchRequest);

    AnimalVisitedLocationsDtoResponse updateAnimalVisitedLocation(Long animalId,
                                                       AnimalVisitedLocationsDtoRequest request);

    void deleteAnimalVisitedLocation(Long animalId, Long visitedPointId);

    AnimalVisitedLocationsDtoResponse addAnimalVisitedLocation(Long animalId, Long locationId);


}
