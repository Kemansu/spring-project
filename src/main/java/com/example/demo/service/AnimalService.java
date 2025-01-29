package com.example.demo.service;

import com.example.demo.dto.animal.AnimalDtoCreateRequest;
import com.example.demo.dto.animal.AnimalDtoUpdateRequest;
import com.example.demo.dto.animal.AnimalDtoUpdateTypeRequest;
import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.model.Account;
import com.example.demo.model.Animal;


import java.util.List;

public interface AnimalService {

    Animal getAnimalById(long id);

    List<Animal> searchAnimalList(String startDateTime,
                                  String endDateTime,
                                  Long chipperId,
                                  Long chippingLocationId,
                                  LifeStatus lifeStatus,
                                  Gender gender,
                                  Integer from,
                                  Integer size);

    Animal addAnimal(AnimalDtoCreateRequest animalDtoCreateRequest);

    Animal updateAnimal(long id, AnimalDtoUpdateRequest request);

    void deleteAnimal(long id);

    Animal addAnimalType(long animalId, long typeId);

    Animal updateAnimalType(long animalId, AnimalDtoUpdateTypeRequest request);

    Animal deleteAnimalType(long animalId, long typeId);

    boolean isValideDeleteRequest(long animalId);

    boolean isAllForCreatingElementsExist(List<Long> animalTypes,
                                          int chipperId,
                                          long chippingLocationId);

    boolean isAllForUpdatingElementsExist(long animalId,
                                          int chipperId,
                                          long chippingLocationId);

    boolean isAllForAddingTypeElementsExist(long animalId, long typeId);

    boolean isAllForUpdateTypeExist(long animalId,
                                    long oldType,
                                    long newType);

    boolean isAlreadyInAnimal(long animalId, long newTypeId);

    boolean isTypeIdInAnimal(long animalId, long typeId);

    boolean isAnimalHasTypes(long animalId);

    boolean isAllForDeletingExist(long animalId, long typeId);

    boolean isAnimalAlive(long animalId);

    void addAnimalVisitedLocation(long animalId, long locationId);

    boolean isValideAnimalPosition(long animalId, long pointId);

}
