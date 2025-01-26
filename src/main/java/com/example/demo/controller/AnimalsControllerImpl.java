package com.example.demo.controller;

import com.example.demo.controllerInterface.AnimalsController;
import com.example.demo.dto.animal.*;
import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalType;
import com.example.demo.model.AnimalVisitedLocations;
import com.example.demo.serviceInterface.AnimalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalsControllerImpl implements AnimalsController {


    private final AnimalService animalServiceImpl;


    @Override
    @GetMapping("/{animalId}")
    public ResponseEntity<AnimalDtoResponse> getAnimalById(@PathVariable @Min(value = 1) long animalId) {
        return ResponseEntity.ok(convertAnimalToAnimalDtoResponse(animalServiceImpl.getAnimalById(animalId)));
    }



    @Override
    @GetMapping("/search")
    public ResponseEntity<List<AnimalDtoResponse>> getAnimalListById(
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime,
            @RequestParam(required = false) @Min(value = 1) Long chipperId,
            @RequestParam(required = false) @Min(value = 1) Long chippingLocationId,
            @RequestParam(required = false) LifeStatus lifeStatus,
            @RequestParam(required = false) Gender gender,
            @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(defaultValue = "10") @Min(value = 1) Integer size) {

        return ResponseEntity.status(HttpStatus.OK).body(animalServiceImpl.searchAnimalList(
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

    @Override
    @PostMapping("")
    public ResponseEntity<AnimalDtoResponse> addAnimal(@RequestBody @Valid AnimalDtoCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertAnimalToAnimalDtoResponse(animalServiceImpl.addAnimal(request)));
    }

    @Override
    @PutMapping("/{animalId}")
    public ResponseEntity<AnimalDtoResponse> updateAnimal(@PathVariable @Min(value = 1) long animalId,
                                          @RequestBody @Valid AnimalDtoUpdateRequest request) {
        return ResponseEntity
                .ok()
                .body(convertAnimalToAnimalDtoResponse(animalServiceImpl.updateAnimal(animalId, request)));
    }

    @Override
    @DeleteMapping("/{animalId}")
    public ResponseEntity<String> deleteAnimal(@PathVariable @Min(value = 1) long animalId) {
        animalServiceImpl.deleteAnimal(animalId);
        return ResponseEntity.ok("");
    }

    @Override
    @PostMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<AnimalDtoResponse> addAnimalType(@PathVariable @Min(value = 1) long animalId,
                                           @PathVariable @Min(value = 1) long typeId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertAnimalToAnimalDtoResponse(animalServiceImpl.addAnimalType(animalId, typeId)));
    }

    @Override
    @PutMapping("/{animalId}/types")
    public ResponseEntity<AnimalDtoResponse> updateAnimalTypes(@PathVariable @Min(value = 1) long animalId,
                                               @RequestBody @Valid AnimalDtoUpdateTypeRequest request) {
        return ResponseEntity
                .ok()
                .body(convertAnimalToAnimalDtoResponse(animalServiceImpl.updateAnimalType(animalId, request)));
    }

    @Override
    @DeleteMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<AnimalDtoResponse> deleteAnimalType(@PathVariable @Min(value = 1) long animalId,
                                              @PathVariable @Min(value = 1) long typeId) {
        return  ResponseEntity
                .ok()
                .body(convertAnimalToAnimalDtoResponse(animalServiceImpl.deleteAnimalType(animalId, typeId)));
    }


    @Override
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

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<String> handleIllegalArgumentException(RequestValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ConflictDataException.class)
    public ResponseEntity<String> handleConflictDataException(ConflictDataException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleConflictDataException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

}
