package com.example.demo.controller;

import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.model.Location;
import com.example.demo.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
public class LocationsController {
    private final LocationService locationService;

    private final ModelMapper modelMapper;

    @Autowired
    public LocationsController(LocationService locationService, ModelMapper modelMapper) {
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocation(@PathVariable long id) {
        Location location = locationService.findLocationById(id);
        if (id <= 0) {
            return ResponseEntity.badRequest().body("");
        }

        if (location == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        return ResponseEntity.ok().body(convertLocationToLocationDtoResponse(location));
    }

    @PostMapping("")
    public ResponseEntity<?> addLocation(@RequestBody LocationDtoRequest request) {

        if (locationService.isValidateLocationRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }
        
        if (locationService.isExistsByLongitudeAndLatitude(request.getLongitude(), request.getLatitude())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        Location location = locationService.save(convertrequestToLocation(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(convertLocationToLocationDtoResponse(location));
    }

    @PutMapping("/{pointId}")
    public ResponseEntity<?> updateLocation(@RequestBody LocationDtoRequest request,
                                              @PathVariable long pointId) {

        if (pointId <= 0 || locationService.isValidateLocationRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (locationService.findLocationById(pointId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (locationService.isExistsByLongitudeAndLatitude(request.getLongitude(), request.getLatitude())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(convertLocationToLocationDtoResponse(locationService.updateLocation(pointId, request)));

    }

    @DeleteMapping("/{pointId}")
    public ResponseEntity<?> deleteLocation(@PathVariable long pointId) {

        Location deletinglocation = locationService.findLocationById(pointId);

        if (pointId > 0 && deletinglocation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (pointId <= 0 || locationService.isLocationDependedOnAnimal(deletinglocation)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        locationService.deleteLocation(pointId);
        return ResponseEntity.ok("");
    }

    public LocationDtoResponse convertLocationToLocationDtoResponse(Location location) {
        return modelMapper.map(location, LocationDtoResponse.class);
    }

    public Location convertrequestToLocation(LocationDtoRequest request) {
        return modelMapper.map(request, Location.class);
    }
}
