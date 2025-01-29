package com.example.demo.controller.impl;

import com.example.demo.controller.AccountsController;
import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Accounts", description = "Управление учетными записями пользователей")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsControllerImpl implements AccountsController {

    private final AccountService accountServiceImpl;
    private final AccountMapper accountMapper;

    @Override
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
    public ResponseEntity<AccountDtoResponse> getAccountId(@PathVariable @Min(value = 1) int accountId) {
        Account accountFounded = accountServiceImpl.findAccountById(accountId);
        return ResponseEntity.ok(accountMapper.toAccountDtoResponse(accountFounded));
    }

    @Override
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
    public ResponseEntity<List<AccountDtoResponse>> searchAccount(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") @Min(value = 0) int from,
            @RequestParam(defaultValue = "10") @Min(value = 1) int size) {
        return ResponseEntity.ok().body(accountServiceImpl.searchAccount(firstName, lastName, email, from, size)
                .stream()
                .map(accountMapper::toAccountDtoResponse)
                .toList());
    }

    @Override
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
    public ResponseEntity<AccountDtoResponse> updateAccount(
            @PathVariable @Min(value = 1) int accountId,
            @RequestBody @Valid AccountDtoRequest request,
            Principal principal) {
        return ResponseEntity
                .ok()
                .body(accountMapper.toAccountDtoResponse(
                        accountServiceImpl.updateAccount(accountId, request, principal)
                ));
    }

    @Override
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
    public ResponseEntity<String> deleteAccount(
            @PathVariable @Min(value = 1) int accountId,
            Principal principal) {
        accountServiceImpl.deleteAccount(accountId, principal);
        return ResponseEntity.ok("");
    }
}