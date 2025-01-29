package com.example.demo.repository;

import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalVisitedLocationsRepository extends JpaRepository<AnimalVisitedLocations, Long> {

    boolean existsByLocation(Location location);

    List<AnimalVisitedLocations> findAnimalVisitedLocationsByAnimalIdAndLocationId(long animalId, long locationId);

    @Query("SELECT avl FROM AnimalVisitedLocations avl " +
            "WHERE (avl.animal.id = :animalId)" +
            "AND (:startDateTime IS NULL OR avl.dateTimeOfVisitLocationPoint >= :startDateTime)" +
            "AND (:endDateTime IS NULL OR avl.dateTimeOfVisitLocationPoint <= :endDateTime)" +
            "ORDER BY avl.id"
    )
    List<AnimalVisitedLocations> findAnimalsVisitedLocationByParams(
            @Param("animalId") Long animalId,
            @Param("startDateTime") String startDateTime,
            @Param("endDateTime") String endDateTime
    );

}
