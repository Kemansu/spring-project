package com.example.demo.repository;

import com.example.demo.model.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalTypeRepository extends JpaRepository<AnimalType, Long> {
    boolean existsAnimalTypeByType(String type);
}
