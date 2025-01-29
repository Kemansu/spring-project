package com.example.demo.mapper;

import com.example.demo.dto.animal.AnimalDtoCreateRequest;
import com.example.demo.dto.animal.AnimalDtoResponse;
import com.example.demo.enums.LifeStatus;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalType;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AnimalTypeRepository;
import com.example.demo.repository.LocationRepository;
import org.mapstruct.Context;
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

    @Mapping(target = "chippingDateTime", expression = "java(java.time.Instant.now().toString())")
    @Mapping(target = "lifeStatus", expression = "java(com.example.demo.enums.LifeStatus.ALIVE)")
    @Mapping(target = "deathDateTime", expression = "java(null)")
    @Mapping(target = "account", expression = "java(accountRepository.findById(request.getChipperId()).orElse(null))")
    @Mapping(target = "location",
            expression = "java(locationRepository.findById(request.getChippingLocationId()).orElse(null))")
    @Mapping(target = "animalTypes", qualifiedByName = "mapAnimalTypeIdsToEntityList")
    @Mapping(target = "id", expression = "java(0)")
    @Mapping(target = "visitedLocations", expression = "java(java.util.List.of())")
    Animal toAnimal(AnimalDtoCreateRequest request,
                    @Context AnimalTypeRepository animalTypeRepository,
                    @Context AccountRepository accountRepository,
                    @Context LocationRepository locationRepository);

    @Named("mapAnimalTypesToIds")
    default List<Long> mapAnimalTypesToIds(List<AnimalType> animalTypes) {
        return animalTypes.stream().map(AnimalType::getId).toList();
    }

    @Named("mapVisitedLocations")
    default List<Long> mapVisitedLocations(List<AnimalVisitedLocations> animalVisitedLocations) {
        return animalVisitedLocations.stream().map(AnimalVisitedLocations::getId).toList();
    }

    @Named("mapLifeStatus")
    default LifeStatus mapLifeStatus() {
        return LifeStatus.ALIVE;
    }

    @Named("mapChippingDateTime")
    default String mapChippingDateTime() {
        return String.valueOf(Instant.now());
    }

    @Named("mapAnimalTypeIdsToEntityList")
    default List<AnimalType> mapAnimalTypeIdsToEntityList(List<Long> animalTypeIds,
                                                          @Context AnimalTypeRepository animalTypeRepository) {
        return animalTypeRepository.findAllById(animalTypeIds);
    }


}
