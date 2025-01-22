package com.example.demo.service;

import com.example.demo.dto.animal.AnimalDtoCreateRequest;
import com.example.demo.dto.animal.AnimalDtoUpdateRequest;
import com.example.demo.dto.animal.AnimalDtoUpdateTypeRequest;
import com.example.demo.model.Account;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.model.Location;
import com.example.demo.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.ParseException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnimalService {
    private final AnimalRepository animalRepository;

    private final AccountRepository accountRepository;

    private final LocationRepository locationRepository;

    private final AnimalTypeRepository animalTypeRepository;

    private final AnimalVisitedLocationsRepository animalVisitedLocationsRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public AnimalService(AnimalRepository animalRepository,
                         AccountRepository accountRepository,
                         LocationRepository locationRepository,
                         AnimalTypeRepository animalTypeRepository, AnimalVisitedLocationsRepository animalVisitedLocationsRepository,
                         ModelMapper modelMapper) {
        this.animalRepository = animalRepository;
        this.accountRepository = accountRepository;
        this.locationRepository = locationRepository;
        this.animalTypeRepository = animalTypeRepository;
        this.animalVisitedLocationsRepository = animalVisitedLocationsRepository;
        this.modelMapper = modelMapper;
    }

    public Animal getAnimalById(long id) {
        return animalRepository.findById(id).orElse(null);
    }

    public List<Animal> searchAnimalList(String startDateTime,
                                         String endDateTime,
                                         Long chipperId,
                                         Long chippingLocationId,
                                         String lifeStatus,
                                         String gender,
                                         Integer from,
                                         Integer size) {
        List<Animal> filtered = animalRepository.findAll().stream().filter(animal -> (
                (startDateTime == null || isWithinRange(animal.getChippingDateTime(), startDateTime, endDateTime))  &&
                        (chipperId == null || animal.getAccount().getId() == chipperId) &&
                        (chippingLocationId == null || animal.getLocation().getId() == chippingLocationId) &&
                        (lifeStatus == null || animal.getLifeStatus().equals(lifeStatus)) &&
                        (gender == null || animal.getGender().equals(gender))
                        )
                )
                .sorted(Comparator.comparing(Animal::getId))
                .toList();

        int toIndex = Math.min(filtered.size(), from + size);
        return filtered.subList(from, toIndex);

    }

    @Transactional
    public Animal addAnimal(AnimalDtoCreateRequest animalDtoCreateRequest) throws ParseException {
        Animal animal = convertAnimalDtoRequestToAnimal(animalDtoCreateRequest);
        System.out.println(animal.getChippingDateTime());
        return animalRepository.save(animal);

    }

    @Transactional
    public Animal updateAnimal(long id, AnimalDtoUpdateRequest request) {
        Animal existAnimal = animalRepository.findById(id).orElse(null);

        existAnimal.setWeight(request.getWeight());
        existAnimal.setLength(request.getLength());
        existAnimal.setHeight(request.getHeight());
        existAnimal.setGender(request.getGender());

        existAnimal.setLifeStatus(request.getLifeStatus());
        if (request.getLifeStatus().equals("DEAD")) {
            existAnimal.setDeathDateTime(String.valueOf(Instant.now()));
        }

        existAnimal.setAccount(accountRepository.findById(request.getChipperId()).get());
        existAnimal.setLocation(locationRepository.findById(request.getChippingLocationId()).get());

        return animalRepository.save(existAnimal);
    }

    @Transactional
    public void deleteAnimal(long id) {
        animalRepository.deleteById(id);
    }

    @Transactional
    public Animal addAnimalType(long animalId, long typeId) {
        Animal existAnimal = animalRepository.findById(animalId).orElse(null);

        existAnimal.addAnimalType(animalTypeRepository.findById(typeId).orElse(null));
        return animalRepository.save(existAnimal);
    }

    @Transactional
    public Animal updateAnimalType(long animalId, AnimalDtoUpdateTypeRequest request) {
        Animal existAnimal = animalRepository.findById(animalId).orElse(null);

        existAnimal.getAnimalTypes().set(
                existAnimal.getAnimalTypes().indexOf(
                        existAnimal.getAnimalTypes()
                                .stream()
                                .filter(type -> type.getId() == request.getOldTypeId())
                                .findFirst().get()
                ), animalTypeRepository.findById(request.getNewTypeId()).orElse(null));

        return animalRepository.save(existAnimal);
    }

    @Transactional
    public Animal deleteAnimalType(long animalId, long typeId) {
        Animal existAnimal = animalRepository.findById(animalId).orElse(null);

        existAnimal.removeAnimalType(animalTypeRepository.findById(typeId).orElse(null));
        return animalRepository.save(existAnimal);
    }

    public boolean isValideAnimalCreateRequest(AnimalDtoCreateRequest request) {
        return (request.getAnimalTypes() != null) &&
                (!request.getAnimalTypes().isEmpty()) &&
                (!request.getAnimalTypes().contains(null)) &&
                (request.getAnimalTypes().stream().noneMatch(type -> type <= 0)) &&
                (request.getWeight() > 0) &&
                (request.getLength() > 0) &&
                (request.getHeight() > 0) &&
                (request.getGender() != null) &&
                (request.getGender().equals("MALE") ||
                        request.getGender().equals("FEMALE") ||
                        request.getGender().equals("OTHER")
                ) &&
                (request.getChipperId() > 0) &&
                (request.getChippingLocationId() > 0);
    }

    public boolean isValideAnimalUpdateRequest(AnimalDtoUpdateRequest request, long animalId) {
        return (request.getWeight() > 0) &&
                (request.getLength() > 0) &&
                (request.getHeight() > 0) &&
                (request.getGender() != null) &&
                (request.getGender().equals("MALE") ||
                        request.getGender().equals("FEMALE") ||
                        request.getGender().equals("OTHER")
                ) &&
                (request.getLifeStatus() != null) &&
                (request.getLifeStatus().equals("ALIVE") || request.getLifeStatus().equals("DEAD")) &&
                (request.getChipperId() > 0) &&
                (request.getChippingLocationId() > 0) &&
                !(request.getLifeStatus().equals("ALIVE") && animalRepository.findById(animalId).get().getLifeStatus().equals("DEAD")) &&
                (animalRepository.findById(animalId).get().getVisitedLocations().isEmpty() || request.getChippingLocationId() != animalRepository.findById(animalId).get().getVisitedLocations().get(0).getLocation().getId());
    }

    public boolean isValideAnimalSearchRequest(Long chipperId,
                                               Long chippingLocationId,
                                               String lifeStatus,
                                               String gender,
                                               Integer from,
                                               Integer size) {
        return (from == null || from >= 0) &&
                (size == null || size > 0) &&
                (chipperId == null || chipperId > 0) &&
                (chippingLocationId == null || chippingLocationId > 0) &&
                (lifeStatus == null || lifeStatus.equals("ALIVE") || lifeStatus.equals("DEAD")) &&
                (gender == null || gender.equals("MALE") || gender.equals("FEMALE") || gender.equals("OTHER"));
    }

    public boolean isValideDeleteRequest(long animalId) {
        List<AnimalVisitedLocations> visitedLocation = animalRepository.findById(animalId).get().getVisitedLocations();

        return (visitedLocation.isEmpty());
    }

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

    public boolean isAllForUpdatingElementsExist(long animalId,
                                                 int chipperId,
                                                 long chippingLocationId) {

        return (animalRepository.existsById(animalId)) &&
                (accountRepository.existsById(chipperId) &&
                        (locationRepository.existsById(chippingLocationId)));

    }

    public boolean isAllForAddingTypeElementsExist(long animalId,
                                                 long typeId) {

        return (animalRepository.existsById(animalId)) &&
                (animalTypeRepository.existsById(typeId));

    }

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


    public Animal convertAnimalDtoRequestToAnimal(AnimalDtoCreateRequest request) {
        Animal animal = new Animal();
        Location location = locationRepository.findById(request.getChippingLocationId()).orElse(null);
        Account account = accountRepository.findById(request.getChipperId()).orElse(null);
        animal.setAccount(account);
        animal.setLocation(location);
        animal.setAnimalTypes(animalTypeRepository.findAllById(request.getAnimalTypes()));
        animal.setChippingDateTime(String.valueOf(Instant.now()));
        animal.setWeight(request.getWeight());
        animal.setLength(request.getLength());
        animal.setHeight(request.getHeight());
        animal.setLifeStatus("ALIVE");
        animal.setGender(request.getGender());
        return animal;
    }

    public boolean isAnimalHasTypes(long animalId) {
        return (animalRepository
                .findById(animalId)
                .get()
                .getAnimalTypes().size() > 1);
    }

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

    public boolean isAnimalExists(long animalId) {
        return animalRepository.existsById(animalId);
    }

    public boolean isAnimalAlive(long animalId) {
        return animalRepository
                .findById(animalId)
                .get()
                .getLifeStatus()
                .equals("ALIVE");
    }

    @Transactional
    public void addAnimalVisitedLocation(long animalId, long locationId) {
        Animal existAnimal = animalRepository.findById(animalId).get();
        Location location = locationRepository.findById(locationId).get();

        existAnimal.addVisitedLocation(location);

        animalRepository.save(existAnimal);
    }

    public boolean isValideAnimalPosition(long animalId, long pointId) {
        Animal animal = animalRepository.findById(animalId).get();
        Location location = locationRepository.findById(pointId).get();

        if (animal.getVisitedLocations().isEmpty() && animal.getLocation().equals(location)) {
            return false;
        }

        return (animal.getVisitedLocations().isEmpty() || !animal.getVisitedLocations().get(animal.getVisitedLocations().size() - 1).getLocation().equals(location));
    }

    public static boolean isWithinRange(String chippingDateTimeString, String startDateTimeString, String endDateTimeString) {
        try {
            // Преобразуем строки в Instant
            Instant chippingDateTime = Instant.parse(chippingDateTimeString);
            Instant startDateTime = startDateTimeString != null ? Instant.parse(startDateTimeString) : null;
            Instant endDateTime = endDateTimeString != null ? Instant.parse(endDateTimeString) : null;

            // Выполняем сравнение
            return (startDateTime == null || chippingDateTime.isAfter(startDateTime)) &&
                    (endDateTime == null || chippingDateTime.isBefore(endDateTime));
        } catch (Exception e) {
            // Обработка ошибок при парсинге
            System.err.println("Invalid date format: " + e.getMessage());
            return false;
        }
    }

    public boolean existAnimalsByChipper(Account account) {
        return animalRepository.existsByAccount(account);
    }

}
