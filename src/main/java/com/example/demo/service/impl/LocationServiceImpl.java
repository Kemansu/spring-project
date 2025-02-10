package com.example.demo.service.impl;

import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.mapper.LocationMapper;
import com.example.demo.model.Location;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.repository.AnimalVisitedLocationsRepository;
import com.example.demo.repository.LocationRepository;

import com.example.demo.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    private final AnimalRepository animalRepository;

    private final AnimalVisitedLocationsRepository animalVisitedLocationsRepository;

    private final LocationMapper locationMapper;


    @Override
    public LocationDtoResponse findLocationById(Long id){
        return locationMapper.toLocationDtoResponse(
                locationRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(""))
        );
    }

    @Override
    @Transactional
    public LocationDtoResponse save(LocationDtoRequest request) {

        if (isExistsByLongitudeAndLatitude(request.getLongitude(), request.getLatitude())) {
            throw new ConflictDataException("");
        }

        return locationMapper.toLocationDtoResponse(locationRepository.save(locationMapper.toLocation(request)));
    }

    @Override
    @Transactional
    public LocationDtoResponse updateLocation(Long pointId, LocationDtoRequest request) {
        Location existLocation = locationRepository.findById(pointId).orElseThrow(() -> new ObjectNotFoundException(""));

        if (isExistsByLongitudeAndLatitude(request.getLongitude(), request.getLatitude())) {
            throw new ConflictDataException("");
        }

        existLocation.setLatitude(request.getLatitude());
        existLocation.setLongitude(request.getLongitude());

        return locationMapper.toLocationDtoResponse(locationRepository.save(existLocation));
    }

    @Override
    @Transactional
    public void deleteLocation(Long pointId) {

        if (isLocationDependedOnAnimal(
                locationRepository.findById(pointId).orElseThrow(() -> new ObjectNotFoundException("")))) {
            throw new RequestValidationException("");
        }

        locationRepository.deleteById(pointId);
    }

    private boolean isExistsByLongitudeAndLatitude(Double longitude, Double latitude) {
        return locationRepository.existsByLongitudeAndLatitude(longitude, latitude);
    }

    private boolean isLocationDependedOnAnimal(Location location) {
        return (animalVisitedLocationsRepository.existsByLocation(location)) ||
                (animalRepository.existsByLocation(location));
    }
}
