package com.example.demo.controller.impl;

import com.example.demo.controller.LocationsController;
import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.service.LocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Локации", description = "Управление точками локаций")
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationsControllerImpl implements LocationsController {
    private final LocationService locationServiceImpl;


    @Override
    public ResponseEntity<LocationDtoResponse> getLocation(@PathVariable Long id) {
        return ResponseEntity.ok().body(locationServiceImpl.findLocationById(id));
    }

    @Override
    public ResponseEntity<LocationDtoResponse> addLocation(@RequestBody LocationDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationServiceImpl.save(request));
    }

    @Override
    public ResponseEntity<LocationDtoResponse> updateLocation(@RequestBody LocationDtoRequest request,
                                              @PathVariable Long pointId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationServiceImpl.updateLocation(pointId, request));

    }

    @Override
    public ResponseEntity<Void> deleteLocation(@PathVariable Long pointId) {
        locationServiceImpl.deleteLocation(pointId);
        return ResponseEntity.ok().build();
    }


}
