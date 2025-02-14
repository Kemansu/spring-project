package com.example.demo.controller;

import com.example.demo.dto.location.LocationDtoRequest;
import com.example.demo.dto.location.LocationDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
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

public interface LocationsController {

    @Operation(summary = "Получение локации", description = "Возвращает локацию по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                    content = @Content(schema = @Schema(implementation = LocationDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Точка локации с таким ID не найдена",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<LocationDtoResponse> getLocation(@PathVariable @Min(value = 1) Long id);

    @Operation(summary = "Добавить локацию",
            description = "Создает новую локацию по введенным параметрам и возвращает добавленную локацию")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Локация успешно создана",
                    content = @Content(schema = @Schema(implementation = LocationDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "409",
                    description = "Точка локации с такими latitude и longitude уже сущесвтует",
                    content = @Content(schema = @Schema(implementation = ConflictDataException.class)))
    })
    @PostMapping
    ResponseEntity<LocationDtoResponse> addLocation(@RequestBody @Valid LocationDtoRequest request);


    @Operation(summary = "Обновить данные локации",
            description = "Обновляет данные локации и возвращает обновленные данные")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Локация успешно обновлена",
                    content = @Content(schema = @Schema(implementation = LocationDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "404", description = "Точка локации с таким ID не найдена",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class))),
            @ApiResponse(responseCode = "409",
                    description = "Точка локации с такими latitude и longitude уже сущесвтует",
                    content = @Content(schema = @Schema(implementation = ConflictDataException.class)))
    })
    @PutMapping("/{pointId}")
    ResponseEntity<LocationDtoResponse> updateLocation(@RequestBody @Valid LocationDtoRequest request,
                                     @PathVariable @Min(value = 1) Long pointId);


    @Operation(summary = "Удалить локацию", description = "удаляет локацию по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Локация успешно удалена"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Точка локации с таким ID не найдена",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))

    })
    @DeleteMapping("/{pointId}")
    ResponseEntity<Void> deleteLocation(@PathVariable @Min(value = 1) Long pointId);
}
