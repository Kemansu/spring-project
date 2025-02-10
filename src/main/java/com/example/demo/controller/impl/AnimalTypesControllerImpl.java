package com.example.demo.controller.impl;

import com.example.demo.controller.AnimalTypesController;
import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
import com.example.demo.mapper.AnimalTypesMapper;
import com.example.demo.service.AnimalTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Типы животных" ,description = "Управление типами животных")
@RestController
@RequestMapping("/animals/types")
@RequiredArgsConstructor
public class AnimalTypesControllerImpl implements AnimalTypesController {

    private final AnimalTypeService animalTypeServiceImpl;


    @Override
    public ResponseEntity<AnimalTypeDtoResponse> getAnimalType(@PathVariable Long typeId) {
        return ResponseEntity
                .ok()
                .body(animalTypeServiceImpl.getAnimalTypeById(typeId));
    }

    @Override
    public ResponseEntity<AnimalTypeDtoResponse> addAnimalType(@RequestBody AnimalTypeDtoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(animalTypeServiceImpl.saveAnimalType(request));
    }

    @Override
    public ResponseEntity<AnimalTypeDtoResponse> updateAnimalType(@PathVariable Long typeId,
                                                  @RequestBody AnimalTypeDtoRequest request) {
        return ResponseEntity
                .ok()
                .body(animalTypeServiceImpl.updateAnimalType(typeId, request));
    }

    @Override
    public ResponseEntity<Void> deleteAnimalType(@PathVariable Long typeId) {
        animalTypeServiceImpl.deleteAnimalType(typeId);
        return ResponseEntity.ok().build();
    }


}
