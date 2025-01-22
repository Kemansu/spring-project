package com.example.demo.repository;

import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalVisitedLocationsRepository extends JpaRepository<AnimalVisitedLocations, Long> {

    List<AnimalVisitedLocations> findAllByAnimalId(long animalId);

    boolean existsByLocation(Location location);

    List<AnimalVisitedLocations> findAnimalVisitedLocationsByAnimalIdAndLocationId(long animalId, long locationId);

    boolean existsByAnimalIdAndLocationId(long animalId, long locationId);
    void deleteByAnimalIdAndLocationId(long animalId, long locationId);

}
