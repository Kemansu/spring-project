package com.example.demo.service.impl;

import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
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
    public AnimalTypeDtoResponse getAnimalTypeById(Long typeId) {
        return animalTypesMapper.toAnimalTypeDtoResponse(
                animalTypeRepository
                        .findById(typeId)
                        .orElseThrow(() -> new ObjectNotFoundException(""))
        );
    }

    private boolean isDependsOnAnimal(AnimalType animalType) {
        return animalTypesRepository.existsByAnimalType(animalType);
    }

    @Override
    @Transactional
    public AnimalTypeDtoResponse saveAnimalType(AnimalTypeDtoRequest request) {
        if (isExistsAnimalType(request.getType())){
            throw new ConflictDataException("");
        }

        return animalTypesMapper
                .toAnimalTypeDtoResponse(animalTypeRepository.save(animalTypesMapper.toAnimalType(request)));
    }

    @Override
    @Transactional
    public AnimalTypeDtoResponse updateAnimalType(Long typeId, AnimalTypeDtoRequest request) {

        var existsAnimalType = animalTypeRepository
                .findById(typeId)
                .orElseThrow(() -> new ObjectNotFoundException(""));

        if (isExistsAnimalType(request.getType())){
            throw new ConflictDataException("");
        }

        existsAnimalType.setType(animalTypesMapper.toAnimalType(request).getType());

        return animalTypesMapper.toAnimalTypeDtoResponse(animalTypeRepository.save(existsAnimalType));
    }

    private boolean isExistsAnimalType(String type) {
        return animalTypeRepository.existsAnimalTypeByType(type);
    }

    @Override
    @Transactional
    public void deleteAnimalType(Long typeId) {
        if (isDependsOnAnimal(
                animalTypeRepository
                .findById(typeId)
                .orElseThrow(() -> new ObjectNotFoundException("")))){
            throw new RequestValidationException("");
        }

        animalTypeRepository.deleteById(typeId);
    }


}
