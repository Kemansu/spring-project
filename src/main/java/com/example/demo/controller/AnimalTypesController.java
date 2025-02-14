package com.example.demo.controller;

import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.AnimalType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

public interface AnimalTypesController {

    @Operation(summary = "Получить тип животного", description = "Возвращает тип животного по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = AnimalTypeDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Тип животного с таким ID не найден",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @GetMapping("/{typeId}")
    ResponseEntity<AnimalTypeDtoResponse> getAnimalType(@PathVariable @Min(value = 1) Long typeId);

    @Operation(summary = "добавить тип животного", description = "Добавление типа животного по введенным параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = AnimalTypeDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "409", description = "Тип животного с таким type уже существует",
                    content = @Content(schema = @Schema(implementation = ConflictDataException.class)))
    })
    @PostMapping
    ResponseEntity<AnimalTypeDtoResponse> addAnimalType(@RequestBody @Valid AnimalTypeDtoRequest request);


    @Operation(summary = "обновить тип животного", description = "обновляет тип животного по введенным параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = AnimalTypeDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Тип животного с таким ID не найден",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class))),
            @ApiResponse(responseCode = "409", description = "Тип животного с таким type уже существует",
                    content = @Content(schema = @Schema(implementation = ForbiddenException.class)))
    })
    @PutMapping("/{typeId}")
    ResponseEntity<AnimalTypeDtoResponse> updateAnimalType(@PathVariable @Min(value = 1) Long typeId,
                                       @RequestBody @Valid AnimalTypeDtoRequest request);


    @Operation(summary = "удалить тип животного", description = "удаление типа животного по введенным параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запроса, Есть животные с типом с Id",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Тип животного с таким ID не найден",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class))),
    })
    @DeleteMapping("/{typeId}")
    ResponseEntity<Void> deleteAnimalType(@PathVariable @Min(value = 1) Long typeId);
}
