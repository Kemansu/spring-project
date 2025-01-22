package com.example.demo.repository;

import com.example.demo.model.AnimalType;
import com.example.demo.model.AnimalTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalTypesRepository extends JpaRepository<AnimalTypes, Long> {

    boolean existsByAnimalType(AnimalType animalType);
}
