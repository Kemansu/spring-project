package com.example.demo.service;

import com.example.demo.model.AnimalType;
import com.example.demo.repository.AnimalTypeRepository;
import com.example.demo.repository.AnimalTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AnimalTypeService {

    private final AnimalTypeRepository animalTypeRepository;

    private final AnimalTypesRepository animalTypesRepository;

    @Autowired
    public AnimalTypeService(AnimalTypeRepository animalTypeRepository, AnimalTypesRepository animalTypesRepository) {
        this.animalTypeRepository = animalTypeRepository;
        this.animalTypesRepository = animalTypesRepository;
    }

    public AnimalType getAnimalTypeById(long id) {
        return animalTypeRepository.findById(id).orElse(null);
    }

    public boolean isDependsOnAnimal(AnimalType animalType) {
        return animalTypesRepository.existsByAnimalType(animalType);
    }

    @Transactional
    public AnimalType saveAnimalType(AnimalType animalType) {
        return animalTypeRepository.save(animalType);
    }

    @Transactional
    public AnimalType updateAnimalType(long id, AnimalType newAnimalType) {
        AnimalType existsAnimalType = animalTypeRepository.findById(id).orElse(null);

        existsAnimalType.setType(newAnimalType.getType());

        return animalTypeRepository.save(existsAnimalType);
    }

    public boolean isExistsAnimalType(String type) {
        return animalTypeRepository.existsAnimalTypeByType(type);
    }

    @Transactional
    public void deleteAnimalType(long id) {
        animalTypeRepository.deleteById(id);
    }


}
