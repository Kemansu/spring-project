package com.example.demo.controllerInterface;

import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
import com.example.demo.model.AnimalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AnimalTypesController {

    @GetMapping("/{typeId}")
    ResponseEntity<AnimalTypeDtoResponse> getAnimalType(@PathVariable @Min(value = 1) long typeId);

    @PostMapping("")
    ResponseEntity<AnimalTypeDtoResponse> addAnimalType(@RequestBody @Valid AnimalTypeDtoRequest request);

    @PutMapping("/{typeId}")
    ResponseEntity<AnimalTypeDtoResponse> updateAnimalType(@PathVariable @Min(value = 1) long typeId,
                                       @RequestBody @Valid AnimalTypeDtoRequest request);

    @DeleteMapping("/{typeId}")
    ResponseEntity<String> deleteAnimalType(@PathVariable @Min(value = 1) long typeId);

    AnimalTypeDtoResponse convertAnimalTypeToAnimalTypeDtoResponse(AnimalType animalType);
}
