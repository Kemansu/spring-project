package com.example.demo.controller;

import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoSearchRequest;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AnimalVisitedLocationsController {


    @Operation(
            summary = "Получить локации, которые посетило животное",
            description = "Возвращает список локаций, посещенных указанным животным, " +
                    "возможно с фильтрацией по датам (startDateTime, endDateTime). " +
                    "Результат выводится постранично (параметры from, size)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список локаций успешно получен",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AnimalVisitedLocationsDtoResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Животное не найдено",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @GetMapping("/{animalId}/locations")
    ResponseEntity<List<AnimalVisitedLocationsDtoResponse>> getVisitedLocations(
            @PathVariable @Min(value = 1) Long animalId,
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime,
            @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(defaultValue = "10") @Min(value = 1) Integer size);

    @Operation(
            summary = "Добавить новую посещенную локацию",
            description = "Создает запись о том, что указанное животное посетило локацию (pointId)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Локация успешно добавлена",
                    content = @Content(schema = @Schema(implementation = AnimalVisitedLocationsDtoResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запроса, " +
                            "Животное находится в точке чипирования и никуда не перемещалось, " +
                            "попытка добавить точку локации, равную точке чипирования" +
                            "Попытка добавить точку локации, в которой уже находится животное",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Животное с animalId не найдено" +
                    "Точка локации с pointId не найдена",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @PostMapping("/{animalId}/locations/{pointId}")
    ResponseEntity<AnimalVisitedLocationsDtoResponse> addVisitedLocation(
            @PathVariable @Min(value = 1) Long animalId,
            @PathVariable @Min(value = 1) Long pointId);

    @Operation(
            summary = "Обновить запись о посещенной локации",
            description = "Обновляет детали посещенной локации у животного (к примеру, если " +
                    "нужно изменить дату или другое поле по логике)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные локации успешно обновлены",
                    content = @Content(schema = @Schema(implementation = AnimalVisitedLocationsDtoResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запроса, " +
                            "Обновление первой посещенной точки на точку чипирования" +
                            "Обновление точки на такую же точку" +
                            "Обновление точки локации на точку, совпадающую со следующей и/или с предыдущей точками",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Животное с animalId не найдено" +
                    "Объект с информацией о посещенной точке локации" +
                    " с visitedLocationPointId не найден." +
                    "У животного нет объекта с информацией о посещенной точке локации с visitedLocationPointId." +
                    "Точка локации с locationPointId не найден",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @PutMapping("/{animalId}/locations")
    ResponseEntity<AnimalVisitedLocationsDtoResponse> updateVisitedLocation(
            @PathVariable @Min(value = 1) Long animalId,
            @RequestBody @Valid AnimalVisitedLocationsDtoRequest request);


    @Operation(
            summary = "Удалить запись о посещенной локации",
            description = "Удаляет указанный идентификатор посещенной локации (visitedPointId) у животного."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен" +
                    "(Если удаляется первая посещенная точка локации, а вторая точка совпадает с точкой чипирования, " +
                    "то она удаляется автоматически)"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "404", description = "Животное с animalId не найдено\n" +
                    "Объект с информацией о посещенной точке локации с visitedPointId не найден.\n" +
                    "У животного нет объекта с информацией о посещенной точке локации с visitedPointId",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @DeleteMapping("/{animalId}/locations/{visitedPointId}")
    ResponseEntity<Void> deleteVisitedLocation(@PathVariable @Min(value = 1) Long animalId,
                                            @PathVariable @Min(value = 1) Long visitedPointId);
}
