package com.example.demo.serviceInterface;

import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.model.Location;
import jakarta.validation.constraints.Min;

public interface LocationService {

    LocationDtoResponse findLocationById(long id);

    LocationDtoResponse save(LocationDtoRequest request);

    LocationDtoResponse updateLocation(long id, LocationDtoRequest locationDtoRequest);

    void deleteLocation(long id);

    boolean isExistsByLongitudeAndLatitude(Double longitude, Double latitude);

    boolean isLocationDependedOnAnimal(Location location);

    LocationDtoResponse convertLocationToLocationDtoResponse(Location location);

    Location convertrequestToLocation(LocationDtoRequest request);
}
