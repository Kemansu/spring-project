package com.example.demo.controller;

import com.example.demo.controllerInterface.AnimalVisitedLocationsController;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.serviceInterface.AnimalService;
import com.example.demo.serviceInterface.AnimalVisitedLocationsService;
import com.example.demo.serviceInterface.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalVisitedLocationsControllerImpl implements AnimalVisitedLocationsController {

    private final AnimalVisitedLocationsService animalVisitedLocationsServiceImpl;

    private final AnimalService animalServiceImpl;



    @Override
    @GetMapping("/{animalId}/locations")
    public ResponseEntity<List<AnimalVisitedLocationsDtoResponse>> getVisitedLocations(
            @PathVariable @Min(value = 1) long animalId,
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime,
            @RequestParam(defaultValue = "0") @Min(value = 0) int from,
            @RequestParam(defaultValue = "10") @Min(value = 1) int size) {

        return ResponseEntity.
                ok()
                .body(animalVisitedLocationsServiceImpl
                        .getAnimalVisitedLocations(animalId,
                                startDateTime,
                                endDateTime,
                                from,
                                size));
    }

    @Override
    @PostMapping("/{animalId}/locations/{pointId}")
    public ResponseEntity<AnimalVisitedLocationsDtoResponse> addVisitedLocation(@PathVariable @Min(value = 1) long animalId,
                                                                @PathVariable @Min(value = 1) long pointId) {
        animalServiceImpl.addAnimalVisitedLocation(animalId, pointId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations(
                        animalVisitedLocationsServiceImpl.findByAnimalIdAndLocationId(animalId, pointId)
                ));
    }

    @Override
    @PutMapping("/{animalId}/locations")
    public ResponseEntity<AnimalVisitedLocationsDtoResponse> updateVisitedLocation(@PathVariable @Min(value = 1) long animalId,
                                                                   @RequestBody @Valid AnimalVisitedLocationsDtoRequest request) {
        return ResponseEntity
                .ok()
                .body(
                animalVisitedLocationsServiceImpl.updateAnimalVisitedLocation(animalId, request)
        );

    }

    @Override
    @DeleteMapping("/{animalId}/locations/{visitedPointId}")
    public ResponseEntity<String> deleteVisitedLocation(@PathVariable @Min(value = 1) long animalId,
                                                   @PathVariable @Min(value = 1) long visitedPointId){
        animalVisitedLocationsServiceImpl.deleteAnimalVisitedLocation(animalId, visitedPointId);
        return ResponseEntity.ok("");
    }

    @Override
    public AnimalVisitedLocationsDtoResponse convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations(
            AnimalVisitedLocations animalVisitedLocations) {
        AnimalVisitedLocationsDtoResponse animalVisitedLocationsDtoResponse = new AnimalVisitedLocationsDtoResponse();
        animalVisitedLocationsDtoResponse.setId(animalVisitedLocations.getId());
        animalVisitedLocationsDtoResponse.setLocationPointId(animalVisitedLocations.getLocation().getId());
        animalVisitedLocationsDtoResponse.setDateTimeOfVisitLocationPoint(animalVisitedLocations.getDateTimeOfVisitLocationPoint());
        return animalVisitedLocationsDtoResponse;
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<String> handleIllegalArgumentException(RequestValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ConflictDataException.class)
    public ResponseEntity<String> handleConflictDataException(ConflictDataException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleConflictDataException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}