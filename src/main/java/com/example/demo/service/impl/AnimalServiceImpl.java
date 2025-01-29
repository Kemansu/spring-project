package com.example.demo.service.impl;

import com.example.demo.dto.animal.AnimalDtoCreateRequest;
import com.example.demo.dto.animal.AnimalDtoUpdateRequest;
import com.example.demo.dto.animal.AnimalDtoUpdateTypeRequest;
import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.mapper.AnimalMapper;
import com.example.demo.model.Account;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.model.Location;
import com.example.demo.repository.*;
import com.example.demo.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

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
    public Animal getAnimalById(long id) throws ObjectNotFoundException, RequestValidationException {
        if (id <= 0) {
            throw new RequestValidationException("");
        }
        return animalRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(""));
    }

    @Override
    public List<Animal> searchAnimalList(String startDateTime,
                                         String endDateTime,
                                         Long chipperId,
                                         Long chippingLocationId,
                                         LifeStatus lifeStatus,
                                         Gender gender,
                                         Integer from,
                                         Integer size) throws RequestValidationException {

        List<Animal> filtered = animalRepository.findAnimalsByParams(
                startDateTime,
                endDateTime,
                chipperId,
                chippingLocationId,
                lifeStatus,
                gender
        );

        int toIndex = Math.min(filtered.size(), from + size);
        return filtered.subList(from, toIndex);
    }

    @Override
    @Transactional
    public Animal addAnimal(AnimalDtoCreateRequest request)
            throws RequestValidationException, ObjectNotFoundException, ConflictDataException {

        if (!isAllForCreatingElementsExist(request.getAnimalTypes(),
                request.getChipperId(),
                request.getChippingLocationId())) {
            throw new ObjectNotFoundException("");
        }

        if (request.getAnimalTypes().size() >
                        new HashSet<>(request.getAnimalTypes()).size()) {
            throw new ConflictDataException("");
        }

        Animal animal = animalMapper.toAnimal(request, animalTypeRepository, accountRepository, locationRepository);

        return animalRepository.save(animal);
    }

    @Override
    @Transactional
    public Animal updateAnimal(long animalId, AnimalDtoUpdateRequest request)
            throws RequestValidationException, ObjectNotFoundException {

        if (!isAllForUpdatingElementsExist(animalId, request.getChipperId(), request.getChippingLocationId())) {
            throw new ObjectNotFoundException("");
        }

        boolean isChippingLocationIdEqualsFirstVisitedLocation = !(animalRepository.findById(animalId).get().getVisitedLocations().isEmpty() || request.getChippingLocationId() != animalRepository.findById(animalId).get().getVisitedLocations().get(0).getLocation().getId());

        if (isChippingLocationIdEqualsFirstVisitedLocation) {
            throw new RequestValidationException("");
        }



        Animal existAnimal = animalRepository.findById(animalId).get();

        existAnimal.setWeight(request.getWeight());
        existAnimal.setLength(request.getLength());
        existAnimal.setHeight(request.getHeight());
        existAnimal.setGender((request.getGender()));

        existAnimal.setLifeStatus(request.getLifeStatus());
        if (request.getLifeStatus().equals(LifeStatus.DEAD)) {
            existAnimal.setDeathDateTime(String.valueOf(Instant.now()));
        }

        existAnimal.setAccount(accountRepository.findById(request.getChipperId()).get());
        existAnimal.setLocation(locationRepository.findById(request.getChippingLocationId()).get());

        return animalRepository.save(existAnimal);
    }

    @Override
    @Transactional
    public void deleteAnimal(long animalId) throws RequestValidationException, ObjectNotFoundException {
        if (!isValideDeleteRequest(animalId)) {
            throw new RequestValidationException("");
        }
        animalRepository.deleteById(animalId);
    }

    @Override
    @Transactional
    public Animal addAnimalType(long animalId, long typeId)
            throws RequestValidationException, ObjectNotFoundException, ConflictDataException {

        if (!isAllForAddingTypeElementsExist(animalId, typeId)) {
            throw new ObjectNotFoundException("");
        }

        if (isTypeIdInAnimal(animalId, typeId)) {
            throw new ConflictDataException("");
        }

        Animal existAnimal = animalRepository.findById(animalId).get();

        existAnimal.addAnimalType(animalTypeRepository.findById(typeId).get());
        return animalRepository.save(existAnimal);
    }

    @Override
    @Transactional
    public Animal updateAnimalType(long animalId, AnimalDtoUpdateTypeRequest request)
            throws RequestValidationException, ObjectNotFoundException, ConflictDataException {

        if (!isAllForUpdateTypeExist(animalId, request.getOldTypeId(), request.getNewTypeId())) {
            throw new ObjectNotFoundException("");
        }

        if (isAlreadyInAnimal(animalId, request.getNewTypeId())) {
            throw new ConflictDataException("");
        }

        Animal existAnimal = animalRepository.findById(animalId).get();

        existAnimal.getAnimalTypes().set(
                existAnimal.getAnimalTypes().indexOf(
                        existAnimal.getAnimalTypes()
                                .stream()
                                .filter(type -> type.getId() == request.getOldTypeId())
                                .findFirst().get()
                ), animalTypeRepository.findById(request.getNewTypeId()).get());

        return animalRepository.save(existAnimal);
    }

    @Override
    @Transactional
    public Animal deleteAnimalType(long animalId, long typeId)
            throws RequestValidationException, ObjectNotFoundException {

        if (!isAllForDeletingExist(animalId, typeId)) {
            throw new ObjectNotFoundException("");
        }

        if (!isAnimalHasTypes(animalId)) {
            throw new RequestValidationException("");
        }

        Animal existAnimal = animalRepository.findById(animalId).get();

        existAnimal.removeAnimalType(animalTypeRepository.findById(typeId).get());
        return animalRepository.save(existAnimal);
    }

    @Override
    public boolean isValideDeleteRequest(long animalId) {
        List<AnimalVisitedLocations> visitedLocation = animalRepository
                .findById(animalId)
                .orElseThrow(() -> new ObjectNotFoundException(""))
                .getVisitedLocations();

        return (visitedLocation.isEmpty());
    }

    @Override
    public boolean isAllForCreatingElementsExist(List<Long> animalTypes,
                                                 int chipperId,
                                                 long chippingLocationId) {


        return (animalTypes
                .stream()
                .filter(type -> animalTypeRepository.findById(type).orElse(null) == null)
                .findFirst().isEmpty()) &&
                (accountRepository.existsById(chipperId) &&
                        (locationRepository.existsById(chippingLocationId)));

    }

    @Override
    public boolean isAllForUpdatingElementsExist(long animalId,
                                                 int chipperId,
                                                 long chippingLocationId) {

        return (animalRepository.existsById(animalId)) &&
                (accountRepository.existsById(chipperId) &&
                        (locationRepository.existsById(chippingLocationId)));

    }

    @Override
    public boolean isAllForAddingTypeElementsExist(long animalId,
                                                   long typeId) {

        return (animalRepository.existsById(animalId)) &&
                (animalTypeRepository.existsById(typeId));

    }

    @Override
    public boolean isAllForUpdateTypeExist(long animalId,
                                           long oldType,
                                           long newType) {
        return (animalRepository.existsById(animalId)) &&
                (animalTypeRepository.existsById(oldType)) &&
                (animalTypeRepository.existsById(newType)) &&
                (animalRepository
                        .findById(animalId)
                        .get()
                        .getAnimalTypes()
                        .contains(
                                animalTypeRepository
                                        .findById(oldType)
                                        .get())
                );
    }

    @Override
    public boolean isAlreadyInAnimal(long animalId,
                                     long newTypeId) {
        return (animalRepository
                .findById(animalId)
                .get()
                .getAnimalTypes()
                .contains(
                        animalTypeRepository
                                .findById(newTypeId)
                                .get()
                ));
    }

    @Override
    public boolean isTypeIdInAnimal(long animalId,
                                    long typeId) {
        return (animalRepository.
                findById(animalId)
                .get()
                .getAnimalTypes()
                .contains(
                        animalTypeRepository
                                .findById(typeId)
                                .get()
                ));
    }

    @Override
    public boolean isAnimalHasTypes(long animalId) {
        return (animalRepository
                .findById(animalId)
                .get()
                .getAnimalTypes().size() > 1);
    }

    @Override
    public boolean isAllForDeletingExist(long animalId, long typeId) {
        return (animalRepository.existsById(animalId)) &&
                (animalTypeRepository.existsById(typeId)) &&
                (animalRepository
                        .findById(animalId)
                        .get()
                        .getAnimalTypes()
                        .contains(
                                animalTypeRepository
                                        .findById(typeId)
                                        .get()
                        )
                );
    }

    @Override
    public boolean isAnimalAlive(long animalId) {
        return animalRepository
                .findById(animalId)
                .get()
                .getLifeStatus()
                .equals(LifeStatus.ALIVE);
    }

    @Override
    @Transactional
    public void addAnimalVisitedLocation(long animalId, long locationId)
            throws ObjectNotFoundException, RequestValidationException{

        if ((!animalRepository.existsById(animalId) || !locationRepository.existsById(locationId))) {
            throw new ObjectNotFoundException("");
        }

        if (!isAnimalAlive(animalId) || !isValideAnimalPosition(animalId, locationId)) {
            throw new RequestValidationException("");
        }

        Animal existAnimal = animalRepository.findById(animalId).get();
        Location location = locationRepository.findById(locationId).get();

        existAnimal.addVisitedLocation(location);

        animalRepository.save(existAnimal);
    }

    @Override
    public boolean isValideAnimalPosition(long animalId, long pointId) {
        Animal animal = animalRepository.findById(animalId).get();
        Location location = locationRepository.findById(pointId).get();

        if (animal.getVisitedLocations().isEmpty() && animal.getLocation().equals(location)) {
            return false;
        }


        return (animal.getVisitedLocations().isEmpty() ||
                !animal.getVisitedLocations()
                        .stream()
                        .sorted(Comparator.comparing(AnimalVisitedLocations::getDateTimeOfVisitLocationPoint)).toList()
                        .get(animal.getVisitedLocations().size() - 1)
                        .getLocation()
                        .equals(location));
    }


}
