package com.example.demo.controllerInterface;

import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.model.AnimalVisitedLocations;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AnimalVisitedLocationsController {

    @GetMapping("/{animalId}/locations")
    ResponseEntity<List<AnimalVisitedLocationsDtoResponse>> getVisitedLocations(@PathVariable @Min(value = 1) long animalId,
                                                                               @RequestParam(required = false) String startDateTime,
                                                                               @RequestParam(required = false) String endDateTime,
                                                                               @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                                                               @RequestParam(defaultValue = "10") @Min(value = 1) int size);

    @PostMapping("/{animalId}/locations/{pointId}")
    ResponseEntity<AnimalVisitedLocationsDtoResponse> addVisitedLocation(@PathVariable @Min(value = 1) long animalId,
                                         @PathVariable @Min(value = 1) long pointId);

    @PutMapping("/{animalId}/locations")
    ResponseEntity<AnimalVisitedLocationsDtoResponse> updateVisitedLocation(@PathVariable @Min(value = 1) long animalId,
                                            @RequestBody @Valid AnimalVisitedLocationsDtoRequest request);

    @DeleteMapping("/{animalId}/locations/{visitedPointId}")
    ResponseEntity<String> deleteVisitedLocation(@PathVariable @Min(value = 1) long animalId,
                                            @PathVariable @Min(value = 1) long visitedPointId);

    AnimalVisitedLocationsDtoResponse convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations(
            AnimalVisitedLocations animalVisitedLocations);
}
