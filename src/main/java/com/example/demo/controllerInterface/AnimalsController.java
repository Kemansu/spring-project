package com.example.demo.controllerInterface;

import com.example.demo.dto.animal.*;
import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.model.Animal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface AnimalsController {

    @GetMapping("/{animalId}")
    ResponseEntity<AnimalDtoResponse> getAnimalById(@PathVariable @Min(value = 1) long animalId);

    @GetMapping("/search")
    ResponseEntity<List<AnimalDtoResponse>> getAnimalListById(
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime,
            @RequestParam(required = false) @Min(value = 1) Long chipperId,
            @RequestParam(required = false) @Min(value = 1) Long chippingLocationId,
            @RequestParam(required = false) LifeStatus lifeStatus,
            @RequestParam(required = false) Gender gender,
            @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(defaultValue = "10") @Min(value = 1) Integer size);

    @PostMapping("")
    ResponseEntity<AnimalDtoResponse> addAnimal(@RequestBody @Valid AnimalDtoCreateRequest request);

    @PutMapping("/{animalId}")
    ResponseEntity<AnimalDtoResponse> updateAnimal(@PathVariable @Min(value = 1) long animalId,
                                   @RequestBody @Valid AnimalDtoUpdateRequest request);

    @DeleteMapping("/{animalId}")
    ResponseEntity<String> deleteAnimal(@PathVariable @Min(value = 1) long animalId);

    @PostMapping("/{animalId}/types/{typeId}")
    ResponseEntity<AnimalDtoResponse> addAnimalType(@PathVariable @Min(value = 1) long animalId,
                                    @PathVariable @Min(value = 1) long typeId);

    @PutMapping("/{animalId}/types")
    ResponseEntity<AnimalDtoResponse> updateAnimalTypes(@PathVariable @Min(value = 1) long animalId,
                                        @RequestBody @Valid AnimalDtoUpdateTypeRequest request);

    @DeleteMapping("/{animalId}/types/{typeId}")
    ResponseEntity<AnimalDtoResponse> deleteAnimalType(@PathVariable @Min(value = 1) long animalId,
                                       @PathVariable @Min(value = 1) long typeId);

    AnimalDtoResponse convertAnimalToAnimalDtoResponse(Animal animal);
}
