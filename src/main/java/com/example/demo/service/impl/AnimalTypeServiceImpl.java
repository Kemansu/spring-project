package com.example.demo.service.impl;

import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.mapper.AnimalTypesMapper;
import com.example.demo.model.AnimalType;
import com.example.demo.repository.AnimalTypeRepository;
import com.example.demo.repository.AnimalTypesRepository;
import com.example.demo.service.AnimalTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnimalTypeServiceImpl implements AnimalTypeService {

    private final AnimalTypeRepository animalTypeRepository;

    private final AnimalTypesRepository animalTypesRepository;

    private final AnimalTypesMapper animalTypesMapper;


    @Override
    public AnimalType getAnimalTypeById(long typeId) throws ObjectNotFoundException, RequestValidationException {
        return animalTypeRepository.findById(typeId).orElseThrow(() -> new ObjectNotFoundException(""));
    }

    @Override
    public boolean isDependsOnAnimal(AnimalType animalType) {
        return animalTypesRepository.existsByAnimalType(animalType);
    }

    @Override
    @Transactional
    public AnimalType saveAnimalType(AnimalTypeDtoRequest request)
            throws RequestValidationException, ConflictDataException {
        if (isExistsAnimalType(request.getType())){
            throw new ConflictDataException("");
        }

        return animalTypeRepository.save(animalTypesMapper.toAnimalType(request));
    }

    @Override
    @Transactional
    public AnimalType updateAnimalType(long typeId, AnimalTypeDtoRequest request)
            throws RequestValidationException, ObjectNotFoundException, ConflictDataException{
        if (isExistsAnimalType(request.getType())) {
            throw new ConflictDataException("");
        }

        AnimalType existsAnimalType = animalTypeRepository.findById(typeId).orElseThrow(() -> new ObjectNotFoundException(""));

        existsAnimalType.setType(animalTypesMapper.toAnimalType(request).getType());

        return animalTypeRepository.save(existsAnimalType);
    }

    @Override
    public boolean isExistsAnimalType(String type) {
        return animalTypeRepository.existsAnimalTypeByType(type);
    }

    @Override
    @Transactional
    public void deleteAnimalType(long typeId) throws RequestValidationException, ObjectNotFoundException {
        if (isDependsOnAnimal(getAnimalTypeById(typeId))){
            throw new RequestValidationException("");
        }

        animalTypeRepository.deleteById(typeId);
    }


}
