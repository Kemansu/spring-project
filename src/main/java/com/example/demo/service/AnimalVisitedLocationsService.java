package com.example.demo.service;

import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.model.Location;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.repository.AnimalVisitedLocationsRepository;
import com.example.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnimalVisitedLocationsService {

    private final AnimalVisitedLocationsRepository animalVisitedLocationsRepository;

    private final LocationRepository locationRepository;

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalVisitedLocationsService(AnimalVisitedLocationsRepository animalVisitedLocationsRepository, LocationRepository locationRepository, AnimalRepository animalRepository) {
        this.animalVisitedLocationsRepository = animalVisitedLocationsRepository;
        this.locationRepository = locationRepository;
        this.animalRepository = animalRepository;
    }
    
    public List<AnimalVisitedLocations> getAnimalVisitedLocations( long animalId,
                                                                   String startDateTime,
                                                                   String endDateTime,
                                                                   int from,
                                                                   int size) {
        List<AnimalVisitedLocations> filtered = animalVisitedLocationsRepository
                .findAllByAnimalId(animalId)
                .stream()
                .filter(location ->
                (startDateTime == null || endDateTime == null || isWithinRange(location.getDateTimeOfVisitLocationPoint(), startDateTime, endDateTime)))
                .sorted(Comparator.comparing(AnimalVisitedLocations::getDateTimeOfVisitLocationPoint))
                .toList();

        int toIndex = Math.min(filtered.size(), from + size);
        return filtered.subList(from, toIndex);
    }

    @Transactional
    public AnimalVisitedLocations updateAnimalVisitedLocation(long animalId,
                                                              AnimalVisitedLocationsDtoRequest request) {

        AnimalVisitedLocations existAnimalLocation = animalVisitedLocationsRepository
                .findById(request.getVisitedLocationPointId())
                .get();
        existAnimalLocation.setLocation(locationRepository.findById(request.getLocationPointId()).get());
        return animalVisitedLocationsRepository.save(existAnimalLocation);
    }

    @Transactional
    public void deleteAnimalVisitedLocation(long animalId, long visitedPointId) {
        Animal animal = animalRepository.findById(animalId).get();

        animal.removeVisitedLocation(animalVisitedLocationsRepository.findById(visitedPointId).get());

        if (!animal.getVisitedLocations().isEmpty() && animal.getVisitedLocations().get(0).getLocation().equals(animal.getLocation())) {
            animal.removeVisitedLocation(0);
        }
    }

    public boolean isValideForUpdating(AnimalVisitedLocationsDtoRequest request, long animalId) {
        Animal animal = animalRepository.findById(animalId).get();
        AnimalVisitedLocations animalVisitedLocations = animalVisitedLocationsRepository.findById(request.getVisitedLocationPointId()).get();

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

        boolean matchesPrevious = index > 0 && animal.getVisitedLocations().get(index - 1).getLocation().equals(newLocation); // Сравнение с предыдущим
        boolean matchesNext = index < animal.getVisitedLocations().size() - 1 && animal.getVisitedLocations().get(index + 1).getLocation().equals(newLocation); // Сравнение со следующим

        return !(oldLocation.equals(firstLocation) && newLocation.equals(animal.getLocation())) &&
                (!newLocation.equals(oldLocation)) &&
                (!matchesNext) &&
                (!matchesPrevious);
    }

    public boolean isAllExistForUpdate(long animalId, AnimalVisitedLocationsDtoRequest request) {
        Animal animal = animalRepository.findById(animalId).orElse(null);
        return (animal != null) &&
                (animalVisitedLocationsRepository.existsById(request.getVisitedLocationPointId())) &&
                (animal.getVisitedLocations()
                        .contains(animalVisitedLocationsRepository
                                .findById(request.getVisitedLocationPointId()).get())) &&
                (locationRepository.existsById(request.getLocationPointId()));
    }

    public boolean isAllExistForDelete(long animalId, long visitedPointId) {
        Animal animal = animalRepository.findById(animalId).orElse(null);
        AnimalVisitedLocations visitedLocation = animalVisitedLocationsRepository.findById(visitedPointId).orElse(null);
        return (animal != null) &&
                (visitedLocation != null) &&
                (animal.getVisitedLocations()
                        .contains(visitedLocation));
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
    public AnimalVisitedLocations findByAnimalIdAndLocationId(long animalId, long locationId) {
        return animalVisitedLocationsRepository.findAnimalVisitedLocationsByAnimalIdAndLocationId(animalId, locationId).stream().sorted(Comparator.comparing(AnimalVisitedLocations::getId).reversed()).findFirst().get();
    }

}
