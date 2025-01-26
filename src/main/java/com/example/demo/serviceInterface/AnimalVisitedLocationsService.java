package com.example.demo.serviceInterface;

import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.model.AnimalVisitedLocations;
import java.util.List;

public interface AnimalVisitedLocationsService {

    List<AnimalVisitedLocationsDtoResponse> getAnimalVisitedLocations(long animalId,
                                                           String startDateTime,
                                                           String endDateTime,
                                                           int from,
                                                           int size);

    AnimalVisitedLocationsDtoResponse updateAnimalVisitedLocation(long animalId,
                                                       AnimalVisitedLocationsDtoRequest request);

    void deleteAnimalVisitedLocation(long animalId, long visitedPointId);

    boolean isValideForUpdating(AnimalVisitedLocationsDtoRequest request, long animalId);

    boolean isAllExistForUpdate(long animalId, AnimalVisitedLocationsDtoRequest request);

    boolean isAllExistForDelete(long animalId, long visitedPointId);

    AnimalVisitedLocations findByAnimalIdAndLocationId(long animalId, long locationId);

    AnimalVisitedLocationsDtoResponse convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations(
            AnimalVisitedLocations animalVisitedLocations);

}
