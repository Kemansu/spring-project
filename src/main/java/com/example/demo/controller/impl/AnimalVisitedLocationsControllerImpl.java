package com.example.demo.controller.impl;

import com.example.demo.controller.AnimalVisitedLocationsController;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoSearchRequest;
import com.example.demo.mapper.AnimalVisitedLocationsMapper;
import com.example.demo.service.AnimalService;
import com.example.demo.service.AnimalVisitedLocationsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Animal Visited Locations",
        description = "Операции по управлению локациями, которые посетило животное")
@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalVisitedLocationsControllerImpl implements AnimalVisitedLocationsController {

    private final AnimalVisitedLocationsService animalVisitedLocationsServiceImpl;

    @Override
    public ResponseEntity<List<AnimalVisitedLocationsDtoResponse>> getVisitedLocations(
            @PathVariable Long animalId,
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        AnimalVisitedLocationsDtoSearchRequest dtoSearchRequest = AnimalVisitedLocationsDtoSearchRequest.builder()
                .animalId(animalId)
                .endDateTime(endDateTime)
                .startDateTime(startDateTime)
                .from(from)
                .size(size)
                .build();

        return ResponseEntity.ok().body(animalVisitedLocationsServiceImpl.getAnimalVisitedLocations(dtoSearchRequest));
    }

    @Override
    public ResponseEntity<AnimalVisitedLocationsDtoResponse> addVisitedLocation(
            @PathVariable Long animalId,
            @PathVariable Long pointId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(animalVisitedLocationsServiceImpl.addAnimalVisitedLocation(animalId, pointId));
    }

    @Override
    public ResponseEntity<AnimalVisitedLocationsDtoResponse> updateVisitedLocation(
            @PathVariable Long animalId,
            @RequestBody AnimalVisitedLocationsDtoRequest request) {
        return ResponseEntity
                .ok()
                .body(animalVisitedLocationsServiceImpl.updateAnimalVisitedLocation(animalId, request));
    }

    @Override
    public ResponseEntity<Void> deleteVisitedLocation(
            @PathVariable Long animalId,
            @PathVariable Long visitedPointId) {
        animalVisitedLocationsServiceImpl.deleteAnimalVisitedLocation(animalId, visitedPointId);
        return ResponseEntity.ok().build();
    }
}
