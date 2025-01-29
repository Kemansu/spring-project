package com.example.demo.controller.impl;

import com.example.demo.controller.LocationsController;
import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.service.LocationService;
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

@Tag(name = "Локации", description = "Управление точками локаций")
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationsControllerImpl implements LocationsController {
    private final LocationService locationServiceImpl;


    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Получение локации", description = "Возвращает локацию по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Точка локации с таким ID не найдена")
    })
    public ResponseEntity<LocationDtoResponse> getLocation(@PathVariable @Min(value = 1) long id) {
        return ResponseEntity.ok().body(locationServiceImpl.findLocationById(id));
    }

    @Override
    @PostMapping("")
    @Operation(summary = "Добавить локацию",
            description = "Создает новую локацию по введенным параметрам и возвращает добавленную локацию")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Локация успешно создана"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные"),
            @ApiResponse(responseCode = "409",
                    description = "Точка локации с такими latitude и longitude уже сущесвтует")
    })
    public ResponseEntity<LocationDtoResponse> addLocation(@RequestBody @Valid LocationDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationServiceImpl.save(request));
    }

    @Override
    @PutMapping("/{pointId}")
    @Operation(summary = "Обновить данные локации",
            description = "Обновляет данные локации и возвращает обновленные данные")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Локация успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Точка локации с таким ID не найдена"),
            @ApiResponse(responseCode = "409",
                    description = "Точка локации с такими latitude и longitude уже сущесвтует")
    })
    public ResponseEntity<LocationDtoResponse> updateLocation(@RequestBody @Valid LocationDtoRequest request,
                                              @PathVariable @Min(value = 1) long pointId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locationServiceImpl.updateLocation(pointId, request));

    }

    @Override
    @DeleteMapping("/{pointId}")
    @Operation(summary = "Удалить локацию", description = "удаляет локацию по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Локация успешно удалена"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Точка локации с таким ID не найдена")

    })
    public ResponseEntity<String> deleteLocation(@PathVariable @Min(value = 1) long pointId) {

        locationServiceImpl.deleteLocation(pointId);
        return ResponseEntity.ok("");
    }


}
