package com.example.demo.controller.impl;

import com.example.demo.controller.AnimalsController;
import com.example.demo.dto.animal.*;
import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.mapper.AnimalMapper;
import com.example.demo.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    @GetMapping("/{animalId}")
    @Operation(summary = "Получить животного", description = "Возвращает животного по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запросы"),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Животное с ID не найдено")
    })
    public ResponseEntity<AnimalDtoResponse> getAnimalById(@PathVariable @Min(value = 1) long animalId) {
        return ResponseEntity.ok(animalMapper.toAnimalDtoResponse(animalServiceImpl.getAnimalById(animalId)));
    }



    @Override
    @GetMapping("/search")
    @Operation(summary = "Получить животных", description = "Возвращает список животных по его фильтрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запросы"),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные")
    })
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
                .map(animalMapper::toAnimalDtoResponse)
                .collect(Collectors.toList()));
    }

    @Override
    @PostMapping
    @Operation(summary = "Добавление животного", description = "Возвращает добавленное животного")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запросы"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "404",
                    description = "Тип животного не найден, Аккаунт с chipperId не найден, " +
                            "Точка локации с chippingLocationId не найдена"),
            @ApiResponse(responseCode = "409", description = "Массив animalTypes содержит дубликаты")
    })
    public ResponseEntity<AnimalDtoResponse> addAnimal(@RequestBody @Valid AnimalDtoCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(animalMapper.toAnimalDtoResponse(animalServiceImpl.addAnimal(request)));
    }

    @Override
    @PutMapping("/{animalId}")
    @Operation(summary = "Обновление животного", description = "Обновление животного по введенном параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запросы, " +
                            "животное мертво, " +
                            "Новая точка чипирования совпадает с первой посещенной точкой локации"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено, " +
                            "Аккаунт с chipperId не найден, " +
                            "Точка локации с chippingLocationId не найдена")
    })
    public ResponseEntity<AnimalDtoResponse> updateAnimal(@PathVariable @Min(value = 1) long animalId,
                                          @RequestBody @Valid AnimalDtoUpdateRequest request) {
        return ResponseEntity
                .ok()
                .body(animalMapper.toAnimalDtoResponse(animalServiceImpl.updateAnimal(animalId, request)));
    }

    @Override
    @DeleteMapping("/{animalId}")
    @Operation(summary = "Удаление животного", description = "Удаление животного по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400",
                    description = "Животное покинуло локацию чипирования, при этом" +
                            "есть другие посещенные точки"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено")
    })
    public ResponseEntity<Void> deleteAnimal(@PathVariable @Min(value = 1) long animalId) {
        animalServiceImpl.deleteAnimal(animalId);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/{animalId}/types/{typeId}")
    @Operation(summary = "Добавление типа животному", description = "Добавляет тип животному по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запросы"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено, Тип животного с typeId не найден"),
            @ApiResponse(responseCode = "409", description = "Тип животного с typeId уже есть у животного с animalId")
    })
    public ResponseEntity<AnimalDtoResponse> addAnimalType(@PathVariable @Min(value = 1) long animalId,
                                           @PathVariable @Min(value = 1) long typeId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(animalMapper.toAnimalDtoResponse(animalServiceImpl.addAnimalType(animalId, typeId)));
    }

    @Override
    @PutMapping("/{animalId}/types")
    @Operation(summary = "Обновление типа животному", description = "Обновяет тип животному по введенным данным")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запросы"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено, Тип животного с oldTypeId не найден" +
                            "Тип животного с newTypeId не найден" +
                            "Типа животного с oldTypeId нет у животного с animalId"),
            @ApiResponse(responseCode = "409",
                    description = "Тип животного с newTypeId уже есть у животного с animalId" +
                    "Животное с animalId уже имеет типы с oldTypeId и newTypeId")
    })
    public ResponseEntity<AnimalDtoResponse> updateAnimalTypes(@PathVariable @Min(value = 1) long animalId,
                                               @RequestBody @Valid AnimalDtoUpdateTypeRequest request) {
        return ResponseEntity
                .ok()
                .body(animalMapper.toAnimalDtoResponse(animalServiceImpl.updateAnimalType(animalId, request)));
    }

    @Override
    @DeleteMapping("/{animalId}/types/{typeId}")
    @Operation(summary = "Удаление типа животному", description = "Удаляет тип животному по введенным данным")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запросы, У животного только один тип и это тип с typeId"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено" +
                            "Тип животного с typeId не найден" +
                            "У животного с animalId нет типа с typeId")
    })
    public ResponseEntity<AnimalDtoResponse> deleteAnimalType(@PathVariable @Min(value = 1) long animalId,
                                              @PathVariable @Min(value = 1) long typeId) {
        return  ResponseEntity
                .ok()
                .body(animalMapper.toAnimalDtoResponse((animalServiceImpl.deleteAnimalType(animalId, typeId))));
    }


}
