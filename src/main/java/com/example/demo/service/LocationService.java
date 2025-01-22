package com.example.demo.service;

import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.model.Location;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.repository.AnimalVisitedLocationsRepository;
import com.example.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;

    private final AnimalRepository animalRepository;

    private final AnimalVisitedLocationsRepository animalVisitedLocationsRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository, AnimalRepository animalRepository, AnimalVisitedLocationsRepository animalVisitedLocationsRepository) {
        this.locationRepository = locationRepository;
        this.animalRepository = animalRepository;
        this.animalVisitedLocationsRepository = animalVisitedLocationsRepository;
    }

    public Location findLocationById(long id) {
        return locationRepository.findById(id).orElse(null);
    }

    @Transactional
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Transactional
    public Location updateLocation(long id, LocationDtoRequest locationDtoRequest) {
        Location existLocation = locationRepository.findById(id).orElse(null);


        existLocation.setLatitude(locationDtoRequest.getLatitude());
        existLocation.setLongitude(locationDtoRequest.getLongitude());

        return locationRepository.save(existLocation);
    }

    @Transactional
    public void deleteLocation(long id) {
        locationRepository.deleteById(id);
    }

    public boolean isExistsByLongitudeAndLatitude(Double longitude, Double latitude) {
        return locationRepository.existsByLongitudeAndLatitude(longitude, latitude);
    }

    public boolean isLocationDependedOnAnimal(Location location) {
        return (animalVisitedLocationsRepository.existsByLocation(location)) ||
                (animalRepository.existsByLocation(location));
    }

    public boolean isLocationExists(long locationId) {
        return locationRepository.existsById(locationId);
    }


    public boolean isValidateLocationRequest(LocationDtoRequest request) {
        return (request.getLongitude() == null) ||
                (request.getLongitude() < -180) ||
                (request.getLongitude() > 180) ||
                (request.getLatitude() == null) ||
                (request.getLatitude() < -90) ||
                (request.getLatitude() > 90);
    }
}
