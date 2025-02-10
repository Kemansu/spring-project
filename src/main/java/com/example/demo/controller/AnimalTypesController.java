package com.example.demo.controller;

import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
import com.example.demo.model.AnimalType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AnimalTypesController {

    @Operation(summary = "Получить тип животного", description = "Возвращает тип животного по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Тип животного с таким ID не найден")
    })
    @GetMapping("/{typeId}")
    ResponseEntity<AnimalTypeDtoResponse> getAnimalType(@PathVariable @Min(value = 1) Long typeId);

    @Operation(summary = "добавить тип животного", description = "Добавление типа животного по введенным параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Неверные авторизационные данные"),
            @ApiResponse(responseCode = "409", description = "Тип животного с таким type уже существует")
    })
    @PostMapping
    ResponseEntity<AnimalTypeDtoResponse> addAnimalType(@RequestBody @Valid AnimalTypeDtoRequest request);


    @Operation(summary = "обновить тип животного", description = "обновляет тип животного по введенным параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Тип животного с таким ID не найден"),
            @ApiResponse(responseCode = "409", description = "Тип животного с таким type уже существует")
    })
    @PutMapping("/{typeId}")
    ResponseEntity<AnimalTypeDtoResponse> updateAnimalType(@PathVariable @Min(value = 1) Long typeId,
                                       @RequestBody @Valid AnimalTypeDtoRequest request);


    @Operation(summary = "удалить тип животного", description = "удаление типа животного по введенным параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запроса, Есть животные с типом с Id"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Тип животного с таким ID не найден"),
    })
    @DeleteMapping("/{typeId}")
    ResponseEntity<Void> deleteAnimalType(@PathVariable @Min(value = 1) Long typeId);
}
