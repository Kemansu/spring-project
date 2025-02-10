package com.example.demo.mapper;

import com.example.demo.dto.animal.AnimalDtoCreateRequest;
import com.example.demo.dto.animal.AnimalDtoResponse;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalType;
import com.example.demo.model.AnimalVisitedLocations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    @Mapping(source = "account.id", target = "chipperId")
    @Mapping(source = "location.id", target = "chippingLocationId")
    @Mapping(source = "animalTypes", target = "animalTypes", qualifiedByName = "mapAnimalTypesToIds")
    @Mapping(source = "visitedLocations", target = "visitedLocations", qualifiedByName = "mapVisitedLocations")
    AnimalDtoResponse toAnimalDtoResponse(Animal animal);



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "animalTypes", ignore = true)
    @Mapping(target = "visitedLocations", ignore = true)
    @Mapping(target = "deathDateTime", ignore = true)
    @Mapping(target = "lifeStatus", ignore = true)
    @Mapping(target = "chippingDateTime", ignore = true)
    Animal toAnimalForCreating(AnimalDtoCreateRequest request);

    @Named("mapAnimalTypesToIds")
    default List<Long> mapAnimalTypesToIds(List<AnimalType> animalTypes) {
        return animalTypes.stream().map(AnimalType::getId).toList();
    }

    @Named("mapVisitedLocations")
    default List<Long> mapVisitedLocations(List<AnimalVisitedLocations> animalVisitedLocations) {
        return animalVisitedLocations.stream().map(AnimalVisitedLocations::getId).toList();
    }

}
