package com.example.demo.controller;

import com.example.demo.controllerInterface.LocationsController;
import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.Location;
import com.example.demo.serviceInterface.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationsControllerImpl implements LocationsController {
    private final LocationService locationServiceImpl;

    private final ModelMapper modelMapper;


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<LocationDtoResponse> getLocation(@PathVariable @Min(value = 1) long id) {
        return ResponseEntity.ok().body(locationServiceImpl.findLocationById(id));
    }

    @Override
    @PostMapping("")
    public ResponseEntity<LocationDtoResponse> addLocation(@RequestBody @Valid LocationDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationServiceImpl.save(request));
    }

    @Override
    @PutMapping("/{pointId}")
    public ResponseEntity<LocationDtoResponse> updateLocation(@RequestBody @Valid LocationDtoRequest request,
                                              @PathVariable @Min(value = 1) long pointId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationServiceImpl.updateLocation(pointId, request));

    }

    @Override
    @DeleteMapping("/{pointId}")
    public ResponseEntity<String> deleteLocation(@PathVariable @Min(value = 1) long pointId) {

        locationServiceImpl.deleteLocation(pointId);
        return ResponseEntity.ok("");
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
