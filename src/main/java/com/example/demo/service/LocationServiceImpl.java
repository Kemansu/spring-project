package com.example.demo.service;

import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.Location;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.repository.AnimalVisitedLocationsRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.serviceInterface.LocationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;


    @Override
    public LocationDtoResponse findLocationById(long id){
        return convertLocationToLocationDtoResponse(
                locationRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(""))
        );
    }

    @Override
    @Transactional
    public LocationDtoResponse save(LocationDtoRequest request) throws RequestValidationException, ConflictDataException {
        if (isExistsByLongitudeAndLatitude(request.getLongitude(), request.getLatitude())) {
            throw new ConflictDataException("");
        }
        return convertLocationToLocationDtoResponse(locationRepository.save(convertrequestToLocation(request)));
    }

    @Override
    @Transactional
    public LocationDtoResponse updateLocation(long pointId, LocationDtoRequest request)
            throws RequestValidationException, ConflictDataException{
        Location existLocation = locationRepository.findById(pointId).orElseThrow(() -> new ObjectNotFoundException(""));

        if (isExistsByLongitudeAndLatitude(request.getLongitude(), request.getLatitude())) {
            throw new ConflictDataException("");
        }

        existLocation.setLatitude(request.getLatitude());
        existLocation.setLongitude(request.getLongitude());

        return convertLocationToLocationDtoResponse(locationRepository.save(existLocation));
    }

    @Override
    @Transactional
    public void deleteLocation(long pointId) throws ObjectNotFoundException, RequestValidationException {

        if (isLocationDependedOnAnimal(
                locationRepository.findById(pointId).orElseThrow(() -> new ObjectNotFoundException("")))) {
            throw new RequestValidationException("");
        }

        locationRepository.deleteById(pointId);
    }

    @Override
    public boolean isExistsByLongitudeAndLatitude(Double longitude, Double latitude) {
        return locationRepository.existsByLongitudeAndLatitude(longitude, latitude);
    }

    @Override
    public boolean isLocationDependedOnAnimal(Location location) {
        return (animalVisitedLocationsRepository.existsByLocation(location)) ||
                (animalRepository.existsByLocation(location));
    }

    @Override
    public LocationDtoResponse convertLocationToLocationDtoResponse(Location location) {
        return modelMapper.map(location, LocationDtoResponse.class);
    }

    @Override
    public Location convertrequestToLocation(LocationDtoRequest request) {
        return modelMapper.map(request, Location.class);
    }
}
