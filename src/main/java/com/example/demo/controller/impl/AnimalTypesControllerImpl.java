package com.example.demo.controller.impl;

import com.example.demo.controller.AnimalTypesController;
import com.example.demo.dto.animalType.AnimalTypeDtoRequest;
import com.example.demo.dto.animalType.AnimalTypeDtoResponse;
import com.example.demo.mapper.AnimalTypesMapper;
import com.example.demo.model.AnimalType;
import com.example.demo.service.AnimalTypeService;
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

@Tag(name = "Типы животных" ,description = "Управление типами животных")
@RestController
@RequestMapping("/animals/types")
@RequiredArgsConstructor
public class AnimalTypesControllerImpl implements AnimalTypesController {

    private final AnimalTypeService animalTypeServiceImpl;
    private final AnimalTypesMapper animalTypesMapper;


    @Override
    @GetMapping("/{typeId}")
    @Operation(summary = "Получить тип животного", description = "Возвращает тип животного по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Тип животного с таким ID не найден")
    })
    public ResponseEntity<AnimalTypeDtoResponse> getAnimalType(@PathVariable @Min(value = 1) long typeId) {
        return ResponseEntity
                .ok()
                .body(animalTypesMapper.toAnimalTypeDtoResponse(animalTypeServiceImpl.getAnimalTypeById(typeId)));
    }

    @Override
    @PostMapping("")
    @Operation(summary = "добавить тип животного", description = "Добавление типа животного по введенным параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Неверные авторизационные данные"),
            @ApiResponse(responseCode = "409", description = "Тип животного с таким type уже существует")
    })
    public ResponseEntity<AnimalTypeDtoResponse> addAnimalType(@RequestBody @Valid AnimalTypeDtoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(animalTypesMapper.toAnimalTypeDtoResponse(
                animalTypeServiceImpl.saveAnimalType(request)
                        )
                );
    }

    @Override
    @PutMapping("/{typeId}")
    @Operation(summary = "обновить тип животного", description = "обновляет тип животного по введенным параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Тип животного с таким ID не найден"),
            @ApiResponse(responseCode = "409", description = "Тип животного с таким type уже существует")
    })
    public ResponseEntity<AnimalTypeDtoResponse> updateAnimalType(@PathVariable @Min(value = 1)long typeId,
                                                  @RequestBody @Valid AnimalTypeDtoRequest request) {
        return ResponseEntity
                .ok()
                .body(animalTypesMapper.toAnimalTypeDtoResponse(
                animalTypeServiceImpl.updateAnimalType(typeId, request)
                        ));
    }

    @Override
    @DeleteMapping("/{typeId}")
    @Operation(summary = "удалить тип животного", description = "удаление типа животного по введенным параметрам")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
            @ApiResponse(responseCode = "400",
                    description = "Невалидные параметры запроса, Есть животные с типом с Id"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Тип животного с таким ID не найден"),
    })
    public ResponseEntity<String> deleteAnimalType(@PathVariable @Min(value = 1) long typeId) {
        animalTypeServiceImpl.deleteAnimalType(typeId);
        return ResponseEntity.ok("");
    }


}
