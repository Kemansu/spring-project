package com.example.demo.service;

import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.model.Location;
import jakarta.validation.constraints.Min;

public interface LocationService {

    LocationDtoResponse findLocationById(Long id);

    LocationDtoResponse save(LocationDtoRequest request);

    LocationDtoResponse updateLocation(Long id, LocationDtoRequest locationDtoRequest);

    void deleteLocation(Long id);

}
