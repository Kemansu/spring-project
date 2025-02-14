package com.example.demo.service;

import com.example.demo.dto.animal.*;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.model.Animal;


import java.util.List;

public interface AnimalService {

    Animal getAnimalById(Long id);

    Integer getNumberOfAnimals();

    List<AnimalDtoResponse> searchAnimalList(AnimalDtoSearchRequest animalDtoSearchRequest);

    AnimalDtoResponse addAnimal(AnimalDtoCreateRequest animalDtoCreateRequest);

    AnimalDtoResponse updateAnimal(Long id, AnimalDtoUpdateRequest request);

    void deleteAnimal(Long id);

    Animal addAnimalType(Long animalId, Long typeId);

    AnimalDtoResponse updateAnimalType(Long animalId, AnimalDtoUpdateTypeRequest request);

    AnimalDtoResponse deleteAnimalType(Long animalId, Long typeId);

}
