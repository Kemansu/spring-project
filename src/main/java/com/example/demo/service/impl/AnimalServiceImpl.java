package com.example.demo.service.impl;

import com.example.demo.dto.animal.*;
import com.example.demo.enums.LifeStatus;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.mapper.AnimalMapper;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;
    private final AccountRepository accountRepository;
    private final LocationRepository locationRepository;
    private final AnimalTypeRepository animalTypeRepository;
    private final AnimalMapper animalMapper;




    @Override
    public Animal getAnimalById(Long id) {
        return animalRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(""));
    }

    @Override
    public List<AnimalDtoResponse> searchAnimalList(AnimalDtoSearchRequest request) {

        List<Animal> filtered = animalRepository.findAnimalsByParams(
                request.getStartDateTime(),
                request.getEndDateTime(),
                request.getChipperId(),
                request.getChippingLocationId(),
                request.getLifeStatus(),
                request.getGender()
        );

        int toIndex = Math.min(filtered.size(), request.getFrom() + request.getSize());
        return filtered.subList(request.getFrom(), toIndex)
                .stream()
                .map(animalMapper::toAnimalDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AnimalDtoResponse addAnimal(AnimalDtoCreateRequest request) {

        if (!isAllForCreatingElementsExist(request.getAnimalTypes(),
                request.getChipperId(),
                request.getChippingLocationId())) {
            throw new ObjectNotFoundException("");
        }

        boolean isAnimalTypesHasDuplicates = request.getAnimalTypes().size() >
                request.getAnimalTypes().stream().distinct().toList().size();


        if (isAnimalTypesHasDuplicates) {
            throw new ConflictDataException("");
        }

        Animal animal = animalMapper.toAnimalForCreating(request);

        Location location = locationRepository
                .findById(request.getChippingLocationId())
                .orElseThrow(() -> new ObjectNotFoundException(""));

        Account account = accountRepository
                .findById(request.getChipperId())
                .orElseThrow(() -> new ObjectNotFoundException(""));

        animal.setAccount(account);
        animal.setLocation(location);
        animal.setAnimalTypes(animalTypeRepository.findAllById(request.getAnimalTypes()));
        animal.setChippingDateTime(String.valueOf(Instant.now()));
        animal.setLifeStatus(LifeStatus.ALIVE);

        return animalMapper.toAnimalDtoResponse(animalRepository.save(animal));
    }

    @Override
    @Transactional
    public AnimalDtoResponse updateAnimal(Long animalId, AnimalDtoUpdateRequest request) {

        if (!isAllForUpdatingElementsExist(animalId, request.getChipperId(), request.getChippingLocationId())) {
            throw new ObjectNotFoundException("");
        }

        Animal existAnimal = animalRepository.findById(animalId).orElseThrow(() -> new ObjectNotFoundException(""));

        boolean isChippingLocationIdEqualsFirstVisitedLocation =
                !(existAnimal
                        .getVisitedLocations()
                        .isEmpty() ||
                        !Objects.equals(request.getChippingLocationId(), existAnimal
                                .getVisitedLocations()
                                .get(0)
                                .getLocation()
                                .getId()));

        if (isChippingLocationIdEqualsFirstVisitedLocation) {
            throw new RequestValidationException("");
        }


        existAnimal.setWeight(request.getWeight());
        existAnimal.setLength(request.getLength());
        existAnimal.setHeight(request.getHeight());
        existAnimal.setGender((request.getGender()));

        existAnimal.setLifeStatus(request.getLifeStatus());
        if (LifeStatus.DEAD.equals(request.getLifeStatus())) {
            existAnimal.setDeathDateTime(String.valueOf(Instant.now()));
        }

        existAnimal.setAccount(accountRepository
                .findById(request.getChipperId())
                .orElseThrow(() -> new ObjectNotFoundException("")));

        existAnimal.setLocation(locationRepository
                .findById(request.getChippingLocationId())
                .orElseThrow(() -> new ObjectNotFoundException("")));

        return animalMapper.toAnimalDtoResponse(animalRepository.save(existAnimal));
    }

    @Override
    @Transactional
    public void deleteAnimal(Long animalId) {
        if (!isValideDeleteRequest(animalId)) {
            throw new RequestValidationException("");
        }
        animalRepository.deleteById(animalId);
    }

    @Override
    @Transactional
    public Animal addAnimalType(Long animalId, Long typeId) {

        if (!isAllForAddingTypeElementsExist(animalId, typeId)) {
            throw new ObjectNotFoundException("");
        }

        if (isTypeIdInAnimal(animalId, typeId)) {
            throw new ConflictDataException("");
        }

        Animal existAnimal = animalRepository
                .findById(animalId)
                .orElseThrow(() -> new ObjectNotFoundException(""));

        existAnimal.addAnimalType(animalTypeRepository
                .findById(typeId)
                .orElseThrow(() -> new ObjectNotFoundException("")));

        return animalRepository.save(existAnimal);
    }

    @Override
    @Transactional
    public AnimalDtoResponse updateAnimalType(Long animalId, AnimalDtoUpdateTypeRequest request) {

        if (!isAllForUpdateTypeExist(animalId, request.getOldTypeId(), request.getNewTypeId())) {
            throw new ObjectNotFoundException("");
        }

        if (isAlreadyInAnimal(animalId, request.getNewTypeId())) {
            throw new ConflictDataException("");
        }

        Animal existAnimal = animalRepository.findById(animalId).orElseThrow(() -> new ObjectNotFoundException(""));

        AnimalType newAnimalType = animalTypeRepository
                .findById(request.getNewTypeId())
                .orElseThrow(() -> new ObjectNotFoundException(""));

        Integer indexOfUpdatingAnimalType = existAnimal.getAnimalTypes().indexOf(
                existAnimal.getAnimalTypes()
                        .stream()
                        .filter(type -> type.getId().equals(request.getOldTypeId()))
                        .findFirst().orElseThrow(() -> new ObjectNotFoundException("")));

        existAnimal.getAnimalTypes().set(indexOfUpdatingAnimalType, newAnimalType);

        return animalMapper.toAnimalDtoResponse(animalRepository.save(existAnimal));
    }

    @Override
    @Transactional
    public AnimalDtoResponse deleteAnimalType(Long animalId, Long typeId) {

        if (!isAllForDeletingExist(animalId, typeId)) {
            throw new ObjectNotFoundException("");
        }

        if (!isAnimalHasTypes(animalId)) {
            throw new RequestValidationException("");
        }

        Animal existAnimal = animalRepository.findById(animalId).orElseThrow(() -> new ObjectNotFoundException(""));

        existAnimal.removeAnimalType(animalTypeRepository
                .findById(typeId)
                .orElseThrow(() -> new ObjectNotFoundException("")));

        return animalMapper.toAnimalDtoResponse(animalRepository.save(existAnimal));
    }

    public boolean isValideDeleteRequest(Long animalId) {
        List<AnimalVisitedLocations> visitedLocation = animalRepository
                .findById(animalId)
                .orElseThrow(() -> new ObjectNotFoundException(""))
                .getVisitedLocations();

        return (visitedLocation.isEmpty());
    }


    private boolean isAllForCreatingElementsExist(List<Long> animalTypes,
                                                 Integer chipperId,
                                                 Long chippingLocationId) {


        return (animalTypes
                .stream()
                .filter(type -> !animalTypeRepository.existsById(type))
                .findFirst().isEmpty()) &&
                (accountRepository.existsById(chipperId) &&
                        (locationRepository.existsById(chippingLocationId)));

    }

    private boolean isAllForUpdatingElementsExist(Long animalId,
                                                 Integer chipperId,
                                                 Long chippingLocationId) {

        return (animalRepository.existsById(animalId)) &&
                (accountRepository.existsById(chipperId) &&
                        (locationRepository.existsById(chippingLocationId)));

    }

    private boolean isAllForAddingTypeElementsExist(Long animalId,
                                                   Long typeId) {

        return (animalRepository.existsById(animalId)) &&
                (animalTypeRepository.existsById(typeId));

    }

    private boolean isAllForUpdateTypeExist(Long animalId,
                                           Long oldType,
                                           Long newType) {
        return (animalTypeRepository.existsById(newType)) &&
                (animalRepository
                        .findById(animalId)
                        .orElseThrow(() -> new ObjectNotFoundException(""))
                        .getAnimalTypes()
                        .contains(
                                animalTypeRepository
                                        .findById(oldType)
                                        .orElseThrow(() -> new ObjectNotFoundException("")))
                );
    }

    private boolean isAlreadyInAnimal(Long animalId,
                                     Long newTypeId) {
        return (animalRepository
                .findById(animalId)
                .orElseThrow(() -> new ObjectNotFoundException(""))
                .getAnimalTypes()
                .contains(
                        animalTypeRepository
                                .findById(newTypeId)
                                .orElseThrow(() -> new ObjectNotFoundException(""))
                ));
    }

    private boolean isTypeIdInAnimal(Long animalId,
                                    Long typeId) {
        return (animalRepository.
                findById(animalId)
                .orElseThrow(() -> new ObjectNotFoundException(""))
                .getAnimalTypes()
                .contains(
                        animalTypeRepository
                                .findById(typeId)
                                .orElseThrow(() -> new ObjectNotFoundException(""))
                ));
    }

    private boolean isAnimalHasTypes(Long animalId) {
        return (animalRepository
                .findById(animalId)
                .orElseThrow(() -> new ObjectNotFoundException(""))
                .getAnimalTypes().size() > 1);
    }

    private boolean isAllForDeletingExist(Long animalId, Long typeId) {
        return (animalRepository
                        .findById(animalId)
                        .orElseThrow(() -> new ObjectNotFoundException(""))
                        .getAnimalTypes()
                        .contains(
                                animalTypeRepository
                                        .findById(typeId)
                                        .orElseThrow(() -> new ObjectNotFoundException(""))
                        )
                );
    }



}
