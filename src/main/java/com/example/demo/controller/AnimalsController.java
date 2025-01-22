package com.example.demo.controller;

import com.example.demo.dto.animal.*;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalType;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.service.AnimalService;
import com.example.demo.service.AnimalTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.util.HashSet;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/animals")
public class AnimalsController {

    private final ModelMapper modelMapper;

    private final AnimalService animalService;

    private final AnimalTypeService animalTypeService;

    @Autowired
    public AnimalsController(ModelMapper modelMapper, AnimalService animalService, AnimalTypeService animalTypeService) {
        this.modelMapper = modelMapper;
        this.animalService = animalService;
        this.animalTypeService = animalTypeService;
    }

    @GetMapping("/{animalId}")
    public ResponseEntity<?> getAnimalById(@PathVariable long animalId) {
        if (animalId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (animalService.getAnimalById(animalId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }


        return ResponseEntity.ok().body(convertAnimalToAnimalDtoResponse(animalService.getAnimalById(animalId)));
    }



    @GetMapping("/search")
    public ResponseEntity<?> getAnimalListById(@RequestParam(required = false) String startDateTime,
                                                     @RequestParam(required = false) String endDateTime,
                                                     @RequestParam(required = false) Long chipperId,
                                                     @RequestParam(required = false) Long chippingLocationId,
                                                     @RequestParam(required = false) String lifeStatus,
                                                     @RequestParam(required = false) String gender,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {

        if (!animalService.isValideAnimalSearchRequest(
                chipperId,
                chippingLocationId,
                lifeStatus,
                gender,
                from,
                size)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }



        return ResponseEntity.status(HttpStatus.OK).body(animalService.searchAnimalList(
                        startDateTime,
                        endDateTime,
                        chipperId,
                        chippingLocationId,
                        lifeStatus,
                        gender,
                        from,
                        size)
                .stream()
                .map(this::convertAnimalToAnimalDtoResponse)
                .collect(Collectors.toList()));
    }

    @PostMapping("")
    public ResponseEntity<?> addAnimal(@RequestBody AnimalDtoCreateRequest request) throws ParseException {
        if (!animalService.isValideAnimalCreateRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (!animalService.isAllForCreatingElementsExist(request.getAnimalTypes(),
                request.getChipperId(),
                request.getChippingLocationId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (request.getAnimalTypes().size() > new HashSet<>(request.getAnimalTypes()).size()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(convertAnimalToAnimalDtoResponse(animalService.addAnimal(request)));
    }

    @PutMapping("/{animalId}")
    public ResponseEntity<?> updateAnimal(@PathVariable long animalId,
                                          @RequestBody AnimalDtoUpdateRequest request) {

        if (animalId <= 0 || (animalService.isAnimalExists(animalId) && !animalService.isValideAnimalUpdateRequest(request, animalId))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (!animalService.isAllForUpdatingElementsExist(animalId, request.getChipperId(), request.getChippingLocationId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }


        return ResponseEntity
                .ok()
                .body(convertAnimalToAnimalDtoResponse(animalService.updateAnimal(animalId, request)));
    }

    @DeleteMapping("/{animalId}")
    public ResponseEntity<?> deleteAnimal(@PathVariable long animalId) {
        if (animalId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }
        if (animalService.getAnimalById(animalId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (!animalService.isValideDeleteRequest(animalId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }
        animalService.deleteAnimal(animalId);
        return ResponseEntity.ok("");
    }

    @PostMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<?> addAnimalType(@PathVariable long animalId,
                                           @PathVariable long typeId) {

        if (animalId <= 0 || typeId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (!animalService.isAllForAddingTypeElementsExist(animalId, typeId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (animalService.isTypeIdInAnimal(animalId, typeId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertAnimalToAnimalDtoResponse(animalService.addAnimalType(animalId, typeId)));
    }

    @PutMapping("/{animalId}/types")
    public ResponseEntity<?> updateAnimalTypes(@PathVariable long animalId,
                                               @RequestBody AnimalDtoUpdateTypeRequest request) {
        System.out.println(request.getOldTypeId());
        if (animalId <= 0 || request.getOldTypeId() <= 0 || request.getNewTypeId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (!animalService.isAllForUpdateTypeExist(animalId, request.getOldTypeId(), request.getNewTypeId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (animalService.isAlreadyInAnimal(animalId, request.getNewTypeId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        return ResponseEntity
                .ok()
                .body(convertAnimalToAnimalDtoResponse(animalService.updateAnimalType(animalId, request)));
    }

    @DeleteMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<?> deleteAnimalType(@PathVariable long animalId,
                                              @PathVariable long typeId) {

        if (animalId > 0 && typeId > 0 && !animalService.isAllForDeletingExist(animalId, typeId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (animalId <= 0 || typeId <= 0 || !animalService.isAnimalHasTypes(animalId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }


        return  ResponseEntity
                .ok()
                .body(convertAnimalToAnimalDtoResponse(animalService.deleteAnimalType(animalId, typeId)));
    }


    public AnimalDtoResponse convertAnimalToAnimalDtoResponse(Animal animal) {
        AnimalDtoResponse response = new AnimalDtoResponse();
        response.setId(animal.getId());
        response.setChipperId(animal.getAccount().getId());
        response.setChippingLocationId(animal.getLocation().getId());
        response.setAnimalTypes(animal.getAnimalTypes().stream()
                .map(AnimalType::getId)
                .toList());
        response.setVisitedLocations(animal.getVisitedLocations().stream()
                .map(AnimalVisitedLocations::getId)
                .toList());
        response.setWeight(animal.getWeight());
        response.setLength(animal.getLength());
        response.setHeight(animal.getHeight());
        response.setGender(animal.getGender());
        response.setLifeStatus(animal.getLifeStatus());
        response.setChippingDateTime(animal.getChippingDateTime());
        response.setDeathDateTime(animal.getDeathDateTime());
        return response;
    }

}
