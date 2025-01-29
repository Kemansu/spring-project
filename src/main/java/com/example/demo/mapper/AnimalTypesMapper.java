package com.example.demo.mapper;

import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
import com.example.demo.model.AnimalType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalTypesMapper {

    AnimalTypeDtoResponse toAnimalTypeDtoResponse(AnimalType animalType);

    @Mapping(target = "id", expression = "java(0)")
    AnimalType toAnimalType(AnimalTypeDtoRequest animalTypeDtoRequest);
}
