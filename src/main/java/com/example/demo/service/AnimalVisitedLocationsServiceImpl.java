package com.example.demo.service;

import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.model.Location;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.repository.AnimalVisitedLocationsRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.serviceInterface.AnimalVisitedLocationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnimalVisitedLocationsServiceImpl implements AnimalVisitedLocationsService {

    private final AnimalVisitedLocationsRepository animalVisitedLocationsRepository;

    private final LocationRepository locationRepository;

    private final AnimalRepository animalRepository;

    
    @Override
    public List<AnimalVisitedLocationsDtoResponse> getAnimalVisitedLocations( long animalId,
                                                                   String startDateTime,
                                                                   String endDateTime,
                                                                   int from,
                                                                   int size)
            throws RequestValidationException, ObjectNotFoundException {

        if (!animalRepository.existsById(animalId)) {
            throw new ObjectNotFoundException("");
        }

        List<AnimalVisitedLocations> filtered = animalVisitedLocationsRepository
                .findAnimalsVisitedLocationByParams(
                        animalId,
                        startDateTime,
                        endDateTime
                );

        int toIndex = Math.min(filtered.size(), from + size);
        return filtered.subList(from, toIndex)
                .stream()
                .map(this::convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations)
                .toList();
    }

    @Override
    @Transactional
    public AnimalVisitedLocationsDtoResponse updateAnimalVisitedLocation(long animalId,
                                                              AnimalVisitedLocationsDtoRequest request)
            throws ObjectNotFoundException, RequestValidationException{

        if (!isAllExistForUpdate(animalId, request)) {
            throw new ObjectNotFoundException("");
        }

        if (!isValideForUpdating(request, animalId)) {
            throw new RequestValidationException("");
        }

        var existAnimalLocation = animalVisitedLocationsRepository
                .findById(request.getVisitedLocationPointId())
                .get();
        existAnimalLocation.setLocation(locationRepository.findById(request.getLocationPointId()).get());
        return convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations(
                animalVisitedLocationsRepository.save(existAnimalLocation)
        );
    }

    @Override
    @Transactional
    public void deleteAnimalVisitedLocation(long animalId, long visitedPointId)
            throws RequestValidationException, ObjectNotFoundException {

        if (!isAllExistForDelete(animalId, visitedPointId)) {
            throw new ObjectNotFoundException("");
        }

        var animal = animalRepository.findById(animalId).get();

        animal.removeVisitedLocation(animalVisitedLocationsRepository.findById(visitedPointId).get());

        if (!animal.getVisitedLocations().isEmpty() && animal.getVisitedLocations().get(0).getLocation().equals(animal.getLocation())) {
            animal.removeVisitedLocation(0);
        }
    }

    @Override
    public boolean isValideForUpdating(AnimalVisitedLocationsDtoRequest request, long animalId) {
        var animal = animalRepository.findById(animalId).get();
        var animalVisitedLocations = animalVisitedLocationsRepository.findById(request.getVisitedLocationPointId()).get();

        Location newLocation = locationRepository.findById(request.getLocationPointId()).get();
        Location oldLocation = animalVisitedLocations.getLocation();

        Location firstLocation = animal.getVisitedLocations()
                .stream()
                .sorted(Comparator.comparing(AnimalVisitedLocations::getId))
                .findFirst()
                .get()
                .getLocation();

        // Находим индекс объекта
        int index = animal.getVisitedLocations().indexOf(animalVisitedLocations);

        boolean matchesPrevious = index > 0 &&
                animal.getVisitedLocations().get(index - 1).getLocation().equals(newLocation); // Сравнение с предыдущим
        boolean matchesNext = index < animal.getVisitedLocations().size() - 1 &&
                animal.getVisitedLocations().get(index + 1).getLocation().equals(newLocation); // Сравнение со следующим

        return !(oldLocation.equals(firstLocation) && newLocation.equals(animal.getLocation())) &&
                (!newLocation.equals(oldLocation)) &&
                (!matchesNext) &&
                (!matchesPrevious);
    }

    @Override
    public boolean isAllExistForUpdate(long animalId, AnimalVisitedLocationsDtoRequest request) {
        Animal animal = animalRepository.findById(animalId).orElse(null);
        return (animal != null) &&
                (animalVisitedLocationsRepository.existsById(request.getVisitedLocationPointId())) &&
                (animal.getVisitedLocations()
                        .contains(animalVisitedLocationsRepository
                                .findById(request.getVisitedLocationPointId()).get())) &&
                (locationRepository.existsById(request.getLocationPointId()));
    }

    @Override
    public boolean isAllExistForDelete(long animalId, long visitedPointId) {
        Animal animal = animalRepository.findById(animalId).orElse(null);
        AnimalVisitedLocations visitedLocation = animalVisitedLocationsRepository.findById(visitedPointId).orElse(null);
        return (animal != null) &&
                (visitedLocation != null) &&
                (animal.getVisitedLocations()
                        .contains(visitedLocation));
    }

    @Override
    public AnimalVisitedLocations findByAnimalIdAndLocationId(long animalId, long locationId) {
        return animalVisitedLocationsRepository.findAnimalVisitedLocationsByAnimalIdAndLocationId(animalId, locationId).stream()
                .sorted(Comparator.comparing(AnimalVisitedLocations::getId).reversed())
                .findFirst()
                .get();
    }

    @Override
    public AnimalVisitedLocationsDtoResponse convertAnimalVisitedLocationsDtoResponseToAnimalVisitedLocations(
            AnimalVisitedLocations animalVisitedLocations) {
        AnimalVisitedLocationsDtoResponse animalVisitedLocationsDtoResponse = new AnimalVisitedLocationsDtoResponse();
        animalVisitedLocationsDtoResponse.setId(animalVisitedLocations.getId());
        animalVisitedLocationsDtoResponse.setLocationPointId(animalVisitedLocations.getLocation().getId());
        animalVisitedLocationsDtoResponse.setDateTimeOfVisitLocationPoint(animalVisitedLocations.getDateTimeOfVisitLocationPoint());
        return animalVisitedLocationsDtoResponse;
    }

}
