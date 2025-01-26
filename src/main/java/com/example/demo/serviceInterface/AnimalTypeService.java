package com.example.demo.serviceInterface;

import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.model.AnimalType;


public interface AnimalTypeService {

    AnimalType getAnimalTypeById(long id);

    boolean isDependsOnAnimal(AnimalType animalType);

    AnimalType saveAnimalType(AnimalTypeDtoRequest request);

    AnimalType updateAnimalType(long id, AnimalTypeDtoRequest request);

    boolean isExistsAnimalType(String type);

    void deleteAnimalType(long id);

    AnimalType convertAnimalTypeDtoRequestToAnimalType(AnimalTypeDtoRequest animalTypeDtoRequest);

}
