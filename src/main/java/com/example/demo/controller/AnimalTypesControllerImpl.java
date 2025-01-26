package com.example.demo.controller;

import com.example.demo.controllerInterface.AnimalTypesController;
import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.AnimalType;
import com.example.demo.serviceInterface.AnimalTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals/types")
@RequiredArgsConstructor
public class AnimalTypesControllerImpl implements AnimalTypesController {

    private final AnimalTypeService animalTypeServiceImpl;
    private final ModelMapper modelMapper;


    @Override
    @GetMapping("/{typeId}")
    public ResponseEntity<AnimalTypeDtoResponse> getAnimalType(@PathVariable @Min(value = 1) long typeId) {
        return ResponseEntity
                .ok()
                .body(convertAnimalTypeToAnimalTypeDtoResponse(animalTypeServiceImpl.getAnimalTypeById(typeId)));
    }

    @Override
    @PostMapping("")
    public ResponseEntity<AnimalTypeDtoResponse> addAnimalType(@RequestBody @Valid AnimalTypeDtoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertAnimalTypeToAnimalTypeDtoResponse(
                animalTypeServiceImpl.saveAnimalType(request)
                        )
                );
    }

    @Override
    @PutMapping("/{typeId}")
    public ResponseEntity<AnimalTypeDtoResponse> updateAnimalType(@PathVariable @Min(value = 1)long typeId,
                                                  @RequestBody @Valid AnimalTypeDtoRequest request) {
        return ResponseEntity
                .ok()
                .body(convertAnimalTypeToAnimalTypeDtoResponse(
                animalTypeServiceImpl.updateAnimalType(typeId, request)
                        ));
    }

    @Override
    @DeleteMapping("/{typeId}")
    public ResponseEntity<String> deleteAnimalType(@PathVariable @Min(value = 1) long typeId) {
        animalTypeServiceImpl.deleteAnimalType(typeId);
        return ResponseEntity.ok("");
    }


    @Override
    public AnimalTypeDtoResponse convertAnimalTypeToAnimalTypeDtoResponse(AnimalType animalType) {
        return modelMapper.map(animalType, AnimalTypeDtoResponse.class);
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
