package com.example.demo.controller;

import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
import com.example.demo.model.AnimalType;
import com.example.demo.service.AnimalTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals/types")
public class AnimalTypesController {

    private final AnimalTypeService animalTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public AnimalTypesController(AnimalTypeService animalTypeService, ModelMapper modelMapper) {
        this.animalTypeService = animalTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<?> getAnimalType(@PathVariable long typeId) {

        if (typeId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        AnimalType animalType = animalTypeService.getAnimalTypeById(typeId);

        if (animalType == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        return ResponseEntity
                .ok()
                .body(convertAnimalTypeToAnimalTypeDtoResponse(animalType));
    }

    @PostMapping("")
    public ResponseEntity<?> addAnimalType(@RequestBody AnimalTypeDtoRequest request) {

        if (request.getType() == null || request.getType().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (animalTypeService.isExistsAnimalType(request.getType())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertAnimalTypeToAnimalTypeDtoResponse(
                animalTypeService.saveAnimalType(convertAnimalTypeDtoRequestToAnimalType(request))
                        )
                );
    }

    @PutMapping("/{typeId}")
    public ResponseEntity<?> updateAnimalType(@PathVariable long typeId,
                                                  @RequestBody AnimalTypeDtoRequest request) {

        if (request.getType() == null || request.getType().trim().isEmpty() || typeId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (animalTypeService.getAnimalTypeById(typeId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        if (animalTypeService.isExistsAnimalType(request.getType())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }


        return ResponseEntity
                .ok()
                .body(convertAnimalTypeToAnimalTypeDtoResponse(
                animalTypeService.updateAnimalType(typeId, convertAnimalTypeDtoRequestToAnimalType(request))
                        ));
    }

    @DeleteMapping("/{typeId}")
    public ResponseEntity<?> deleteAnimalType(@PathVariable long typeId) {
        if (typeId <= 0 || animalTypeService.isDependsOnAnimal(animalTypeService.getAnimalTypeById(typeId))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (animalTypeService.getAnimalTypeById(typeId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        animalTypeService.deleteAnimalType(typeId);
        return ResponseEntity.ok("");
    }


    public AnimalTypeDtoResponse convertAnimalTypeToAnimalTypeDtoResponse(AnimalType animalType) {
        return modelMapper.map(animalType, AnimalTypeDtoResponse.class);
    }

    public AnimalType convertAnimalTypeDtoRequestToAnimalType(AnimalTypeDtoRequest animalTypeDtoRequest) {
        return modelMapper.map(animalTypeDtoRequest, AnimalType.class);
    }
}
