package com.example.demo.controller;

import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.dto.animal.*;
import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.Animal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface AnimalsController {

    @Operation(summary = "Получить животного", description = "Возвращает животного по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = AnimalDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запросы",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Животное с ID не найдено",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @GetMapping("/{animalId}")
    ResponseEntity<AnimalDtoResponse> getAnimalById(@PathVariable @Min(value = 1) Long animalId);

    @Operation(summary = "Получить количество животных", description = "Возвраащет текущее количество живых животных")
    @GetMapping("/getNumberOfAnimals")
    ResponseEntity<Integer> getNumberOfAnimals();

    @Operation(summary = "Получить животных", description = "Возвращает список животных по его фильтрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = AnimalDtoResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запросы",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class)))
    })
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

    @Operation(summary = "Добавление животного", description = "Возвращает добавленное животного")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = AnimalDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запросы",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404",
                    description = "Тип животного не найден, Аккаунт с chipperId не найден, " +
                            "Точка локации с chippingLocationId не найдена",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class))),
            @ApiResponse(responseCode = "409", description = "Массив animalTypes содержит дубликаты",
                    content = @Content(schema = @Schema(implementation = ConflictDataException.class)))
    })
    @PostMapping
    ResponseEntity<AnimalDtoResponse> addAnimal(@RequestBody @Valid AnimalDtoCreateRequest request);



    @Operation(summary = "Обновление животного", description = "Обновление животного по введенном параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = AnimalDtoResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запросы, " +
                            "животное мертво, " +
                            "Новая точка чипирования совпадает с первой посещенной точкой локации",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено, " +
                            "Аккаунт с chipperId не найден, " +
                            "Точка локации с chippingLocationId не найдена",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @PutMapping("/{animalId}")
    ResponseEntity<AnimalDtoResponse> updateAnimal(@PathVariable @Min(value = 1) Long animalId,
                                   @RequestBody @Valid AnimalDtoUpdateRequest request);

    @Operation(summary = "Удаление животного", description = "Удаление животного по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400",
                    description = "Животное покинуло локацию чипирования, при этом" +
                            "есть другие посещенные точки",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @DeleteMapping("/{animalId}")
    ResponseEntity<Void> deleteAnimal(@PathVariable @Min(value = 1) Long animalId);


    @Operation(summary = "Добавление типа животному", description = "Добавляет тип животному по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = AnimalDtoResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запросы",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено, Тип животного с typeId не найден",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class))),
            @ApiResponse(responseCode = "409", description = "Тип животного с typeId уже есть у животного с animalId",
                    content = @Content(schema = @Schema(implementation = ConflictDataException.class)))
    })
    @PostMapping("/{animalId}/types/{typeId}")
    ResponseEntity<AnimalDtoResponse> addAnimalType(@PathVariable @Min(value = 1) Long animalId,
                                    @PathVariable @Min(value = 1) Long typeId);


    @Operation(summary = "Обновление типа животному", description = "Обновяет тип животному по введенным данным")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = AnimalDtoResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запросы",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено, Тип животного с oldTypeId не найден" +
                            "Тип животного с newTypeId не найден" +
                            "Типа животного с oldTypeId нет у животного с animalId",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class))),
            @ApiResponse(responseCode = "409",
                    description = "Тип животного с newTypeId уже есть у животного с animalId" +
                            "Животное с animalId уже имеет типы с oldTypeId и newTypeId",
                    content = @Content(schema = @Schema(implementation = ConflictDataException.class)))
    })
    @PutMapping("/{animalId}/types")
    ResponseEntity<AnimalDtoResponse> updateAnimalTypes(@PathVariable @Min(value = 1) Long animalId,
                                        @RequestBody @Valid AnimalDtoUpdateTypeRequest request);

    @Operation(summary = "Удаление типа животному", description = "Удаляет тип животному по введенным данным")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = AnimalDtoResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запросы, У животного только один тип и это тип с typeId",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404",
                    description = "Животное с animalId не найдено" +
                            "Тип животного с typeId не найден" +
                            "У животного с animalId нет типа с typeId",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @DeleteMapping("/{animalId}/types/{typeId}")
    ResponseEntity<AnimalDtoResponse> deleteAnimalType(@PathVariable @Min(value = 1) Long animalId,
                                       @PathVariable @Min(value = 1) Long typeId);
}
