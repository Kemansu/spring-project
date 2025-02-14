package com.example.demo.controller;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ForbiddenException;
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

import java.security.Principal;
import java.util.List;

public interface AccountsController {

    @Operation(
            summary = "Получить учетную запись по ID",
            description = "Возвращает данные учетной записи по ее ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Учетная запись успешно найдена",
                    content = @Content(schema = @Schema(implementation = AccountDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный ID",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "404", description = "Аккаунт с таким accountId не найден",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @GetMapping("/{accountId}")
    ResponseEntity<AccountDtoResponse> getAccountId(@PathVariable @Min(value = 1) Integer accountId);

    @Operation(
            summary = "Поиск учетных записей",
            description = "Позволяет искать учетные записи по имени, фамилии, email с пагинацией"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список учетных записей получен",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = AccountDtoResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401", description = "Неверные авторизационные данные",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class)))
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
            @ApiResponse(responseCode = "200", description = "Список учетных записей получен",
                    content = @Content(schema = @Schema(implementation = AccountDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "403", description = "Обновление не своего аккаунта, аккаунт не найден",
                    content = @Content(schema = @Schema(implementation = ForbiddenException.class))),
            @ApiResponse(responseCode = "409", description = "Аккаунт с таким email уже существует",
                    content = @Content(schema = @Schema(implementation = ConflictDataException.class)))
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
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса, Аккаунт связан с животным",
                    content = @Content(schema = @Schema(implementation = RequestValidationException.class))),
            @ApiResponse(responseCode = "401",
                    description = "Неверные авторизационные данные, Запрос от неавторизованного аккаунта",
                    content = @Content(schema = @Schema(implementation = AuthenticationException.class))),
            @ApiResponse(responseCode = "403", description = "Обновление не своего аккаунта, аккаунт не найден",
                    content = @Content(schema = @Schema(implementation = ForbiddenException.class)))
    })
    @DeleteMapping("/{accountId}")
    ResponseEntity<Void> deleteAccount(@PathVariable @Min(value = 1) Integer accountId,
                                    Principal principal);
}
