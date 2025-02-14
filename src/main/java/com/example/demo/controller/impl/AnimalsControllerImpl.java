package com.example.demo.controller.impl;

import com.example.demo.controller.AnimalsController;
import com.example.demo.dto.animal.*;
import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.mapper.AnimalMapper;
import com.example.demo.model.Animal;
import com.example.demo.service.AnimalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Animals", description = "Управление данными животных")
@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalsControllerImpl implements AnimalsController {


    private final AnimalService animalServiceImpl;
    
    private final AnimalMapper animalMapper;


    @Override
    public ResponseEntity<AnimalDtoResponse> getAnimalById(@PathVariable Long animalId) {
        return ResponseEntity.ok(animalMapper.toAnimalDtoResponse(animalServiceImpl.getAnimalById(animalId)));
    }

    @Override
    public ResponseEntity<Integer> getNumberOfAnimals() {
        return ResponseEntity.ok(animalServiceImpl.getNumberOfAnimals());
    }



    @Override
    public ResponseEntity<List<AnimalDtoResponse>> getAnimalListById(
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime,
            @RequestParam(required = false) Long chipperId,
            @RequestParam(required = false) Long chippingLocationId,
            @RequestParam(required = false) LifeStatus lifeStatus,
            @RequestParam(required = false) Gender gender,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        AnimalDtoSearchRequest dtoSearchRequest = AnimalDtoSearchRequest.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .chipperId(chipperId)
                .chippingLocationId(chippingLocationId)
                .lifeStatus(lifeStatus)
                .gender(gender)
                .from(from)
                .size(size)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(animalServiceImpl.searchAnimalList(dtoSearchRequest));
    }

    @Override
    public ResponseEntity<AnimalDtoResponse> addAnimal(@RequestBody AnimalDtoCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(animalServiceImpl.addAnimal(request));
    }

    @Override
    public ResponseEntity<AnimalDtoResponse> updateAnimal(@PathVariable Long animalId,
                                          @RequestBody AnimalDtoUpdateRequest request) {
        return ResponseEntity
                .ok()
                .body(animalServiceImpl.updateAnimal(animalId, request));
    }

    @Override
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long animalId) {
        animalServiceImpl.deleteAnimal(animalId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AnimalDtoResponse> addAnimalType(@PathVariable Long animalId,
                                           @PathVariable Long typeId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(animalMapper.toAnimalDtoResponse(animalServiceImpl.addAnimalType(animalId, typeId)));
    }

    @Override
    public ResponseEntity<AnimalDtoResponse> updateAnimalTypes(@PathVariable Long animalId,
                                               @RequestBody AnimalDtoUpdateTypeRequest request) {
        return ResponseEntity
                .ok()
                .body(animalServiceImpl.updateAnimalType(animalId, request));
    }

    @Override
    public ResponseEntity<AnimalDtoResponse> deleteAnimalType(@PathVariable Long animalId,
                                              @PathVariable Long typeId) {
        return  ResponseEntity
                .ok()
                .body((animalServiceImpl.deleteAnimalType(animalId, typeId)));
    }


}
