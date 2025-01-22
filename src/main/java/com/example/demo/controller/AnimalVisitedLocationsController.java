package com.example.demo.controller;

import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.service.AnimalService;
import com.example.demo.service.AnimalVisitedLocationsService;
import com.example.demo.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalVisitedLocationsController {

    private final AnimalVisitedLocationsService animalVisitedLocationsService;

    private final AnimalService animalService;

    private final LocationService locationService;

    @Autowired
    public AnimalVisitedLocationsController(AnimalVisitedLocationsService animalVisitedLocationsService, ModelMapper modelMapper, AnimalService animalService, LocationService locationService) {
        this.animalVisitedLocationsService = animalVisitedLocationsService;
        this.animalService = animalService;
        this.locationService = locationService;
    }

    @GetMapping("/{animalId}/locations")
    public ResponseEntity<?> getVisitedLocations(@PathVariable long animalId,
                                                                 @RequestParam(required = false) String startDateTime,
                                                                 @RequestParam(required = false) String endDateTime,
                                                                 @RequestParam(defaultValue = "0") int from,
                                                                 @RequestParam(defaultValue = "10") int size
                                                                 ) {

        if (animalId <= 0 || from < 0 || size <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (!animalService.isAnimalExists(animalId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        List<AnimalVisitedLocations> animalVisitedLocations = animalVisitedLocationsService
                .getAnimalVisitedLocations(animalId,
                        startDateTime,
                        endDateTime,
                        from,
                        size);


        return ResponseEntity.
                ok()
                .body(animalVisitedLocations
                .stream()
                .map(this::convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations)
                .toList());
    }

    @PostMapping("/{animalId}/locations/{pointId}")
    public ResponseEntity<?> addVisitedLocation(@PathVariable long animalId,
                                                                @PathVariable long pointId) {
        if (animalId > 0 && pointId > 0 && (!animalService.isAnimalExists(animalId) || !locationService.isLocationExists(pointId))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (animalId <= 0 || pointId <= 0 || !animalService.isAnimalAlive(animalId) || !animalService.isValideAnimalPosition(animalId, pointId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        animalService.addAnimalVisitedLocation(animalId, pointId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations(
                        animalVisitedLocationsService.findByAnimalIdAndLocationId(animalId, pointId)
                ));
    }

    @PutMapping("/{animalId}/locations")
    public ResponseEntity<?> updateVisitedLocation(@PathVariable long animalId,
                                                                   @RequestBody AnimalVisitedLocationsDtoRequest request) {
        if (animalId > 0 &&
                request.getVisitedLocationPointId() > 0 &&
                request.getLocationPointId() > 0 &&
                !animalVisitedLocationsService.isAllExistForUpdate(animalId, request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (animalId <= 0 ||
                request.getVisitedLocationPointId() <= 0 ||
                request.getLocationPointId() <= 0 ||
                !animalVisitedLocationsService.isValideForUpdating(request, animalId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }


        return ResponseEntity.ok().body(convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations(
                animalVisitedLocationsService.updateAnimalVisitedLocation(animalId, request))
        );

    }

    @DeleteMapping("/{animalId}/locations/{visitedPointId}")
    public ResponseEntity<?> deleteVisitedLocation(@PathVariable long animalId,
                                                   @PathVariable long visitedPointId){

        if (animalId <= 0 || visitedPointId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (!animalVisitedLocationsService.isAllExistForDelete(animalId, visitedPointId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        animalVisitedLocationsService.deleteAnimalVisitedLocation(animalId, visitedPointId);
        return ResponseEntity.ok("");
    }

    public AnimalVisitedLocationsDtoResponse convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations(
            AnimalVisitedLocations animalVisitedLocations) {
        AnimalVisitedLocationsDtoResponse animalVisitedLocationsDtoResponse = new AnimalVisitedLocationsDtoResponse();
        animalVisitedLocationsDtoResponse.setId(animalVisitedLocations.getId());
        animalVisitedLocationsDtoResponse.setLocationPointId(animalVisitedLocations.getLocation().getId());
        animalVisitedLocationsDtoResponse.setDateTimeOfVisitLocationPoint(animalVisitedLocations.getDateTimeOfVisitLocationPoint());
        return animalVisitedLocationsDtoResponse;
    }
}