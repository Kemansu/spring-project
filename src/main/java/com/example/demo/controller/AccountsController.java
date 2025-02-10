package com.example.demo.controller;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

public interface AccountsController {

    @Operation(
            summary = "Получить учетную запись по ID",
            description = "Возвращает данные учетной записи по ее ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Учетная запись успешно найдена"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID"),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные"),
            @ApiResponse(responseCode = "404", description = "Аккаунт с таким accountId не найден")
    })
    @GetMapping("/{accountId}")
    ResponseEntity<AccountDtoResponse> getAccountId(@PathVariable @Min(value = 1) Integer accountId);

    @Operation(
            summary = "Поиск учетных записей",
            description = "Позволяет искать учетные записи по имени, фамилии, email с пагинацией"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список учетных записей получен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные")
    })
    @GetMapping("/search")
    ResponseEntity<List<AccountDtoResponse>> searchAccount(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(defaultValue = "10") @Min(value = 1) Integer size);

    @Operation(
            summary = "Обновить учетную запись",
            description = "Обновляет данные учетной записи по ее ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список учетных записей получен"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "403", description = "Обновление не своего аккаунта, аккаунт не найден"),
            @ApiResponse(responseCode = "409", description = "Аккаунт с таким email уже существует")
    })
    @PutMapping("/{accountId}")
    ResponseEntity<AccountDtoResponse> updateAccount(@PathVariable @Min(value = 1) Integer accountId,
                                    @RequestBody @Valid AccountDtoRequest request,
                                    Principal principal);

    @Operation(
            summary = "Удалить учетную запись",
            description = "Удаляет учетную запись по ее ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Учетная запись успешно удалена"),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса, Аккаунт связан с животным"),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта"),
            @ApiResponse(responseCode = "403", description = "Обновление не своего аккаунта, аккаунт не найден")
    })
    @DeleteMapping("/{accountId}")
    ResponseEntity<Void> deleteAccount(@PathVariable @Min(value = 1) Integer accountId,
                                    Principal principal);
}
