package com.example.demo.controller.impl;

import com.example.demo.controller.AnimalVisitedLocationsController;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoRequest;
import com.example.demo.dto.animalVisitedLocations.AnimalVisitedLocationsDtoResponse;
import com.example.demo.mapper.AnimalVisitedLocationsMapper;
import com.example.demo.service.AnimalService;
import com.example.demo.service.AnimalVisitedLocationsService;
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

@Tag(name = "Animal Visited Locations",
        description = "Операции по управлению локациями, которые посетило животное")
@RestController
@RequestMapping("/animals")
@RequiredArgsConstructor
public class AnimalVisitedLocationsControllerImpl implements AnimalVisitedLocationsController {

    private final AnimalVisitedLocationsService animalVisitedLocationsServiceImpl;
    private final AnimalService animalServiceImpl;
    private final AnimalVisitedLocationsMapper visitedLocationsMapper;

    @Override
    @Operation(
            summary = "Получить локации, которые посетило животное",
            description = "Возвращает список локаций, посещенных указанным животным, " +
                    "возможно с фильтрацией по датам (startDateTime, endDateTime). " +
                    "Результат выводится постранично (параметры from, size)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список локаций успешно получен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Животное не найдено")
    })
    @GetMapping("/{animalId}/locations")
    public ResponseEntity<List<AnimalVisitedLocationsDtoResponse>> getVisitedLocations(
            @PathVariable @Min(value = 1) long animalId,
            @RequestParam(required = false) String startDateTime,
            @RequestParam(required = false) String endDateTime,
            @RequestParam(defaultValue = "0") @Min(value = 0) int from,
            @RequestParam(defaultValue = "10") @Min(value = 1) int size) {

        return ResponseEntity
                .ok()
                .body(animalVisitedLocationsServiceImpl
                        .getAnimalVisitedLocations(animalId, startDateTime, endDateTime, from, size));
    }

    @Override
    @Operation(
            summary = "Добавить новую посещенную локацию",
            description = "Создает запись о том, что указанное животное посетило локацию (pointId)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Локация успешно добавлена"),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запроса, " +
                            "Животное находится в точке чипирования и никуда не перемещалось, " +
                            "попытка добавить точку локации, равную точке чипирования" +
                            "Попытка добавить точку локации, в которой уже находится животное"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "404", description = "Животное с animalId не найдено" +
                    "Точка локации с pointId не найдена")
    })
    @PostMapping("/{animalId}/locations/{pointId}")
    public ResponseEntity<AnimalVisitedLocationsDtoResponse> addVisitedLocation(
            @PathVariable @Min(value = 1) long animalId,
            @PathVariable @Min(value = 1) long pointId) {

        animalServiceImpl.addAnimalVisitedLocation(animalId, pointId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(visitedLocationsMapper.toAnimalVisitedLocationsDtoResponse(
                        animalVisitedLocationsServiceImpl.findByAnimalIdAndLocationId(animalId, pointId)
                ));
    }

    @Override
    @Operation(
            summary = "Обновить запись о посещенной локации",
            description = "Обновляет детали посещенной локации у животного (к примеру, если " +
                    "нужно изменить дату или другое поле по логике)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные локации успешно обновлены"),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запроса, " +
                            "Обновление первой посещенной точки на точку чипирования" +
                            "Обновление точки на такую же точку" +
                            "Обновление точки локации на точку, совпадающую со следующей и/или с предыдущей точками"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "404", description = "Животное с animalId не найдено" +
                    "Объект с информацией о посещенной точке локации" +
                    " с visitedLocationPointId не найден." +
                    "У животного нет объекта с информацией о посещенной точке локации с visitedLocationPointId." +
                    "Точка локации с locationPointId не найден")
    })
    @PutMapping("/{animalId}/locations")
    public ResponseEntity<AnimalVisitedLocationsDtoResponse> updateVisitedLocation(
            @PathVariable @Min(value = 1) long animalId,
            @RequestBody @Valid AnimalVisitedLocationsDtoRequest request) {

        return ResponseEntity
                .ok()
                .body(animalVisitedLocationsServiceImpl.updateAnimalVisitedLocation(animalId, request));
    }

    @Override
    @Operation(
            summary = "Удалить запись о посещенной локации",
            description = "Удаляет указанный идентификатор посещенной локации (visitedPointId) у животного."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен" +
                    "(Если удаляется первая посещенная точка локации, а вторая точка совпадает с точкой чипирования, " +
                    "то она удаляется автоматически)"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Животное с animalId не найдено\n" +
                    "Объект с информацией о посещенной точке локации с visitedPointId не найден.\n" +
                    "У животного нет объекта с информацией о посещенной точке локации с visitedPointId")
    })
    @DeleteMapping("/{animalId}/locations/{visitedPointId}")
    public ResponseEntity<String> deleteVisitedLocation(
            @PathVariable @Min(value = 1) long animalId,
            @PathVariable @Min(value = 1) long visitedPointId) {

        animalVisitedLocationsServiceImpl.deleteAnimalVisitedLocation(animalId, visitedPointId);
        return ResponseEntity.ok("");
    }
}
