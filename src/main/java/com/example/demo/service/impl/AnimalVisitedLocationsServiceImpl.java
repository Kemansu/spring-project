package com.example.demo.service.impl;

import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoSearchRequest;
import com.example.demo.enums.LifeStatus;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.mapper.AnimalVisitedLocationsMapper;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.model.Location;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.repository.AnimalVisitedLocationsRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.service.AnimalVisitedLocationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnimalVisitedLocationsServiceImpl implements AnimalVisitedLocationsService {

    private final AnimalVisitedLocationsRepository animalVisitedLocationsRepository;

    private final LocationRepository locationRepository;

    private final AnimalRepository animalRepository;

    private final AnimalVisitedLocationsMapper visitedLocationsMapper;

    
    @Override
    public List<AnimalVisitedLocationsDtoResponse> getAnimalVisitedLocations(
            AnimalVisitedLocationsDtoSearchRequest request) {
        boolean isAnimalExist = animalRepository.existsById(request.getAnimalId());

        if (!isAnimalExist) {
            throw new ObjectNotFoundException("");
        }

        List<AnimalVisitedLocations> filtered = animalVisitedLocationsRepository
                .findAnimalsVisitedLocationByParams(
                        request.getAnimalId(),
                        request.getStartDateTime(),
                        request.getEndDateTime()
                );

        int toIndex = Math.min(filtered.size(), request.getFrom() + request.getSize());

        return filtered.subList(request.getFrom(), toIndex)
                .stream()
                .map(visitedLocationsMapper::toAnimalVisitedLocationsDtoResponse)
                .toList();
    }

    @Override
    @Transactional
    public AnimalVisitedLocationsDtoResponse updateAnimalVisitedLocation(Long animalId,
                                                              AnimalVisitedLocationsDtoRequest request) {

        var animal = animalRepository.findById(animalId).orElseThrow(() -> new ObjectNotFoundException(""));

        var existAnimalLocation = animalVisitedLocationsRepository
                .findById(request.getVisitedLocationPointId())
                .orElseThrow(() -> new ObjectNotFoundException(""));

        var newLocation = locationRepository
                .findById(request.getLocationPointId())
                .orElseThrow(() -> new ObjectNotFoundException(""));

        boolean isAnimalContainsLocation = animal.getVisitedLocations()
                .contains(existAnimalLocation);


        if (!isAnimalContainsLocation) {
            throw new ObjectNotFoundException("");
        }

        if (!isValideForUpdating(animal, existAnimalLocation, newLocation)) {
            throw new RequestValidationException("");
        }


        existAnimalLocation.setLocation(newLocation);
        return visitedLocationsMapper.toAnimalVisitedLocationsDtoResponse(
                animalVisitedLocationsRepository.save(existAnimalLocation)
        );
    }

    @Override
    @Transactional
    public void deleteAnimalVisitedLocation(Long animalId, Long visitedPointId) {

        var animal = animalRepository.findById(animalId).orElseThrow(() -> new ObjectNotFoundException(""));

        var existAnimalLocation = animalVisitedLocationsRepository
                .findById(visitedPointId)
                .orElseThrow(() -> new ObjectNotFoundException(""));

        boolean isAnimalContainsLocation = animal.getVisitedLocations()
                .contains(existAnimalLocation);

        if (!isAnimalContainsLocation) {
            throw new ObjectNotFoundException("");
        }

        animal.removeVisitedLocation(existAnimalLocation);

        boolean isFirstLocationNotChipping = !animal.getVisitedLocations().isEmpty() &&
                animal.getVisitedLocations().get(0).getLocation().equals(animal.getLocation());

        if (isFirstLocationNotChipping) {
            animal.removeVisitedLocation(0);
        }
    }

    private boolean isValideForUpdating(Animal animal,
                                        AnimalVisitedLocations animalVisitedLocations,
                                        Location newLocation) {
        Location oldLocation = animalVisitedLocations.getLocation();

        Location firstLocation = animal.getVisitedLocations()
                .stream()
                .min(Comparator.comparing(AnimalVisitedLocations::getId))
                .orElseThrow(() -> new ObjectNotFoundException(""))
                .getLocation();

        // Находим индекс объекта
        int index = animal.getVisitedLocations().indexOf(animalVisitedLocations);

        boolean matchesPrevious = index > 0 &&
                animal.getVisitedLocations().get(index - 1).getLocation().equals(newLocation); // Сравнение с предыдущим

        boolean matchesNext = index < animal.getVisitedLocations().size() - 1 &&
                animal.getVisitedLocations().get(index + 1).getLocation().equals(newLocation); // Сравнение со следующим

        boolean isReplacingFirstLocationOnChipping = oldLocation.equals(firstLocation) &&
                newLocation.equals(animal.getLocation());

        return (!isReplacingFirstLocationOnChipping) &&
                (!newLocation.equals(oldLocation)) &&
                (!matchesNext) &&
                (!matchesPrevious);
    }

    @Override
    @Transactional
    public AnimalVisitedLocationsDtoResponse addAnimalVisitedLocation(Long animalId, Long locationId) {


        if (!isAnimalAlive(animalId) || !isValideAnimalPosition(animalId, locationId)) {
            throw new RequestValidationException("");
        }

        Animal existAnimal = animalRepository.findById(animalId).orElseThrow(() -> new ObjectNotFoundException(""));
        Location location = locationRepository.findById(locationId).orElseThrow(() -> new ObjectNotFoundException(""));

        existAnimal.addVisitedLocation(location);

        var animalVisitedLocations = animalRepository.save(existAnimal).getVisitedLocations()
                .stream()
                .max(Comparator.comparing(AnimalVisitedLocations::getId))
                .orElseThrow(() -> new ObjectNotFoundException(""));

        return visitedLocationsMapper.toAnimalVisitedLocationsDtoResponse(animalVisitedLocations);
    }

    private boolean isAnimalAlive(Long animalId) {
        return LifeStatus.ALIVE.equals(animalRepository
                .findById(animalId)
                .orElseThrow(() -> new ObjectNotFoundException(""))
                .getLifeStatus());
    }

    private boolean isValideAnimalPosition(Long animalId, Long pointId) {
        Animal animal = animalRepository.findById(animalId).orElseThrow(() -> new ObjectNotFoundException(""));
        Location location = locationRepository.findById(pointId).orElseThrow(() -> new ObjectNotFoundException(""));


        boolean isAddingChippingLocation = animal.getVisitedLocations().isEmpty() &&
                animal.getLocation().equals(location);

        if (isAddingChippingLocation) {
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
