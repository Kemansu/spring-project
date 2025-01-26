package com.example.demo.controllerInterface;

import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.model.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface LocationsController {

    @GetMapping("/{id}")
    ResponseEntity<LocationDtoResponse> getLocation(@PathVariable @Min(value = 1) long id);

    @PostMapping("")
    ResponseEntity<LocationDtoResponse> addLocation(@RequestBody @Valid LocationDtoRequest request);

    @PutMapping("/{pointId}")
    ResponseEntity<LocationDtoResponse> updateLocation(@RequestBody @Valid LocationDtoRequest request,
                                     @PathVariable @Min(value = 1) long pointId);

    @DeleteMapping("/{pointId}")
    ResponseEntity<String> deleteLocation(@PathVariable @Min(value = 1) long pointId);
}
