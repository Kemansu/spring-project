package com.example.demo.service;

import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
import com.example.demo.model.AnimalType;


public interface AnimalTypeService {

    AnimalTypeDtoResponse getAnimalTypeById(Long id);

    AnimalTypeDtoResponse saveAnimalType(AnimalTypeDtoRequest request);

    AnimalTypeDtoResponse updateAnimalType(Long id, AnimalTypeDtoRequest request);

    void deleteAnimalType(Long id);

}
